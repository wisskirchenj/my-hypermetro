package de.cofinpro.metro.controller;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.controller.command.CommandType;
import de.cofinpro.metro.controller.command.LineCommand;
import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;
import de.cofinpro.metro.model.MetroLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * controller class for the Hypermetro application. On construction it gets instances of a StationsReader
 * and StationsPrinter for the IO. Its only public run-method performs the application flow.
 */
public class MetroController {

    private static final String DEPOT_STATION = "depot";
    private final StationsReader stationsReader;
    private final StationsPrinter stationsPrinter;
    private final CommandLineInterpreter commandLineInterpreter;
    private Map<String, MetroLine> lines;

    public MetroController(StationsReader stationsReader, StationsPrinter stationsPrinter,
                           CommandLineInterpreter commandLineInterpreter) {
        this.stationsReader = stationsReader;
        this.stationsPrinter = stationsPrinter;
        this.commandLineInterpreter = commandLineInterpreter;
        this.commandLineInterpreter.setPrinter(stationsPrinter);
    }

    /**
     * read the given input file into a list of stations and prepend, append the 'depot' station.
     * Then call the Printer's output method.
     * @param linesJsonFilePath path to the json lines file.
     */
    public void run(String linesJsonFilePath) {
        if (!readLines(linesJsonFilePath)) {
            return;
        }
        LineCommand command = commandLineInterpreter.parseNext();
        while (command.getType() != CommandType.EXIT) {
            command.execute(lines);
            command = commandLineInterpreter.parseNext();
        }
    }

    private boolean readLines(String linesJsonFilePath) {
        try {
            lines = stationsReader.readJsonFile(linesJsonFilePath);
        } catch (JsonParseException | IllegalStateException | NumberFormatException | UnsupportedOperationException e) {
            stationsPrinter.printError("Incorrect file");
            return false;
        } catch (FileNotFoundException e) {
            stationsPrinter.printError("Error! Such a file doesn't exist!");
            return false;
        } catch (IOException e) {
            stationsPrinter.printError(e.toString());
            return false;
        }
        lines.values().forEach(this::addDepotStation);
        return true;
    }

    /**
     * pre- and append the 'depot'-station to the read-in list.
     * @param stations the read-in list to be extended
     */
    private void addDepotStation(List<String> stations) {
        stations.add(DEPOT_STATION);
        stations.add(0, DEPOT_STATION);
    }
}
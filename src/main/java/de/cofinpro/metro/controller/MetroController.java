package de.cofinpro.metro.controller;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.controller.command.CommandType;
import de.cofinpro.metro.controller.command.LineCommand;
import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.io.MetroReader;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * controller class for the Hypermetro application. On construction it gets instances of a MetroReader
 * CommandLineInterpreter and StationsPrinterO. Its only public run-method performs the application flow.
 */
public class MetroController {

    private final MetroReader metroReader;
    private final MetroPrinter metroPrinter;
    private final CommandLineInterpreter commandLineInterpreter;
    private MetroNet lines;

    public MetroController(MetroReader metroReader, MetroPrinter metroPrinter,
                           CommandLineInterpreter commandLineInterpreter) {
        this.metroReader = metroReader;
        this.metroPrinter = metroPrinter;
        this.commandLineInterpreter = commandLineInterpreter;
        this.commandLineInterpreter.setPrinter(metroPrinter);
    }

    /**
     * read the given input file into a list of stations and prepend, append the 'depot' station.
     * Then start the CLI-loop.
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

    /**
     * read the Json File with metro data from the given path and start the GSON-parsing using the MetroPrinter.
     * After reading in into the lines map, each line gets the "depot" station appended and prepended.
     * @param linesJsonFilePath path to json
     * @return true if read was successful, false if an exception occurred.
     */
    private boolean readLines(String linesJsonFilePath) {
        try {
            lines = metroReader.readJsonFile(linesJsonFilePath);
        } catch (JsonParseException | IllegalStateException | NumberFormatException | UnsupportedOperationException e) {
            metroPrinter.printError("Incorrect file");
            return false;
        } catch (FileNotFoundException e) {
            metroPrinter.printError("Error! Such a file doesn't exist!");
            return false;
        } catch (IOException e) {
            metroPrinter.printError(e.toString());
            return false;
        }
        lines.values().forEach(this::addDepotStation);
        return true;
    }

    /**
     * pre- and append the 'depot'-station to the read-in list.
     * @param line the read-in metro line to be extended
     */
    private void addDepotStation(MetroLine line) {
        Station depotStation = new Station("depot");
        depotStation.setLine(line.getName());
        line.add(depotStation);
        line.add(0, depotStation);
    }
}
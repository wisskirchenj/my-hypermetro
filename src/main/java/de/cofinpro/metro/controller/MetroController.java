package de.cofinpro.metro.controller;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * controller class for the Hypermetro application. On construction it gets instances of a StationsReader
 * and StationsPrinter for the IO. Its only public run-method performs the application flow.
 */
public class MetroController {

    private static final String DEPOT_STATION = "depot";
    private final StationsReader stationsReader;
    private final StationsPrinter stationsPrinter;

    public MetroController(StationsReader stationsReader, StationsPrinter stationsPrinter) {
        this.stationsReader = stationsReader;
        this.stationsPrinter = stationsPrinter;
    }

    /**
     * read the given input file into a list of stations and prepend, append the 'depot' station.
     * Then call the Printer's output method.
     * @param stationsFile path to the stations file.
     */
    public void run(String stationsFile) {
        List<String> stations;
        try {
            stations = stationsReader.readFile(stationsFile);
        } catch (FileNotFoundException e) {
            stationsPrinter.printError("Error! Such a file doesn't exist!");
            return;
        } catch (IOException e) {
            stationsPrinter.printError(e.toString());
            return;
        }
        addDepotStation(stations);
        stationsPrinter.print(stations);
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
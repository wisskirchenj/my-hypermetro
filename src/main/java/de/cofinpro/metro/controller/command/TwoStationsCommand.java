package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;

import java.util.Optional;

/**
 * abstract parent class to LineCommands, that need two stations input with line name and station name each.
 * It comprises the error handling for finding both the stations in the net and calls an abstract hook
 * method to be implemented by deriving classes for the actual execute command.
 */
public abstract class TwoStationsCommand implements LineCommand {

    protected final MetroPrinter metroPrinter;
    protected final String lineName;
    protected final String stationName;
    protected final String transferLineName;
    protected final String transferStationName;

    protected TwoStationsCommand(MetroPrinter metroPrinter, String lineName, String stationName,
                                 String transferLineName, String transferStationName) {
        this.metroPrinter = metroPrinter;
        this.lineName = lineName;
        this.stationName = stationName;
        this.transferLineName = transferLineName;
        this.transferStationName = transferStationName;
    }

    /**
     * connects the station given by name and line to the station given by transferLine, -name,
     * and vice versa - iff the lines and stations are found. If not, invalid command is thrown.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(MetroNet lines) {
        Optional<Station> from = lines.findStation(lineName, stationName);
        Optional<Station> to = lines.findStation(transferLineName, transferStationName);
        if (from.isEmpty() || to.isEmpty()) {
            metroPrinter.printError("Invalid Command");
        } else {
            executeTwoStationsCommand(lines, from.get(), to.get());
        }
    }

    /**
     * hook for extending classes that gives the found stations and the metor net for the actual command
     * execution
     * @param lines metro net data
     * @param from first station from string name params
     * @param to second station from the name params
     */
    protected abstract void executeTwoStationsCommand(MetroNet lines, Station from, Station to);
}


package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

/**
 * InsertCommand class is used for the /append as well as /add-head commands on the command line.
 * The command has two arguments lineName and stationName.
 */
public class InsertCommand implements LineCommand {

    private final CommandType type;
    private final StationsPrinter stationsPrinter;
    private final String lineName;
    private final String stationName;

    public InsertCommand(CommandType type, StationsPrinter printer, String lineName, String stationName) {
        this.type = type;
        this.stationsPrinter = printer;
        this.lineName = lineName;
        this.stationName = stationName;
    }

    /**
     * inserts the station given by name either at index position 1 (after depot) in case of /add-head
     * or before the last station (depot) in case of /append.
     * If line is not found by name: Invalid Command is print out.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(Map<String, MetroLine> lines) {
        MetroLine line = lines.get(lineName);
        if (line == null) {
            stationsPrinter.printError("Invalid Command");
        } else {
            line.addStationByName(type == CommandType.ADD_HEAD ? 1 : line.size() - 1, stationName);
        }
    }

    @Override
    public CommandType getType() {
        return type;
    }
}

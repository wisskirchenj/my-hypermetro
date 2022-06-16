package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;

/**
 * InsertCommand class is used for the /append as well as /add-head commands on the command line.
 * The command has two arguments lineName and stationName.
 */
public class InsertCommand implements LineCommand {

    private final CommandType type;
    private final MetroPrinter metroPrinter;
    private final String lineName;
    private final String stationName;
    private final String time;

    public InsertCommand(CommandType type, MetroPrinter printer, String lineName, String stationName, String time) {
        this.type = type;
        this.metroPrinter = printer;
        this.lineName = lineName;
        this.stationName = stationName;
        this.time = time;
    }

    /**
     * inserts the station given by name either at index position 1 (after depot) in case of /add-head
     * or before the last station (depot) in case of /append.
     * If line is not found by name: Invalid Command is print out.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(MetroNet lines) {
        MetroLine line = lines.get(lineName);
        if (line == null || !time.matches("\\d+")) {
            metroPrinter.printError("Invalid Command");
        } else {
            line.addStationByName(type == CommandType.ADD_HEAD ? 1 : line.size() - 1, stationName, Integer.parseInt(time));
        }
    }

    @Override
    public CommandType getType() {
        return type;
    }
}

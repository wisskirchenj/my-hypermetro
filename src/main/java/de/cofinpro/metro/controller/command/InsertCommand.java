package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

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

    @Override
    public void execute(Map<String, MetroLine> lines) {
        MetroLine line = lines.get(lineName);
        if (line == null) {
            stationsPrinter.printError("Invalid Command");
        } else {
            line.add(type == CommandType.ADD_HEAD ? 1 : line.size() - 1, stationName);
        }
    }

    @Override
    public CommandType getType() {
        return type;
    }
}

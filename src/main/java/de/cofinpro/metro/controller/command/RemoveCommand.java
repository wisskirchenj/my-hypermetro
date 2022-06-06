package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

public class RemoveCommand implements LineCommand {

    private final StationsPrinter stationsPrinter;
    private final String lineName;
    private final String stationName;

    public RemoveCommand(StationsPrinter printer, String lineName, String stationName) {
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
            line.remove(stationName);
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.REMOVE;
    }
}

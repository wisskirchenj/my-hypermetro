package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

public class InvalidCommand implements LineCommand {

    private final StationsPrinter  stationsPrinter;
    public InvalidCommand(StationsPrinter printer) {
        this.stationsPrinter = printer;
    }

    @Override
    public void execute(Map<String, MetroLine> lines) {
        stationsPrinter.printError("Invalid Command");
    }

    @Override
    public CommandType getType() {
        return CommandType.INVALID;
    }
}

package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

/**
 * LineCommand implementation that corresponds to an output command for a lineName.
 */
public class OutputCommand implements LineCommand {

    private final StationsPrinter stationsPrinter;
    private final String lineName;

    public OutputCommand(StationsPrinter printer, String lineName) {
        this.stationsPrinter = printer;
        this.lineName = lineName;
    }

    /**
     * print the stations sequence for the given lineName using the StationsPrinter - or
     * if the line is not contained in the lines map, output invalid command.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(Map<String, MetroLine> lines) {
        MetroLine line = lines.get(lineName);
        if (line == null) {
            stationsPrinter.printError("Invalid Command");
        } else {
            stationsPrinter.print(line);
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.OUTPUT;
    }
}
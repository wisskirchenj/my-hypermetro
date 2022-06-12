package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;

/**
 * LineCommand implementation that corresponds to an output command for a lineName.
 */
public class OutputCommand implements LineCommand {

    private final MetroPrinter metroPrinter;
    private final String lineName;

    public OutputCommand(MetroPrinter printer, String lineName) {
        this.metroPrinter = printer;
        this.lineName = lineName;
    }

    /**
     * print the stations sequence for the given lineName using the MetroPrinter - or
     * if the line is not contained in the lines map, output invalid command.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(MetroNet lines) {
        MetroLine line = lines.get(lineName);
        if (line == null) {
            metroPrinter.printError("Invalid Command");
        } else {
            metroPrinter.printLine(line);
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.OUTPUT;
    }
}

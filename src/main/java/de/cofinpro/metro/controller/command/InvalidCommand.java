package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroNet;

/**
 * InvalidCommand objects are instantiated, if the CL-Interpreter parsing cannot recognize a known command
 * as well as if the parameters to a known command do not match the syntax.
 */
public class InvalidCommand implements LineCommand {

    private final MetroPrinter metroPrinter;

    public InvalidCommand(MetroPrinter printer) {
        this.metroPrinter = printer;
    }

    @Override
    public void execute(MetroNet lines) {
        metroPrinter.printError("Invalid Command");
    }

    @Override
    public CommandType getType() {
        return CommandType.INVALID;
    }
}

package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.model.MetroNet;

/**
 * the exit command, that stops the CL-Interpreter run loop in our controller.
 */
public class ExitCommand implements LineCommand {

    @Override
    public void execute(MetroNet lines) {
        // nothing to do - as the run loop exits on receiving this command (never called)
    }

    @Override
    public CommandType getType() {
        return CommandType.EXIT;
    }
}

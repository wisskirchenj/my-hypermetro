package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

public class ExitCommand implements LineCommand {
    @Override
    public void execute(Map<String, MetroLine> lines) {
        // nothing to do -
    }

    @Override
    public CommandType getType() {
        return CommandType.EXIT;
    }
}

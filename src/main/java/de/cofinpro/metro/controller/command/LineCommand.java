package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

public interface LineCommand {

    void execute(Map<String, MetroLine> lines);

    CommandType getType();
}

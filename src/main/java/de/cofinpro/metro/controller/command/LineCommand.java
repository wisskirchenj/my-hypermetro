package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.model.MetroLine;

import java.util.Map;

/**
 * Command-pattern interface: implementing classes are executable (Concrete) LineCommands
 */
public interface LineCommand {

    /**
     * execute the command on the given metro lines.
     * The execute-command may change the lines map content (e.g.: append, add-head or remove)
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    void execute(Map<String, MetroLine> lines);

    /**
     * getter for the commands type
     * @return the CommandType enum
     */
    CommandType getType();
}

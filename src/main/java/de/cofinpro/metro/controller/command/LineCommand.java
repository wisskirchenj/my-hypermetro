package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.model.MetroNet;

/**
 * Command-pattern interface: implementing classes are executable (Concrete) LineCommands
 */
public interface LineCommand {

    /**
     * execute the command on the given metro net.
     * The execute-command may change the lines map content (e.g.: append, add-head or remove)
     * @param lines MetroNet lineName-key map containing all Metrolines in the current state
     */
    void execute(MetroNet lines);

    CommandType getType();
}

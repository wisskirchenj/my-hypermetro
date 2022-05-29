package de.cofinpro.metro;

import de.cofinpro.metro.controller.MetroController;
import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;
import lombok.extern.slf4j.Slf4j;

/**
 * Application main for the Hypermetro CL-application.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: java metro.Main <path-to-stations-file.txt>");
            return;
        }
        new MetroController(new StationsReader(), new StationsPrinter()).run(args[0]);
    }
}

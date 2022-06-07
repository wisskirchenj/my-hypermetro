package de.cofinpro.metro.io;

import de.cofinpro.metro.model.Station;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * printer class to output a stations list - uses log4j (resp. System.out.* for hyperskill tests)
 */
@Slf4j
public class StationsPrinter {

    /**
     * log the stations list in the desired format
     * @param stations list of station name strings
     */
    public void print(List<Station> stations) {
        String output = createOutputFromList(stations);
        log.info(output);
    }

    /**
     * test-friendly method that creates overall output for the stations given.
     * @param stations list of station name strings
     * @return the total output string
     */
    String createOutputFromList(List<Station> stations) {
        if (stations.size() < 3) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        stations.forEach(station -> builder.append(station).append("\n"));
        return builder.toString();
    }

    /**
     * wrapper Method to log / print an error message
     * @param errorMessage the error message given
     */
    public void printError(String errorMessage) {
        log.error(errorMessage);
    }
}

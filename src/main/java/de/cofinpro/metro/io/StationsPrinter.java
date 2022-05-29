package de.cofinpro.metro.io;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ListIterator;

/**
 * printer class to output a stations list - uses log4j (resp. System.out.* for hyperskill tests)
 */
@Slf4j
public class StationsPrinter {

    private static final String STATION_JOIN = " - ";

    /**
     * log the stations list in the desired format
     * @param stations list of station name strings
     */
    public void print(List<String> stations) {
        String output = createOutputFromList(stations);
        log.info(output);
    }

    /**
     * test-friendly method that creates overall output for the stations given. For each of
     * the stations, it prints an extra line with the previous, current and following stations
     * @param stations list of station name strings
     * @return the total output string
     */
    String createOutputFromList(List<String> stations) {
        if (stations.size() < 3) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        ListIterator<String> iterator = stations.listIterator(1);
        for (int i = 0; i < stations.size() - 2; i++) {
            builder.append(iterator.previous()).append(STATION_JOIN);
            iterator.next();
            builder.append(iterator.next()).append(STATION_JOIN);
            builder.append(iterator.next()).append("\n");
            iterator.previous();
        }
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

package de.cofinpro.metro.io;

import de.cofinpro.metro.model.Station;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * printer class for all outputs of the hypermetro application - uses log4j (resp. System.out.* for hyperskill tests)
 */
@Slf4j
public class MetroPrinter {

    /**
     * log the stations list of a metro line in the desired format
     * @param stations list of station name strings
     */
    public void printLine(List<Station> stations) {
        log.info(createLineInfo(stations));
    }

    /**
     * test-friendly method that creates overall output for the stations given.
     * @param stations list of stations of this line
     * @return the total output string
     */
    String createLineInfo(List<Station> stations) {
        if (stations.size() < 3) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        stations.forEach(station -> builder.append(station).append("\n"));
        return builder.toString();
    }

    /**
     * test-friendly method that creates overall output for the route given.
     * @param route list of stations of this route
     * @return the total output string
     */
    String createRouteInfo(List<Station> route) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < route.size(); i++) {
            builder.append(route.get(i).getName());
            if (i < route.size() - 1 && !route.get(i).getLine().equals(route.get(i + 1).getLine())) {
                builder.append("\nTransition to line ").append(route.get(i + 1).getLine());
            }
            builder.append("\n");
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

    /**
     * log the stations list of the route requested in the desired format
     * @param route list of stations of this route
     */
    public void printRoute(List<Station> route) {
        log.info(createRouteInfo(route));
    }
}

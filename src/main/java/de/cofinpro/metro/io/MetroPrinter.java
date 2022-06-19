package de.cofinpro.metro.io;

import de.cofinpro.metro.model.Station;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * printer class for all outputs of the hyper metro application - uses log4j (resp. System.out.* for hyperskill tests)
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

    String createLineInfo(List<Station> stations) {
        if (stations.size() < 3) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        stations.forEach(station -> builder.append(station).append("\n"));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    String createRouteInfo(List<Station> route) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < route.size(); i++) {
            addStationAndPossibleTransition(i, route, builder);
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private void addStationAndPossibleTransition(int i, List<Station> route, StringBuilder builder) {
        builder.append(route.get(i).getName());
        if (i < route.size() - 1 && !route.get(i).getLine().equals(route.get(i + 1).getLine())) {
            builder.append("\nTransition to line ").append(route.get(i + 1).getLine());
        }
        builder.append("\n");
    }

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

    /**
     * print a travel time estimate line after fastest-route search.
     */
    public void printTotalTime(int totalTime) {
        log.info(String.format("Total: %d minutes in the way", totalTime));
    }
}

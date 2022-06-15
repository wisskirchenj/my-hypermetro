package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.DijkstraSearch;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;

/**
 * LineCommand given by /fastest-route and two line station name pairs, that routes the fastest path
 * including line transfers from the departure to the target station given, considering Json-input,
 * that provides travel time in minutes form a station to the next in line. Uses the Dijkstra
 * algorithm, which is instantiated here.
 */
public class FastestRouteCommand extends TwoStationsCommand {
    public FastestRouteCommand(MetroPrinter metroPrinter, String lineFromName, String stationFromName,
                            String lineToName, String stationToName) {
        super(metroPrinter, lineFromName, stationFromName, lineToName, stationToName);
    }

    /**
     * execute the command by doing a BF-search to get shortest route, which is printed by the MetroPrinter.
     * @param lines metro net data
     * @param from first station from string name params
     * @param to second station from the name params
     */
    @Override
    protected void executeTwoStationsCommand(MetroNet lines, Station from, Station to) {
        DijkstraSearch dijkstraSearch = new DijkstraSearch(lines);
        metroPrinter.printRoute(dijkstraSearch.getRoute(from, to));
        metroPrinter.printTotalTime(dijkstraSearch.getTotalTime());
    }

    @Override
    public CommandType getType() {
        return CommandType.FASTEST_ROUTE;
    }
}

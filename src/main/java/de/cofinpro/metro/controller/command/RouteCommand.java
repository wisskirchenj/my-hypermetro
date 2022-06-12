package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.BreadthFirstSearch;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;

/**
 * LineCommand given by /route and two line station name pairs, that routes the shortest path
 * including line transfers from the departure to the target station given. Uses the BFS tree traversal
 * algorithm, which is instantiated here.
 */
public class RouteCommand extends TwoStationsCommand {


    public RouteCommand(MetroPrinter metroPrinter, String lineFromName, String stationFromName,
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
        metroPrinter.printRoute(new BreadthFirstSearch(lines).getRoute(from, to));
    }

    @Override
    public CommandType getType() {
        return CommandType.ROUTE;
    }
}

package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.BreadthFirstSearch;
import de.cofinpro.metro.model.DijkstraSearch;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;

/**
 * LineCommand given by /route or /fastest-route and two line station name pairs, that routes the shortest, resp. fastest path
 * including line transfers from the departure to the target station given. Uses the BFS tree traversal resp. Dijkstra
 * algorithm, which are instantiated here.
 */
public class RouteCommand extends TwoStationsCommand {

    private final CommandType type;

    public RouteCommand(CommandType type, MetroPrinter metroPrinter, String lineFromName, String stationFromName,
                        String lineToName, String stationToName) {
        super(metroPrinter, lineFromName, stationFromName, lineToName, stationToName);
        this.type = type;
    }

    /**
     * execute the command by doing a BF-search to get shortest or Dijkstra to get the fastest route, which is printed
     * by the MetroPrinter.
     * @param lines metro net data
     */
    @Override
    protected void executeTwoStationsCommand(MetroNet lines, Station from, Station to) {
        if (type == CommandType.ROUTE) {
            metroPrinter.printRoute(new BreadthFirstSearch(lines).getRoute(from, to));
        } else {
            DijkstraSearch dijkstraSearch = new DijkstraSearch(lines);
            metroPrinter.printRoute(dijkstraSearch.getRoute(from, to));
            metroPrinter.printTotalTime(dijkstraSearch.getTotalTime());
        }
    }

    @Override
    public CommandType getType() {
        return type;
    }
}

package de.cofinpro.metro.model;

import java.util.*;

import static java.util.function.Predicate.not;

/**
 * BFS-algorithm implementing class - specialized for the Metro application to find the shortest route between
 * two stations given in the MetroNet, which is passed as construction parameter. An instance of this class
 * should be used ONLY FOR ONE routing, i.e. construct first and then call getRoute(from, to).
 * This makes sense here, because as the metro net can change in each command, a new net has to be used for routing.
 */
public class BreadthFirstSearch {
    
    private final MetroNet lines;
    private final Queue<UpwardsNode> stationQueue;
    private final Set<Station> visited;
    
    public BreadthFirstSearch(MetroNet lines) {
        this.lines = lines;
        stationQueue = new ArrayDeque<>();
        visited = new HashSet<>();
    }

    /**
     * core method to be called after creation, that encapsulates all the BFS algorithm steps and the internal Node
     * structure, collections used.
     * @param from departure station given
     * @param to destination station given
     * @return list of the stations for this route from departure to destination stations
     */
    public List<Station> getRoute(Station from, Station to) {
        enqueue(from, null);
        while (!stationQueue.isEmpty()) {
            UpwardsNode stationNode = stationQueue.poll();
            if (to.equals(stationNode.station)) {
                return getRouteList(stationNode);
            }
            getUnvisitedNeighbors(stationNode.station).forEach(neighbor -> enqueue(neighbor, stationNode));
        }
        throw new NoSuchElementException("Station to route not found!");
    }

    /**
     * helper method that returns all not yet visited (in this BF search) neighbors of a given station.
     * The algorithm adds all possible transfer stations first, then the possible previous station in line and the
     * possible next station.
     * @param station  the station to look for neighbors
     * @return list of unvisited neighbor stations
     */
    private List<Station> getUnvisitedNeighbors(Station station) {
        List<Station> neighbors = new ArrayList<>();
        station.getTransfer().stream()
                .map(transfer -> lines.findStation(transfer.getLine(), transfer.getStation()).orElseThrow())
                .filter(not(visited::contains)).forEach(neighbors::add);
        lines.findPreviousStationInLine(station).stream().filter(not(visited::contains)).forEach(neighbors::add);
        lines.findNextStationInLine(station).stream().filter(not(visited::contains)).forEach(neighbors::add);
        return neighbors;
    }

    /**
     * after the BFS has found the destination station, this method is called and returns the shortest route form
     * departure to destination station. It uses the UpwardsNode structure, that is created during BFS to go back from
     * target via parent to the root.
     * @param stationNode the node of the target station found.
     * @return list with the stations route from departure to target.
     */
    private List<Station> getRouteList(UpwardsNode stationNode) {
        List<Station> routeList = new ArrayList<>();
        routeList.add(stationNode.station);
        while (stationNode.parent != null) {
            stationNode = stationNode.parent;
            routeList.add(0, stationNode.station);
        }
        return routeList;
    }

    /**
     * convenience method that creates an UpwardsNode with the station as data and a given parent
     * AND marks the visited station as visited - using a field-level Set.
     * @param station station to enqueue
     * @param parent the parent of this station - in terms of route...
     */
    private void enqueue(Station station, UpwardsNode parent) {
        stationQueue.offer(new UpwardsNode(station, parent));
        visited.add(station);
    }
}

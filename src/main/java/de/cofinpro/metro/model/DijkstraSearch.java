package de.cofinpro.metro.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import static java.util.function.Predicate.not;

/**
 * Dijkstra algorithm implementing class - specialized for this metro routing problem of finding the fastest route between
 * stations in the metro net. It uses a priority queue with station and travel time containing upwards nodes,
 * a set to keep track on processed stations and a map for fast retrieval of already set travelTimes for a visited station
 * (we could do without the map but then increasing runtime - because retrieving from the priority queue is much slower).
 */
public class DijkstraSearch {

    private static final int TRANSFER_TIME = 5;

    private final MetroNet lines;
    private final PriorityQueue<UpwardsTravelTimeNode> stationQueue
            = new PriorityQueue<>(Comparator.comparingInt(UpwardsTravelTimeNode::getTime));
    private final Set<Station> processed = new HashSet<>();
    private final Map<Station, Integer> travelTimes = new HashMap<>();
    private int totalTime;

    public DijkstraSearch(MetroNet lines) {
        this.lines = lines;
    }

    /**
     * core method to be called after creation of this search, that encapsulates all the dijkstra algorithm specifics.
     * Uses a priority queue with station and time containing upwards nodes, a set to keep track on processed
     * @param from departure station given
     * @param to destination station given
     * @return list of the stations for this route from departure to destination stations
     */
    public List<Station> getRoute(Station from, Station to) {
        stationQueue.offer(new UpwardsTravelTimeNode(from, null, 0));
        travelTimes.put(from, 0);
        while (!stationQueue.isEmpty()) {
            UpwardsTravelTimeNode stationNode = stationQueue.poll();
            if (to.equals(stationNode.getStation())) {
                return getRouteList(stationNode);
            }
            processUnprocessedNeighbors(stationNode);
            processed.add(stationNode.getStation());
        }
        throw new NoSuchElementException("Station to route not found!");
    }

    /**
     * helper method that processes all not yet processed (in this Dijkstra search) neighbors of a given station.
     * The algorithm processes all possible transfer stations first, the possible previous and next station in line. 
     * @param stationNode the UpwardsTimeNode to the station whose neighbors are processed
     */
    private void processUnprocessedNeighbors(UpwardsTravelTimeNode stationNode) {
        Station station = stationNode.getStation();
        station.getTransfer().stream()
                .map(transfer -> lines.findStation(transfer.getLine(), transfer.getStation()).orElseThrow())
                .filter(not(processed::contains)).forEach(nb -> dijkstraUpdateNeighbor(stationNode, nb, TRANSFER_TIME));

        lines.findPreviousInLine(station).stream().filter(not(processed::contains))
                .forEach(nb -> dijkstraUpdateNeighbor(stationNode, nb, nb.getTimeToNextStationInLine()));

        lines.findNextInLine(station).stream().filter(not(processed::contains))
                .forEach(nb -> dijkstraUpdateNeighbor(stationNode, nb, station.getTimeToNextStationInLine()));
    }

    /**
     * method to update a neighbor iff the time on this route is smaller than a possible previous time or none given yet.
     * In that case, the neighbor station gets a(n updated) travelTimes - map entry and a priority queue node is added.
     * @param stationNode the node of the station to search from
     * @param neighbor the neighbor station
     * @param travelTimeStationToNeighbor the travel time between station and neighbor
     */
    private void dijkstraUpdateNeighbor(UpwardsTravelTimeNode stationNode, Station neighbor, int travelTimeStationToNeighbor) {
        int timeOnThisRoute = travelTimes.get(stationNode.getStation()) + travelTimeStationToNeighbor;
        if (travelTimes.containsKey(neighbor) && travelTimes.get(neighbor) < timeOnThisRoute
                || neighbor.getName().equals("depot")) {
            return;
        }
        stationQueue.offer(new UpwardsTravelTimeNode(neighbor, stationNode, timeOnThisRoute));
        travelTimes.put(neighbor, timeOnThisRoute);
    }

    /**
     * after Dijkstra algorithm has found the destination station, this method is called and returns the fastest route
     * from departure to destination station. It uses the UpwardsNode structure, that is created during dijkstra to go back
     * from target via parent to the root. Also, the total time property is set for later retrieval by caller command.
     * @param stationNode the node of the target station found.
     * @return list with the stations route from departure to target.
     */
    private List<Station> getRouteList(UpwardsTravelTimeNode stationNode) {
        List<Station> routeList = new ArrayList<>();
        routeList.add(stationNode.getStation());
        totalTime = stationNode.getTime();
        while (stationNode.getParent() != null) {
            stationNode = stationNode.getParent();
            routeList.add(0, stationNode.getStation());
        }
        return routeList;
    }

    public int getTotalTime() {
        return totalTime;
    }
}

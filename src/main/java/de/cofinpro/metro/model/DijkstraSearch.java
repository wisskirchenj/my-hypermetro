package de.cofinpro.metro.model;

import java.util.*;

public class DijkstraSearch {

    private static final int TRANSFER_TIME = 5;

    private final MetroNet lines;
    private final PriorityQueue<UpwardsTimeWeightedNode> stationQueue;
    private final Set<Station> processed;
    private int totalTime;

    public DijkstraSearch(MetroNet lines) {
        this.lines = lines;
        processed = new HashSet<>();
        stationQueue = new PriorityQueue<>(Comparator.comparingInt(UpwardsTimeWeightedNode::getTimeWeight));
    }

    public List<Station> getRoute(Station from, Station to) {
        stationQueue.offer(new UpwardsTimeWeightedNode(from, null, 0));
        while (!stationQueue.isEmpty()) {
            UpwardsTimeWeightedNode stationNode = stationQueue.poll();
            if (to.equals(stationNode.getStation())) {
                return getRouteList(stationNode);
            }
            processNeighbors(stationNode.getStation());
            processed.add(stationNode.getStation());
        }
        throw new NoSuchElementException("Station to route not found!");
    }

    private void processNeighbors(Station station) {
        // TODO implement
    }

    /**
     * after Dijkstra algorithm has found the destination station, this method is called and returns the fastest route
     * from departure to destination station. It uses the UpwardsNode structure, that is created during BFS to go back
     * from target via parent to the root.
     * @param stationNode the node of the target station found.
     * @return list with the stations route from departure to target.
     */
    private List<Station> getRouteList(UpwardsTimeWeightedNode stationNode) {
        List<Station> routeList = new ArrayList<>();
        routeList.add(stationNode.getStation());
        totalTime = stationNode.getTimeWeight();
        while (stationNode.getParent() != null) {
            stationNode = stationNode.getParent();
            routeList.add(0, stationNode.getStation());
            totalTime += stationNode.getTimeWeight();
        }
        return routeList;
    }

    public int getTotalTime() {
        return totalTime;
    }
}

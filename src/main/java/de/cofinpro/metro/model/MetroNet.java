package de.cofinpro.metro.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * HashMap derived class that serves as the model of the Metro net data read in as Json.
 */
public class MetroNet extends HashMap<String, MetroLine> {

    private final NetType type;

    public MetroNet(NetType type) {
        this.type = type;
    }

    /**
     * finds the previous station in the metro line to the lowest index station with given name, if present.
     * @param station  station, whose previous is asked for
     * @return Optional result of the search
     */
    public List<Station> findPreviousInLine(Station station) {
        MetroLine line = get(station.getLine());
        int index = line.indexOf(station);
        List<Station> previousList = new ArrayList<>();

        if (type == NetType.CLASSICAL && index > 0) {
            previousList.add(line.get(index - 1));
        }
        station.getPreviousInLine().stream()
                .map(name -> findStation(station.getLine(), name)).filter(Optional::isPresent)
                .map(Optional::get).forEach(previousList::add);
        return previousList;
    }

    /**
     * finds the next station in the metro line to the station given, if present.
     * @param station station, whose next is asked for
     * @return Optional result of the search
     */
    public List<Station> findNextInLine(Station station) {
        MetroLine line = get(station.getLine());
        int index = line.indexOf(station);
        List<Station> nextList = new ArrayList<>();
        
        if (type == NetType.CLASSICAL && index < line.size() - 1) {
            nextList.add(line.get(index + 1));
        }
        station.getNextInLine().stream()
                .map(name -> findStation(station.getLine(), name)).filter(Optional::isPresent)
                .map(Optional::get).forEach(nextList::add);
        return nextList;
    }

    /**
     * find a station in the metro net with the given lineName and stationName, if exists
     * @param lineName line of station to search
     * @param stationName name of station to search
     * @return Optional result of the search
     */
    public Optional<Station> findStation(String lineName, String stationName) {
        return get(lineName) == null ? Optional.empty() : get(lineName).findStationByName(stationName);
    }

    public NetType getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other)
                && other instanceof MetroNet metroNet
                && this.type.equals(metroNet.type);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + type.hashCode();
    }
}

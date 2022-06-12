package de.cofinpro.metro.model;

import java.util.HashMap;
import java.util.Optional;

/**
 * HashMap derived class that serves as the model of the Metro net data read in as Json.
 */
public class MetroNet extends HashMap<String, MetroLine> {

    /**
     * finds the previous station in the metro line to the lowest index station with given name, if present.
     * @param station  station, whose previous is asked for
     * @return Optional result of the search
     */
    public Optional<Station> findPreviousStationInLine(Station station) {
        MetroLine line = get(station.getLine());
        int index = line.indexOf(station);
        return index > 0 ? Optional.of(line.get(index - 1)) : Optional.empty();
    }

    /**
     * finds the next station in the metro line to the station given, if present.
     * @param station station, whose next is asked for
     * @return Optional result of the search
     */
    public Optional<Station> findNextStationInLine(Station station) {
        MetroLine line = get(station.getLine());
        int index = line.indexOf(station);
        return index >= 0 && index < line.size() - 1 ? Optional.of(line.get(index + 1)) : Optional.empty();
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
}

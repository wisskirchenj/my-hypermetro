package de.cofinpro.metro.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * MetroLine class is a LinkedList<Station>. Has convenience methods for adding, removing stations by name.
 */
public class MetroLine extends LinkedList<Station> {

    public void addStationByName(int index, String stationName) {
        add(index, new Station(stationName));
    }

    public void removeStationByName(String stationName) {
        Iterator<Station> iterator = iterator();
        while (iterator.hasNext()) {
            if (stationName.equals(iterator.next().getName())) {
                iterator.remove();
                break;
            }
        }
    }

    public Optional<Station> findStationByName(String stationName) {
        for (Station current : this) {
            if (stationName.equals(current.getName())) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }
}

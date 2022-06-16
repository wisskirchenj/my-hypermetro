package de.cofinpro.metro.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * MetroLine class is a LinkedList<Station> that has the line name as additional field.
 * It offers convenience methods for finding, adding and removing stations by name.
 */
public class MetroLine extends LinkedList<Station> {

    private final String name;

    public MetroLine(String name) {
        super();
        this.name = name;
    }

    /**
     * adds the station at the given index into the list
     * @param index index where to add
     * @param stationName station to add
     */
    public void addStationByName(int index, String stationName, int timeToNextStation) {
        Station station = new Station(stationName);
        station.setLine(name);
        station.setTimeToNextStationInLine(timeToNextStation);
        add(index, station);
    }

    /**
     * removes the (first = lowest index) station given by the name from this MetroLine - does nothing, if station
     * is not found.
     * @param stationName station to be removed
     */
    public void removeStationByName(String stationName) {
        Iterator<Station> iterator = iterator();
        while (iterator.hasNext()) {
            if (stationName.equals(iterator.next().getName())) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * finds the lowest index station to the given name, if present.
     * @param stationName search key station name
     * @return Optional result of the search
     */
    public Optional<Station> findStationByName(String stationName) {
        for (Station current : this) {
            if (stationName.equals(current.getName())) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other)
                && other instanceof MetroLine metroLine
                && this.name.equals(metroLine.name);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + name.hashCode();
    }
}

package de.cofinpro.metro.io;

import de.cofinpro.metro.model.Station;
import de.cofinpro.metro.model.TransferStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationsPrinterTest {

    StationsPrinter stationsPrinter;

    @BeforeEach
    void setUp() {
        stationsPrinter = new StationsPrinter();
    }

    @Test
    void whenEmptyStations_OrOnlyDepotGiven_createOutputFromListReturnsEmpty() {
        List<Station> stations = List.of();
        assertEquals("", stationsPrinter.createOutputFromList(stations));
        stations = List.of(new Station("depot"), new Station("depot"));
        assertEquals("", stationsPrinter.createOutputFromList(stations));
    }

    @Test
    void whenStationListHas1ExtraStation_createOutputGives1CorrectLine() {
        List<Station> stations = List.of(new Station("depot"), new Station("station 1"),
                new Station("depot"));
        String output = stationsPrinter.createOutputFromList(stations);
        assertEquals("depot\nstation 1\ndepot\n", output);
    }

    @Test
    void whenStationListHas2ExtraStations_createOutputGives2CorrectLines() {
        List<Station> stations = new ArrayList<>();
        Station station = new Station("station");
        station.setTransfer(new TransferStation("Ligne", "other"));
        stations.add(new Station("depot"));
        stations.add(station);
        stations.add(new Station("depot"));
        String output = stationsPrinter.createOutputFromList(stations);
        assertEquals("depot\nstation - other (Ligne line)\ndepot\n", output);
    }

}
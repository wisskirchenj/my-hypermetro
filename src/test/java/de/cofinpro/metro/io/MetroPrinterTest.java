package de.cofinpro.metro.io;

import de.cofinpro.metro.model.Station;
import de.cofinpro.metro.model.TransferStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetroPrinterTest {

    MetroPrinter metroPrinter;

    @BeforeEach
    void setUp() {
        metroPrinter = new MetroPrinter();
    }

    @Test
    void whenEmptyStations_OrOnlyDepotGiven_createOutputFromListReturnsEmpty() {
        List<Station> stations = List.of();
        assertEquals("", metroPrinter.createLineInfo(stations));
        stations = List.of(new Station("depot"), new Station("depot"));
        assertEquals("", metroPrinter.createLineInfo(stations));
    }

    @Test
    void whenStationListHas1ExtraStation_createOutputGives1CorrectLine() {
        List<Station> stations = List.of(new Station("depot"), new Station("station 1"),
                new Station("depot"));
        String output = metroPrinter.createLineInfo(stations);
        assertEquals("depot\nstation 1\ndepot", output);
    }

    @Test
    void whenStationListHas2ExtraStations_createOutputGives2CorrectLines() {
        List<Station> stations = new ArrayList<>();
        Station station = new Station("station");
        station.addTransfer(new TransferStation("Ligne", "other"));
        stations.add(new Station("depot"));
        stations.add(station);
        stations.add(new Station("depot"));
        String output = metroPrinter.createLineInfo(stations);
        assertEquals("depot\nstation - other (Ligne line)\ndepot", output);
    }

}
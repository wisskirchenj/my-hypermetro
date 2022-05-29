package de.cofinpro.metro.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        List<String> stations = List.of();
        assertEquals("", stationsPrinter.createOutputFromList(stations));
        stations = List.of("depot", "depot");
        assertEquals("", stationsPrinter.createOutputFromList(stations));
    }

    @Test
    void whenStationListHas1ExtraStation_createOutputGives1CorrectLine() {
        List<String> stations = List.of("depot", "station 1", "depot");
        String output = stationsPrinter.createOutputFromList(stations);
        assertEquals("depot - station 1 - depot\n", output);
    }

    @Test
    void whenStationListHas2ExtraStations_createOutputGives2CorrectLines() {
        List<String> stations = List.of("depot", "station 1", "station 2", "depot");
        String output = stationsPrinter.createOutputFromList(stations);
        assertEquals("depot - station 1 - station 2\nstation 1 - station 2 - depot\n", output);
    }

    @Test
    void whenStationListHas3ExtraStations_createOutputGives3CorrectLines() {
        List<String> stations = List.of("depot", "station 1", "station 2", "station 3", "depot");
        String output = stationsPrinter.createOutputFromList(stations);
        assertEquals("depot - station 1 - station 2\nstation 1 - station 2 - station 3\nstation 2 - station 3 - depot\n", output);
    }
}
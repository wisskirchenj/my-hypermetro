package de.cofinpro.metro.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationsReaderTest {

    StationsReader stationsReader;

    @BeforeEach
    void setUp() {
        stationsReader = new StationsReader();
    }

    @Test
    void whenStationsFileGiven_readFileReadsListOfStations() throws IOException {
        String goodPath = "./src/test/resources/baltimore-stations.txt";
        List<String> stations = stationsReader.readFile(goodPath);
        assertNotNull(stations);
        assertFalse(stations.isEmpty());
        assertEquals(14, stations.size());
        assertEquals("Old Court", stations.get(1));
    }

    @Test
    void whenEmptyFileGiven_readFileReturnsEmptyList() throws IOException {
        String emptyPath = "./src/test/resources/empty.txt";
        List<String> stations = stationsReader.readFile(emptyPath);
        assertNotNull(stations);
        assertTrue(stations.isEmpty());
    }

    @Test
    void whenGoodPathToNonExistingFileGiven_readFileThrowsFileNotFound() {
        String nonExistingFilePath = "./src/test/resources/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> stationsReader.readFile(nonExistingFilePath));
    }

    @Test
    void whenNonExistingDirectoryPathGiven_readFileThrowsFileNotFound() {
        String nonExistingDirPath = "./src/test/notexists/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> stationsReader.readFile(nonExistingDirPath));
    }
}
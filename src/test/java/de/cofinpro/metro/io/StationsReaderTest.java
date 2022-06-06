package de.cofinpro.metro.io;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.model.MetroLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StationsReaderTest {

    StationsReader stationsReader;

    @BeforeEach
    void setUp() {
        stationsReader = new StationsReader();
    }

    @Test
    void whenGoodPathToNonExistingFileGiven_readFileThrowsFileNotFound() {
        String nonExistingFilePath = "./src/test/resources/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> stationsReader.readJsonFile(nonExistingFilePath));
    }

    @Test
    void whenNonExistingDirectoryPathGiven_readJsonFileThrowsFileNotFound() {
        String nonExistingDirPath = "./src/test/notexists/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> stationsReader.readJsonFile(nonExistingDirPath));
    }

    @Test
    void whenInvalidJsonGiven_readJsonFileThrowsJsonParse() {
        String invalidPath = "./src/test/resources/standard-invalid.json";
        assertThrows(JsonParseException.class, () -> stationsReader.readJsonFile(invalidPath));
    }

    @ParameterizedTest
    @ValueSource(strings = {"./src/test/resources/standard-wrong.json",
            "./src/test/resources/standard-wrong2.json"})
    void whenValidJsonNotFormatGiven_readJsonFileThrowsIllegalState(String path) {
        assertThrows(IllegalStateException.class, () -> stationsReader.readJsonFile(path));
    }

    @Test
    void whenJsonStationValueIsObject_readJsonFileThrowsUnsupportedOperation() {
        String invalidPath = "./src/test/resources/standard-wrong3.json";
        assertThrows(UnsupportedOperationException.class, () -> stationsReader.readJsonFile(invalidPath));
    }

    @Test
    void whenJsonStationKeyIsNaN_readJsonFileThrowsNumberFormat() {
        String invalidPath = "./src/test/resources/standard-nan.json";
        assertThrows(NumberFormatException.class, () -> stationsReader.readJsonFile(invalidPath));
    }

    @Test
    void whenStandardJsonGiven_readJsonFileReadsLines() throws IOException {
        String standardJsonPath = "./src/test/resources/standard-lines.json";
        Map<String, MetroLine> lines = stationsReader.readJsonFile(standardJsonPath);
        System.out.println(lines);
        assertEquals(2, lines.size());
        assertTrue(lines.containsKey("line 1"));
        assertEquals(3, lines.get("line 1").size());
        assertEquals("station 2", lines.get("line 1").get(1));
        assertEquals("station 2", lines.get("line 2").get(1));
    }
}
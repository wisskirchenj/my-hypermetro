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

class MetroReaderTest {

    MetroReader metroReader;

    @BeforeEach
    void setUp() {
        metroReader = new MetroReader();
    }

    @Test
    void whenGoodPathToNonExistingFileGiven_readFileThrowsFileNotFound() {
        String nonExistingFilePath = "./src/test/resources/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> metroReader.readJsonFile(nonExistingFilePath));
    }

    @Test
    void whenNonExistingDirectoryPathGiven_readJsonFileThrowsFileNotFound() {
        String nonExistingDirPath = "./src/test/notexists/not-exists.txt";
        assertThrows(FileNotFoundException.class, () -> metroReader.readJsonFile(nonExistingDirPath));
    }

    @Test
    void whenInvalidJsonGiven_readJsonFileThrowsJsonParse() {
        String invalidPath = "./src/test/resources/standard-invalid.json";
        assertThrows(JsonParseException.class, () -> metroReader.readJsonFile(invalidPath));
    }

    @ParameterizedTest
    @ValueSource(strings = {"./src/test/resources/standard-wrong.json",
            "./src/test/resources/standard-wrong2.json"})
    void whenValidJsonNotFormatGiven_readJsonFileThrowsIllegalState(String path) {
        assertThrows(IllegalStateException.class, () -> metroReader.readJsonFile(path));
    }

    @Test
    void whenJsonStationKeyIsNaN_readJsonFileThrowsNumberFormat() {
        String invalidPath = "./src/test/resources/standard-nan.json";
        assertThrows(NumberFormatException.class, () -> metroReader.readJsonFile(invalidPath));
    }

    @Test
    void whenStandardJsonGiven_readJsonFileReadsLines() throws IOException {
        String standardJsonPath = "./src/test/resources/standard-lines.json";
        Map<String, MetroLine> lines = metroReader.readJsonFile(standardJsonPath);
        System.out.println(lines);
        assertEquals(2, lines.size());
        assertTrue(lines.containsKey("line 1"));
        assertEquals(3, lines.get("line 1").size());
        assertEquals("station 2", lines.get("line 1").get(1).getName());
        assertEquals("station 2", lines.get("line 2").get(1).getName());
    }
}
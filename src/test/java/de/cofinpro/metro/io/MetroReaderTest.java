package de.cofinpro.metro.io;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
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
    void whenValidJsonNotFormatGiven_readJsonFileThrowsRuntime(String path) {
        assertThrows(RuntimeException.class, () -> metroReader.readJsonFile(path));
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

    @Test
    void whenJsonWithTimeGiven_readJsonFileReadsLinesWithTime() throws IOException {
        String standardJsonPath = "./src/test/resources/test-w-time.json";
        Map<String, MetroLine> lines = metroReader.readJsonFile(standardJsonPath);
        System.out.println(lines);
        assertEquals(2, lines.size());
        assertTrue(lines.containsKey("Hammersmith-and-City"));
        assertEquals(3, lines.get("Hammersmith-and-City").size());
        assertEquals("Metro-Railway", lines.get("Metro-Railway").get(1).getLine());
        assertEquals("Edgver road", lines.get("Metro-Railway").get(1).getName());
        assertNotNull(lines.get("Metro-Railway").get(2).getTransfer());
        assertEquals(1, lines.get("Metro-Railway").get(2).getTransfer().size());
        assertEquals(1, lines.get("Metro-Railway").get(2).getTimeToNextStationInLine());
    }

    @Test
    void whenLondonJson_readExtendedJsonReadsLines() throws IOException {
        String standardJsonPath = "./src/test/resources/london.json";
        MetroNet lines = metroReader.readJsonFile(standardJsonPath);
        System.out.println(lines);
        assertEquals(14, lines.size());
    }
}
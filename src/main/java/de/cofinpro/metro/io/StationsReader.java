package de.cofinpro.metro.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class for reading file input on subway stations.
 */
public class StationsReader {

    /**
     * read input of the given file path linewise into a string
     * @param stationsFile path string to the input file
     * @return line-wise list of file contents
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException may be thrown by readAllLines
     */
    public List<String> readFile(String stationsFile) throws IOException {
        Path path = Path.of(stationsFile);
        if (Files.notExists(path)) {
            throw new FileNotFoundException(stationsFile + "does not exist.");
        }
        return Files.readAllLines(path);
    }
}

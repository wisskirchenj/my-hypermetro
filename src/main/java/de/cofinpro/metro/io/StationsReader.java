package de.cofinpro.metro.io;

import com.google.gson.*;
import de.cofinpro.metro.model.MetroLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Class for reading .json file input on subway stations. Uses GSON.
 */
public class StationsReader {

    /**
     * read input of the given json file path into a list of MetroLines using Google's GSON lib.
     * @param jsonPath path string to the input .json file
     * @return list of the deserialized Metro lines
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException may be thrown by readAllLines
     * @throws JsonParseException if the given json file cannot be parsed
     * @throws NumberFormatException passed on from parseMetroLineJson-method
     * @throws UnsupportedOperationException passed on from parseMetroLineJson-method
     */
    public Map<String, MetroLine> readJsonFile(String jsonPath) throws JsonParseException, NumberFormatException,
            IllegalStateException, UnsupportedOperationException, IOException {
        Path path = Path.of(jsonPath);
        if (Files.notExists(path)) {
            throw new FileNotFoundException(jsonPath + "does not exist.");
        }
        Map<String, MetroLine> lines = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(path)) {
            JsonElement tree = JsonParser.parseReader(reader);
            tree.getAsJsonObject().entrySet()
                    .forEach(entry -> lines.put(entry.getKey(), parseMetroLineJson(entry.getValue())));
        }
        return lines;
    }

    /**
     * parse the json object-string for one line into a MetroLine instance, which is created in this method.
     * @param lineJson the json line object.
     * @return the parse result as MetroLine object
     * @throws NumberFormatException if a key in the json-object is not an integer
     * @throws IllegalStateException if the (main) value is not a JsonObject
     * @throws UnsupportedOperationException  if one of the object-properties values is not a string
     */
    private MetroLine parseMetroLineJson(JsonElement lineJson)
            throws NumberFormatException, IllegalStateException, UnsupportedOperationException {
        MetroLine line = new MetroLine();
        lineJson.getAsJsonObject().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .forEach(stationEntry -> line.addLast(stationEntry.getValue().getAsString()));
        return line;
    }
}

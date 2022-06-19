package de.cofinpro.metro.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.NetType;
import de.cofinpro.metro.model.Station;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Class for reading .json file input on subway stations. Uses GSON.
 */
public class MetroReader {

    private static final Gson GSON = new Gson();

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
    public MetroNet readJsonFile(String jsonPath) throws JsonParseException, NumberFormatException,
            IllegalStateException, UnsupportedOperationException, IOException {
        Path path = Path.of(jsonPath);
        if (Files.notExists(path)) {
            throw new FileNotFoundException(jsonPath + "does not exist.");
        }
        MetroNet lines;
        try (Reader reader = Files.newBufferedReader(path)) {
            lines = parseMetroNetFrom(reader);
        }
        return lines;
    }

    /**
     * method is capable of reading two different json formats: the old-style has a line object with properties
     * station number and station data, the new-style has stations as Json array of objects.
     * @return the parsed Metronet
     */
    private MetroNet parseMetroNetFrom(Reader reader) throws NumberFormatException, IllegalStateException,
            UnsupportedOperationException {
        MetroNet lines = null;
        JsonElement tree = JsonParser.parseReader(reader);
        for (Map.Entry<String, JsonElement> line : tree.getAsJsonObject().entrySet()) {
            if (lines == null) {
                lines = new MetroNet(line.getValue().isJsonArray() ? NetType.EXTENDED : NetType.CLASSICAL);
            }
            lines.put(line.getKey(), lines.getType() == NetType.EXTENDED
                    ? parseMetroLineJsonArray(line.getValue().getAsJsonArray(), line.getKey())
                    : parseMetroLineJson(line.getValue(), line.getKey()));
        }
        return lines;
    }

    /**
     * parse the json object-string for one line (of the new format of stage 6 where a line is a Json array)
     * into a MetroLine instance, which is created in this method.
     * @param stations the json array object of station JsonElements.
     * @return the parse result as MetroLine object
     * @throws IllegalStateException if the (main) value is not a JsonObject
     * @throws UnsupportedOperationException  if one of the object-properties values is not a string
     */
    private MetroLine parseMetroLineJsonArray(JsonArray stations, String lineName) throws IllegalStateException,
            UnsupportedOperationException {
        MetroLine line = new MetroLine(lineName);
        stations.forEach(stationJson -> {
            Station station = GSON.fromJson(stationJson, Station.class);
            station.setLine(lineName);
            line.addLast(station);
        });
        return line;
    }

    /**
     * parse the json object-string for one line (of the old format until stage 5 including) into a MetroLine instance,
     * which is created in this method.
     * @param lineJson the json line object.
     * @return the parse result as MetroLine object
     * @throws NumberFormatException if a key in the json-object is not an integer
     * @throws IllegalStateException if the (main) value is not a JsonObject
     * @throws UnsupportedOperationException  if one of the object-properties values is not a string
     */
    private MetroLine parseMetroLineJson(JsonElement lineJson, String lineName)
            throws NumberFormatException, IllegalStateException, UnsupportedOperationException {
        MetroLine line = new MetroLine(lineName);
        lineJson.getAsJsonObject().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .forEach(stationEntry -> {
                    Station station = GSON.fromJson(stationEntry.getValue(), Station.class);
                    station.setLine(lineName);
                    station.setNextInLine(new ArrayList<>());
                    station.setPreviousInLine(new ArrayList<>());
                    line.addLast(station);
                });
        return line;
    }
}

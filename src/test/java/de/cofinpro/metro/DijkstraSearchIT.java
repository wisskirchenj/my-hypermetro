package de.cofinpro.metro;

import de.cofinpro.metro.io.MetroReader;
import de.cofinpro.metro.model.BreadthFirstSearch;
import de.cofinpro.metro.model.DijkstraSearch;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraSearchIT {

    static final String JSON_PATH = "./src/test/resources/dijkstra_test.json";
    static final String OTHER_JSON_PATH = "./src/test/resources/dijkstra_test2.json";

    MetroReader metroReader;
    MetroNet lines;

    @BeforeEach
    void setup() throws IOException {
        metroReader = new MetroReader();
        lines = metroReader.readJsonFile(JSON_PATH);
    }

    @Test
    void whenJsonFastestRoute_ThenFastestRouteIs26OverI() {
        DijkstraSearch dijkstraSearch = new DijkstraSearch(lines);
        assertTrue(lines.findStation("L1", "A").isPresent());
        assertTrue(lines.findStation("L2", "G").isPresent());
        List<Station> route = dijkstraSearch.getRoute(lines.findStation("L1", "A").get(),
                lines.findStation("L2", "G").get());
        assertEquals(7, route.size());
        assertEquals("A", route.get(0).getName());
        assertEquals("E", route.get(1).getName());
        assertEquals("F", route.get(2).getName());
        assertEquals("B", route.get(3).getName());
        assertEquals("I", route.get(4).getName());
        assertEquals("D", route.get(5).getName());
        assertEquals("G", route.get(6).getName());
        assertEquals(26, dijkstraSearch.getTotalTime());
    }

    @Test
    void whenJsonModified_ThenFastestRouteIs25NotOverI() throws IOException {
        lines = metroReader.readJsonFile(OTHER_JSON_PATH);
        DijkstraSearch dijkstraSearch = new DijkstraSearch(lines);
        assertTrue(lines.findStation("L1", "A").isPresent());
        assertTrue(lines.findStation("L2", "G").isPresent());
        List<Station> route = dijkstraSearch.getRoute(lines.findStation("L1", "A").get(),
                lines.findStation("L2", "G").get());
        assertEquals(4, route.size());
        assertEquals("A", route.get(0).getName());
        assertEquals("E", route.get(1).getName());
        assertEquals("F", route.get(2).getName());
        assertEquals("G", route.get(3).getName());
        assertEquals(25, dijkstraSearch.getTotalTime());
    }

    @Test
    void whenJsonFastestRoute_ThenShortestRouteIsOverE() {
        BreadthFirstSearch bFSearch = new BreadthFirstSearch(lines);
        assertTrue(lines.findStation("L1", "A").isPresent());
        assertTrue(lines.findStation("L2", "G").isPresent());
        List<Station> route = bFSearch.getRoute(lines.findStation("L1", "A").get(),
                lines.findStation("L2", "G").get());
        assertEquals(4, route.size());
        assertEquals("A", route.get(0).getName());
        assertEquals("E", route.get(1).getName());
        assertEquals("F", route.get(2).getName());
        assertEquals("G", route.get(3).getName());
    }
}

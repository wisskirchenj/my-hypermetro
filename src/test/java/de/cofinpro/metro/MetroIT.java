package de.cofinpro.metro;

import de.cofinpro.metro.controller.CommandLineInterpreter;
import de.cofinpro.metro.controller.MetroController;
import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.io.MetroReader;
import de.cofinpro.metro.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetroIT {

    private static final String JSON_PATH = "./src/test/resources/test.json";

    @Mock
    Scanner scanner;

    @Mock
    MetroPrinter metroPrinter;

    @Captor
    ArgumentCaptor<List<Station>> printCaptor;

    MetroController controller;

    @BeforeEach
    void setup() {
        controller = new MetroController(new MetroReader(), metroPrinter, new CommandLineInterpreter(scanner));
    }

    @Test
    void example1Stage4() {
        when(scanner.nextLine()).thenReturn("/route Metro-Railway \"Edgver road\" Hammersmith-and-City Westbourne-park",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printRoute(printCaptor.capture());
        assertEquals(4, printCaptor.getValue().size());
        assertEquals("Edgver road", printCaptor.getValue().get(0).getName());
        assertEquals("Baker street", printCaptor.getValue().get(1).getName());
        assertEquals("Baker street", printCaptor.getValue().get(2).getName());
        assertEquals("Hammersmith-and-City", printCaptor.getValue().get(2).getLine());
        assertEquals("Westbourne-park", printCaptor.getValue().get(3).getName());
    }

    @Test
    void example2Stage4() {
        when(scanner.nextLine()).thenReturn("/append Hammersmith-and-City \"Test station\"",
                "/output Hammersmith-and-City",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printLine(printCaptor.capture());
        assertEquals(6, printCaptor.getValue().size());
        assertEquals("depot", printCaptor.getValue().get(0).getName());
        assertEquals("Baker street", printCaptor.getValue().get(3).getName());
        assertEquals("Baker street", printCaptor.getValue().get(3).getTransfer().get(0).getStation());
        assertEquals("Metro-Railway", printCaptor.getValue().get(3).getTransfer().get(0).getLine());
        assertEquals("Test station", printCaptor.getValue().get(4).getName());
        assertEquals("depot", printCaptor.getValue().get(5).getName());
    }


    @Test
    void example3Stage4() {
        when(scanner.nextLine()).thenReturn("/remove Hammersmith-and-City Hammersmith",
                "/output Hammersmith-and-City",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printLine(printCaptor.capture());
        assertEquals(4, printCaptor.getValue().size());
        assertEquals("depot", printCaptor.getValue().get(0).getName());
        assertEquals("Westbourne-park", printCaptor.getValue().get(1).getName());
        assertEquals("Baker street", printCaptor.getValue().get(2).getName());
        assertEquals("Baker street", printCaptor.getValue().get(2).getTransfer().get(0).getStation());
        assertEquals("Metro-Railway", printCaptor.getValue().get(2).getTransfer().get(0).getLine());
        assertEquals("depot", printCaptor.getValue().get(3).getName());
    }
}

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

    private static final String JSON_PATH = "./src/test/resources/test-w-time.json";

    @Mock
    Scanner scanner;

    @Mock
    MetroPrinter metroPrinter;

    @Captor
    ArgumentCaptor<List<Station>> printListCaptor;

    @Captor
    ArgumentCaptor<Integer> printTextCaptor;

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
        verify(metroPrinter).printRoute(printListCaptor.capture());
        assertEquals(4, printListCaptor.getValue().size());
        assertEquals("Edgver road", printListCaptor.getValue().get(0).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(1).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(2).getName());
        assertEquals("Hammersmith-and-City", printListCaptor.getValue().get(2).getLine());
        assertEquals("Westbourne-park", printListCaptor.getValue().get(3).getName());
    }

    @Test
    void example2Stage4() {
        when(scanner.nextLine()).thenReturn("/append Hammersmith-and-City \"Test station\" 3",
                "/output Hammersmith-and-City",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printLine(printListCaptor.capture());
        assertEquals(6, printListCaptor.getValue().size());
        assertEquals("depot", printListCaptor.getValue().get(0).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(3).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(3).getTransfer().get(0).getStation());
        assertEquals("Metro-Railway", printListCaptor.getValue().get(3).getTransfer().get(0).getLine());
        assertEquals("Test station", printListCaptor.getValue().get(4).getName());
        assertEquals("depot", printListCaptor.getValue().get(5).getName());
    }


    @Test
    void example3Stage4() {
        when(scanner.nextLine()).thenReturn("/remove Hammersmith-and-City Hammersmith",
                "/output Hammersmith-and-City",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printLine(printListCaptor.capture());
        assertEquals(4, printListCaptor.getValue().size());
        assertEquals("depot", printListCaptor.getValue().get(0).getName());
        assertEquals("Westbourne-park", printListCaptor.getValue().get(1).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(2).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(2).getTransfer().get(0).getStation());
        assertEquals("Metro-Railway", printListCaptor.getValue().get(2).getTransfer().get(0).getLine());
        assertEquals("depot", printListCaptor.getValue().get(3).getName());
    }

    @Test
    void example1Stage5() {
        when(scanner.nextLine()).thenReturn("/fastest-route Hammersmith-and-City \"Baker street\" Hammersmith-and-City Hammersmith",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printRoute(printListCaptor.capture());
        verify(metroPrinter).printTotalTime(printTextCaptor.capture());
        assertEquals(3, printListCaptor.getValue().size());
        assertEquals("Baker street", printListCaptor.getValue().get(0).getName());
        assertEquals("Westbourne-park", printListCaptor.getValue().get(1).getName());
        assertEquals("Hammersmith-and-City", printListCaptor.getValue().get(2).getLine());
        assertEquals(4, printTextCaptor.getValue());
    }

    @Test
    void example2And3Stage5() {
        when(scanner.nextLine()).thenReturn("/add Hammersmith-and-City New-Station 4",
                "/remove Hammersmith-and-City Hammersmith",
                "/output Hammersmith-and-City",
                "/exit");
        controller.run(JSON_PATH);
        verify(metroPrinter).printLine(printListCaptor.capture());
        System.out.println(printListCaptor.getValue());
        assertEquals(5, printListCaptor.getValue().size());
        assertEquals("Westbourne-park", printListCaptor.getValue().get(1).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(2).getName());
        assertEquals("Baker street", printListCaptor.getValue().get(2).getTransfer().get(0).getStation());
        assertEquals("depot", printListCaptor.getValue().get(4).getName());
        assertEquals(4, printListCaptor.getValue().get(3).getTimeToNextStationInLine());
    }
}

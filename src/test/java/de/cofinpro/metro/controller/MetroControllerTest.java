package de.cofinpro.metro.controller;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetroControllerTest {

    @Mock
    StationsReader stationsReader;

    @Mock
    StationsPrinter stationsPrinter;

    @Captor
    ArgumentCaptor<String> printerErrorCaptor;

    @Captor
    ArgumentCaptor<List<String>> printerListCaptor;

    MetroController metroController;

    @BeforeEach
    void setUp() {
        metroController = new MetroController(stationsReader, stationsPrinter);
    }

    @Test
    void whenFNFException_runCallsPrintCorrectErrormessageAndNoOtherOutput() throws IOException {
        when(stationsReader.readFile(anyString())).thenThrow(new FileNotFoundException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Error! Such a file doesn't exist!", printerErrorCaptor.getValue());
        verify(stationsPrinter, times(0)).print(anyList());
    }

    @Test
    void whenIOException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(stationsReader.readFile(anyString())).thenThrow(new IOException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertTrue(printerErrorCaptor.getValue().contains("IOException"));
    }

    @Test
    void whenEmptyListRead_runAddsDepotTailAndEnd() throws IOException {
        when(stationsReader.readFile(anyString())).thenReturn(new ArrayList<>());
        metroController.run("mockPath");
        verify(stationsPrinter, times(0)).printError(anyString());
        verify(stationsPrinter).print(printerListCaptor.capture());
        assertEquals(2, printerListCaptor.getValue().size());
        assertEquals("depot", printerListCaptor.getValue().get(0));
        assertEquals("depot", printerListCaptor.getValue().get(1));
    }

    @Test
    void when2ItemListRead_runProduces4ItemList() throws IOException {
        when(stationsReader.readFile(anyString())).thenReturn(new ArrayList<>(List.of("station 1","station 2")));
        metroController.run("mockPath");
        verify(stationsPrinter, times(0)).printError(anyString());
        verify(stationsPrinter).print(printerListCaptor.capture());
        assertEquals(4, printerListCaptor.getValue().size());
        assertEquals("depot", printerListCaptor.getValue().get(0));
        assertEquals("station 2", printerListCaptor.getValue().get(2));
        assertEquals("depot", printerListCaptor.getValue().get(3));
    }
}
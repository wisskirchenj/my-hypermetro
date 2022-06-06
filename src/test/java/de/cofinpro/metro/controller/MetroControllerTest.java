package de.cofinpro.metro.controller;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    @Mock
    CommandLineInterpreter commandLineInterpreter;
    @Captor
    ArgumentCaptor<String> printerErrorCaptor;

    @Captor
    ArgumentCaptor<List<String>> printerListCaptor;

    MetroController metroController;

    @BeforeEach
    void setUp() {
        metroController = new MetroController(stationsReader, stationsPrinter, commandLineInterpreter);
    }

    @Test
    void whenFNFException_runCallsPrintCorrectErrormessageAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new FileNotFoundException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Error! Such a file doesn't exist!", printerErrorCaptor.getValue());
        verify(stationsPrinter, times(0)).print(anyList());
    }

    @Test
    void whenIOException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new IOException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertTrue(printerErrorCaptor.getValue().contains("IOException"));
    }

    @Test
    void whenPossibleRuntimeException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new JsonParseException(""));
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Incorrect file", printerErrorCaptor.getValue());
        verify(stationsPrinter, times(0)).print(anyList());
    }
}
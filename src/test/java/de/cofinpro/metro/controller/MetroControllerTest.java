package de.cofinpro.metro.controller;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.controller.command.ExitCommand;
import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.io.StationsReader;
import de.cofinpro.metro.model.MetroLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    MetroController metroController;

    Map<String, MetroLine> lines;

    @BeforeEach
    void setUp() {
        metroController = new MetroController(stationsReader, stationsPrinter, commandLineInterpreter);
        lines = new HashMap<>();
        lines.put("line", new MetroLine());
    }

    @Test
    void whenExitCommand_runExitsNoOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenReturn(lines);
        when(commandLineInterpreter.parseNext()).thenReturn(new ExitCommand());
        metroController.run("mockPath");
        verify(stationsPrinter, times(0)).print(anyList());
        verify(stationsPrinter, times(0)).printError(anyString());
        verify(commandLineInterpreter).parseNext();
    }

    @Test
    void whenFNFException_runCallsPrintCorrectErrormessageAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new FileNotFoundException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Error! Such a file doesn't exist!", printerErrorCaptor.getValue());
        verify(stationsPrinter, times(0)).print(anyList());
        verify(commandLineInterpreter, times(0)).parseNext();
    }

    @Test
    void whenIOException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new IOException());
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertTrue(printerErrorCaptor.getValue().contains("IOException"));
        verify(commandLineInterpreter, times(0)).parseNext();
    }

    @Test
    void whenPossibleRuntimeException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(stationsReader.readJsonFile(anyString())).thenThrow(new JsonParseException(""));
        metroController.run("mockPath");
        verify(stationsPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Incorrect file", printerErrorCaptor.getValue());
        verify(stationsPrinter, times(0)).print(anyList());
        verify(commandLineInterpreter, times(0)).parseNext();
    }
}
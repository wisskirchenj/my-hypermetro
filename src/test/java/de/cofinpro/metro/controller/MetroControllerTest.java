package de.cofinpro.metro.controller;

import com.google.gson.JsonParseException;
import de.cofinpro.metro.controller.command.ExitCommand;
import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.io.MetroReader;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetroControllerTest {

    @Mock
    MetroReader metroReader;
    @Mock
    MetroPrinter metroPrinter;
    @Mock
    CommandLineInterpreter commandLineInterpreter;
    @Captor
    ArgumentCaptor<String> printerErrorCaptor;

    MetroController metroController;

    MetroNet lines;

    @BeforeEach
    void setUp() {
        metroController = new MetroController(metroReader, metroPrinter, commandLineInterpreter);
        lines = new MetroNet();
        lines.put("line", new MetroLine("line"));
    }

    @Test
    void whenExitCommand_runExitsNoOutput() throws IOException {
        when(metroReader.readJsonFile(anyString())).thenReturn(lines);
        when(commandLineInterpreter.parseNext()).thenReturn(new ExitCommand());
        metroController.run("mockPath");
        verify(metroPrinter, times(0)).printLine(anyList());
        verify(metroPrinter, times(0)).printError(anyString());
        verify(commandLineInterpreter).parseNext();
    }

    @Test
    void whenFNFException_runCallsPrintCorrectErrormessageAndNoOtherOutput() throws IOException {
        when(metroReader.readJsonFile(anyString())).thenThrow(new FileNotFoundException());
        metroController.run("mockPath");
        verify(metroPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Error! Such a file doesn't exist!", printerErrorCaptor.getValue());
        verify(metroPrinter, times(0)).printLine(anyList());
        verify(commandLineInterpreter, times(0)).parseNext();
    }

    @Test
    void whenIOException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(metroReader.readJsonFile(anyString())).thenThrow(new IOException());
        metroController.run("mockPath");
        verify(metroPrinter).printError(printerErrorCaptor.capture());
        assertTrue(printerErrorCaptor.getValue().contains("IOException"));
        verify(commandLineInterpreter, times(0)).parseNext();
    }

    @Test
    void whenPossibleRuntimeException_runCallsPrintErrorAndNoOtherOutput() throws IOException {
        when(metroReader.readJsonFile(anyString())).thenThrow(new JsonParseException(""));
        metroController.run("mockPath");
        verify(metroPrinter).printError(printerErrorCaptor.capture());
        assertEquals("Incorrect file", printerErrorCaptor.getValue());
        verify(metroPrinter, times(0)).printLine(anyList());
        verify(commandLineInterpreter, times(0)).parseNext();
    }
}
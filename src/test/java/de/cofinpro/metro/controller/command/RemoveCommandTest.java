package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveCommandTest {

    private static final String LINE_NAME = "line 1";
    @Mock
    StationsPrinter printer;

    RemoveCommand removeCommand;

    Map<String, MetroLine> lines;

    @BeforeEach
    void setup() {
        lines = new HashMap<>();
        MetroLine line = new MetroLine();
        line.addStationByName(0,"depot");
        line.addStationByName(1,"station");
        line.addStationByName(2,"other station");
        line.addStationByName(3,"depot");
        lines.put(LINE_NAME, line);
    }

    @Test
    void whenRemoveValidLineAndStation_executeRemoves() {
        removeCommand = new RemoveCommand(printer, LINE_NAME, "other station");
        removeCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(3, lines.get(LINE_NAME).size());
        assertEquals("station", lines.get(LINE_NAME).get(1).getName());
        assertEquals("depot", lines.get(LINE_NAME).get(2).getName());
    }

    @Test
    void whenRemoveValidLineInvalidStation_executeDoesNothing() {
        removeCommand = new RemoveCommand(printer, LINE_NAME, "not there");
        removeCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        assertEquals("station", lines.get(LINE_NAME).get(1).getName());
        assertEquals("depot", lines.get(LINE_NAME).get(3).getName());
    }


    @Test
    void whenRemoveInvalidLine_executePrintsInvalidCommand() {
        removeCommand = new RemoveCommand(printer, "wrong", "first station");
        removeCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError("Invalid Command");
    }

    @Test
    void getType() {
        removeCommand = new RemoveCommand(printer, "any", "any");
        assertEquals(CommandType.REMOVE, removeCommand.getType());
    }
}
package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsertCommandTest {

    private static final String LINE_NAME = "line 1";
    @Mock
    StationsPrinter printer;

    @Captor
    ArgumentCaptor<String> printCaptor;

    InsertCommand insertCommand;

    Map<String, MetroLine> lines;

    @BeforeEach
    void setup() {
        lines = new HashMap<>();
        MetroLine line = new MetroLine();
        line.addAll(List.of("depot", "station", "other station", "depot"));
        lines.put(LINE_NAME, line);
    }

    @Test
    void whenAddHeadValidLine_executeAddsLineHead() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(5, lines.get(LINE_NAME).size());
        assertEquals("first station", lines.get(LINE_NAME).get(1));
    }

    @Test
    void whenAddHeadInvalidLine_executePrintsInvalidCommand() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, "wrong", "first station");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError(printCaptor.capture());
        assertEquals("Invalid Command", printCaptor.getValue());
    }

    @Test
    void whenAppendValidLine_executeAddsLineHead() {
        insertCommand = new InsertCommand(CommandType.APPEND, printer, LINE_NAME, "first station");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(5, lines.get(LINE_NAME).size());
        assertEquals("first station", lines.get(LINE_NAME).get(3));
    }

    @Test
    void whenAppendInvalidLine_executePrintsInvalidCommand() {
        insertCommand = new InsertCommand(CommandType.APPEND, printer, "wrong", "first station");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError(printCaptor.capture());
        assertEquals("Invalid Command", printCaptor.getValue());
    }
    @Test
    void getType() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station");
        assertEquals(CommandType.ADD_HEAD, insertCommand.getType());
        insertCommand = new InsertCommand(CommandType.APPEND, printer, LINE_NAME, "first station");
        assertEquals(CommandType.APPEND, insertCommand.getType());
    }
}
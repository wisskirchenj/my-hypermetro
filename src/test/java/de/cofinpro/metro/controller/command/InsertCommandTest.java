package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.NetType;
import de.cofinpro.metro.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsertCommandTest {

    private static final String LINE_NAME = "line 1";
    @Mock
    MetroPrinter printer;

    @Captor
    ArgumentCaptor<String> printCaptor;

    InsertCommand insertCommand;

    MetroNet lines;

    @BeforeEach
    void setup() {
        lines = new MetroNet(NetType.CLASSICAL);
        MetroLine line = new MetroLine(LINE_NAME);
        line.addStationByName(0,"depot", 1);
        line.addStationByName(1,"station", 2);
        line.addStationByName(2,"other station", 3);
        line.addStationByName(3,"depot", 0);
        lines.put(LINE_NAME, line);
    }

    @Test
    void whenAddHeadValidLine_executeAddsLineHead() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station", "4");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(5, lines.get(LINE_NAME).size());
        assertEquals("first station", lines.get(LINE_NAME).get(1).getName());
        assertEquals(4, lines.get(LINE_NAME).get(1).getTimeToNextStationInLine());
    }

    @Test
    void whenAddHeadValid_executeCreatesPrevNext() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station", "4");
        insertCommand.execute(lines);
        assertEquals("first station", lines.get(LINE_NAME).get(1).getName());
        assertEquals(1, lines.get(LINE_NAME).get(1).getNextInLine().size());
        assertEquals("station", lines.get(LINE_NAME).get(1).getNextInLine().get(0));
        assertEquals("station", lines.get(LINE_NAME).get(2).getName());
        assertEquals(1, lines.get(LINE_NAME).get(2).getPreviousInLine().size());
        assertEquals("first station", lines.get(LINE_NAME).get(2).getPreviousInLine().get(0));
    }

    @Test
    void whenAddHeadExtended_executeCreatesPrevNext() {
        lines = new MetroNet(NetType.EXTENDED);
        MetroLine line = new MetroLine(LINE_NAME);
        line.addStationByName(0,"station", 2);
        line.addStationByName(1,"other station", 3);
        lines.put(LINE_NAME, line);
        assertEquals(2, lines.get(LINE_NAME).size());
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station", "4");
        insertCommand.execute(lines);
        assertEquals("first station", lines.get(LINE_NAME).get(0).getName());
        assertEquals(1, lines.get(LINE_NAME).get(0).getNextInLine().size());
        assertEquals("station", lines.get(LINE_NAME).get(0).getNextInLine().get(0));
        assertEquals("station", lines.get(LINE_NAME).get(1).getName());
        assertEquals(1, lines.get(LINE_NAME).get(1).getPreviousInLine().size());
        assertEquals("first station", lines.get(LINE_NAME).get(1).getPreviousInLine().get(0));
    }

    @Test
    void whenAddHeadInvalidLine_executePrintsInvalidCommand() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, "wrong", "first station", "1");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError(printCaptor.capture());
        assertEquals("Invalid Command", printCaptor.getValue());
    }

    @Test
    void whenAddHeadInvalidTime_executePrintsInvalidCommand() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, "wrong", "first station", "1a");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError(printCaptor.capture());
        assertEquals("Invalid Command", printCaptor.getValue());
    }

    @Test
    void whenAppendValidLine_executeAddsLineHead() {
        insertCommand = new InsertCommand(CommandType.APPEND, printer, LINE_NAME, "first station", "5");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(5, lines.get(LINE_NAME).size());
        assertEquals("first station", lines.get(LINE_NAME).get(3).getName());
        assertEquals(LINE_NAME, lines.get(LINE_NAME).get(3).getLine());
        assertEquals(5, lines.get(LINE_NAME).get(3).getTimeToNextStationInLine());
    }

    @Test
    void whenAppendValid_executeCreatesPrevNext() {
        insertCommand = new InsertCommand(CommandType.APPEND, printer, LINE_NAME, "first station", "4");
        insertCommand.execute(lines);
        assertEquals("first station", lines.get(LINE_NAME).get(3).getName());
        assertEquals(1, lines.get(LINE_NAME).get(3).getPreviousInLine().size());
        assertEquals("other station", lines.get(LINE_NAME).get(3).getPreviousInLine().get(0));
        assertEquals("other station", lines.get(LINE_NAME).get(2).getName());
        assertEquals(1, lines.get(LINE_NAME).get(2).getNextInLine().size());
        assertEquals("first station", lines.get(LINE_NAME).get(2).getNextInLine().get(0));
    }

    @Test
    void whenAppendInvalidLine_executePrintsInvalidCommand() {
        insertCommand = new InsertCommand(CommandType.APPEND, printer, "wrong", "first station", "2");
        insertCommand.execute(lines);
        assertEquals(1, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError(printCaptor.capture());
        assertEquals("Invalid Command", printCaptor.getValue());
    }
    @Test
    void getType() {
        insertCommand = new InsertCommand(CommandType.ADD_HEAD, printer, LINE_NAME, "first station", "0");
        assertEquals(CommandType.ADD_HEAD, insertCommand.getType());
        insertCommand = new InsertCommand(CommandType.APPEND, printer, LINE_NAME, "first station", "1");
        assertEquals(CommandType.APPEND, insertCommand.getType());
    }
}
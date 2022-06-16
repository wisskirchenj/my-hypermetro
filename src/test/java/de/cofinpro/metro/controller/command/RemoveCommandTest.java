package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveCommandTest {

    private static final String LINE_NAME = "line 1";
    @Mock
    MetroPrinter printer;

    RemoveCommand removeCommand;

    MetroNet lines;

    @BeforeEach
    void setup() {
        lines = new MetroNet();
        MetroLine line = new MetroLine(LINE_NAME);
        line.addStationByName(0,"depot", 1);
        line.addStationByName(1,"station", 1);
        line.addStationByName(2,"other station", 2);
        line.addStationByName(3,"depot", 0);
        lines.put(LINE_NAME, line);
        line = new MetroLine("line2");
        line.addStationByName(0,"depot", 2);
        line.addStationByName(1,"station", 2);
        line.addStationByName(2,"newone", 5);
        line.addStationByName(3,"depot", 0);
        lines.put("line2", line);
    }

    @Test
    void whenRemoveValidLineAndStation_executeRemoves() {
        removeCommand = new RemoveCommand(printer, LINE_NAME, "other station");
        removeCommand.execute(lines);
        assertEquals(2, lines.size());
        assertEquals(3, lines.get(LINE_NAME).size());
        assertEquals("station", lines.get(LINE_NAME).get(1).getName());
        assertEquals("depot", lines.get(LINE_NAME).get(2).getName());
    }

    @Test
    void whenRemoveValidLineInvalidStation_executeDoesNothing() {
        removeCommand = new RemoveCommand(printer, LINE_NAME, "not there");
        removeCommand.execute(lines);
        assertEquals(2, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        assertEquals("station", lines.get(LINE_NAME).get(1).getName());
        assertEquals("depot", lines.get(LINE_NAME).get(3).getName());
    }

    @Test
    void whenRemoveInvalidLine_executePrintsInvalidCommand() {
        removeCommand = new RemoveCommand(printer, "wrong", "first station");
        removeCommand.execute(lines);
        assertEquals(2, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        verify(printer).printError("Invalid Command");
    }

    @Test
    void whenRemoveStationWithTransfer_stationRemovedFromTransferList() {
        new ConnectCommand(printer, LINE_NAME, "station", "line2", "newone")
                .execute(lines);
        new ConnectCommand(printer, LINE_NAME, "other station", "line2", "newone")
                .execute(lines);
        assertTrue(lines.findStation(LINE_NAME, "station").isPresent());
        assertEquals(1, lines.findStation(LINE_NAME, "station").get().getTransfer().size());
        assertTrue(lines.findStation(LINE_NAME, "other station").isPresent());
        assertEquals(1, lines.findStation(LINE_NAME, "other station").get().getTransfer().size());
        removeCommand = new RemoveCommand(printer, "line2", "newone");
        removeCommand.execute(lines);
        assertEquals(2, lines.size());
        assertEquals(4, lines.get(LINE_NAME).size());
        assertEquals(3, lines.get("line2").size());
        assertTrue(lines.findStation(LINE_NAME, "station").isPresent());
        assertTrue(lines.findStation(LINE_NAME, "other station").isPresent());
        assertEquals(0, lines.findStation(LINE_NAME, "station").get().getTransfer().size());
        assertEquals(0, lines.findStation(LINE_NAME, "other station").get().getTransfer().size());
    }

    @Test
    void getType() {
        removeCommand = new RemoveCommand(printer, "any", "any");
        assertEquals(CommandType.REMOVE, removeCommand.getType());
    }
}
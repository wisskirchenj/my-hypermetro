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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutputCommandTest {

    @Mock
    MetroPrinter printer;

    OutputCommand outputCommand;
    MetroNet lines;

    @BeforeEach
    void setUp() {
        lines = new MetroNet();
        MetroLine line = new MetroLine("line 1");
        line.addStationByName(0,"depot", 1);
        line.addStationByName(1,"station", 2);
        line.addStationByName(2,"other station", 3);
        line.addStationByName(3,"depot", 0);
        lines.put("line 1", line);
    }

    @Test
    void whenValidLine_executeOutputsList() {
        outputCommand = new OutputCommand(printer, "line 1");
        outputCommand.execute(lines);
        verify(printer).printLine(lines.get("line 1"));
        verify(printer, times(0)).printError(anyString());
    }

    @Test
    void whenInvalidLine_executePrintsError() {
        outputCommand = new OutputCommand(printer, "wrong");
        outputCommand.execute(lines);
        verify(printer,times(0)).printLine(anyList());
        verify(printer).printError("Invalid Command");
    }
    @Test
    void getType() {
        outputCommand = new OutputCommand(printer, "sth");
        assertEquals(CommandType.OUTPUT, outputCommand.getType());
    }
}
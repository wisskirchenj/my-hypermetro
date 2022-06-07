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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutputCommandTest {

    @Mock
    StationsPrinter printer;

    OutputCommand outputCommand;
    Map<String, MetroLine> lines;

    @BeforeEach
    void setUp() {
        lines = new HashMap<>();
        MetroLine line = new MetroLine();
        line.addStationByName(0,"depot");
        line.addStationByName(1,"station");
        line.addStationByName(2,"other station");
        line.addStationByName(3,"depot");
        lines.put("line 1", line);
    }

    @Test
    void whenValidLine_executeOutputsList() {
        outputCommand = new OutputCommand(printer, "line 1");
        outputCommand.execute(lines);
        verify(printer).print(lines.get("line 1"));
        verify(printer, times(0)).printError(anyString());
    }

    @Test
    void whenInvalidLine_executePrintsError() {
        outputCommand = new OutputCommand(printer, "wrong");
        outputCommand.execute(lines);
        verify(printer,times(0)).print(anyList());
        verify(printer).printError("Invalid Command");
    }
    @Test
    void getType() {
        outputCommand = new OutputCommand(printer, "sth");
        assertEquals(CommandType.OUTPUT, outputCommand.getType());
    }
}
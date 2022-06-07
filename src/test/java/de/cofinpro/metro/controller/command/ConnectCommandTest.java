package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.TransferStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConnectCommandTest {

    @Mock
    StationsPrinter printer;

    ConnectCommand connectCommand;

    Map<String, MetroLine> lines;

    @BeforeEach
    void setup() {
        lines = new HashMap<>();
        MetroLine line = new MetroLine();
        line.addStationByName(0,"depot");
        line.addStationByName(1,"station");
        line.addStationByName(2,"otherstation");
        line.addStationByName(3,"depot");
        lines.put("line1", line);
        line = new MetroLine();
        line.addStationByName(0,"depot");
        line.addStationByName(1,"station");
        line.addStationByName(2,"newone");
        line.addStationByName(3,"depot");
        lines.put("line2", line);
    }

    @ParameterizedTest
    @CsvSource({
            "line1, otherstation, line2, station",
            "line1, station, line2, newone",
            "line1, station, line2, station",
            "line1, otherstation, line2, newone"
    })
    void whenConnectValidParams_executeConnects(String line, String station,
                                                String transferLine, String transferStation) {
        connectCommand = new ConnectCommand(printer, line, station, transferLine, transferStation);
        connectCommand.execute(lines);
        assertTrue(lines.get(line).findStationByName(station).isPresent());
        assertEquals(List.of(new TransferStation(transferLine, transferStation)),
                lines.get(line).findStationByName(station).get().getTransfer());
        assertTrue(lines.get(transferLine).findStationByName(transferStation).isPresent());
        assertEquals(List.of(new TransferStation(line, station)),
                lines.get(transferLine).findStationByName(transferStation).get().getTransfer());
    }

    @Test
    void whenTwoConnectToOne_executeConnectProvidesTransferListWithTwo() {
        connectCommand = new ConnectCommand(printer, "line1", "station", "line2", "station");
        connectCommand.execute(lines);
        connectCommand = new ConnectCommand(printer, "line1", "station", "line2", "newone");
        connectCommand.execute(lines);
        assertTrue(lines.get("line1").findStationByName("station").isPresent());
        assertEquals(List.of(new TransferStation("line2", "station"), new TransferStation("line2", "newone")),
                lines.get("line1").findStationByName("station").get().getTransfer());
        assertTrue(lines.get("line2").findStationByName("newone").isPresent());
        assertEquals(List.of(new TransferStation("line1", "station")),
                lines.get("line2").findStationByName("newone").get().getTransfer());
    }

    @ParameterizedTest
    @CsvSource({
            "line0, station, line2, newone",
            "line1, stattttion, line2, newone",
            "line1, station, linzzze2, newone",
            "line1, station, line2, nothtere",
    })
    void whenConnectInvalidParams_executePrintsInvalidCommand(String line, String station,
                                                              String transferLine, String transferStation) {
        connectCommand = new ConnectCommand(printer, line, station, transferLine, transferStation);
        connectCommand.execute(lines);
        assertEquals(2, lines.size());
        assertEquals(4, lines.get("line1").size());
        verify(printer).printError("Invalid Command");
    }

    @Test
    void getType() {
        connectCommand = new ConnectCommand(printer, "any", "any", "any", "any");
        assertEquals(CommandType.CONNECT, connectCommand.getType());
    }
}
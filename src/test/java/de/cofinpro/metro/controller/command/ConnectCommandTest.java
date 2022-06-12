package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.TransferStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConnectCommandTest {

    @Mock
    MetroPrinter printer;

    ConnectCommand connectCommand;

    MetroNet lines;

    @BeforeEach
    void setup() {
        lines = new MetroNet();
        MetroLine line = new MetroLine("line1");
        line.addStationByName(0,"depot");
        line.addStationByName(1,"station");
        line.addStationByName(2,"otherstation");
        line.addStationByName(3,"depot");
        lines.put("line1", line);
        line = new MetroLine("line2");
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
        assertEquals(new TransferStation(transferLine, transferStation),
                lines.get(line).findStationByName(station).get().getTransfer().get(0));
        assertTrue(lines.get(transferLine).findStationByName(transferStation).isPresent());
        assertEquals(new TransferStation(line, station),
                lines.get(transferLine).findStationByName(transferStation).get().getTransfer().get(0));
    }

    @Test
    void whenTwoConnectToOne_executeConnectProvidesTransferListWithTwo() {
        connectCommand = new ConnectCommand(printer, "line1", "station", "line2", "station");
        connectCommand.execute(lines);
        connectCommand = new ConnectCommand(printer, "line1", "station", "line2", "newone");
        connectCommand.execute(lines);
        assertTrue(lines.get("line1").findStationByName("station").isPresent());
        assertEquals(2, lines.get("line1").findStationByName("station").get().getTransfer().size());
        assertTrue(lines.get("line2").findStationByName("newone").isPresent());
        assertEquals(new TransferStation("line1", "station"),
                lines.get("line2").findStationByName("newone").get().getTransfer().get(0));
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
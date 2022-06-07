package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.StationsPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.TransferStation;

import java.util.Map;

/**
 * ConnectCommand class is used for the /connect instruction on the command line.
 * The command has four arguments lineName, stationName, transferLineName and transferStationName.
 * Its execution connects two stations of two separate lines as transfer stations.
 */
public class ConnectCommand implements LineCommand {

    private final StationsPrinter stationsPrinter;
    private final String lineName;
    private final String stationName;
    private final String transferLineName;
    private final String transferStationName;

    public ConnectCommand(StationsPrinter stationsPrinter, String lineName, String stationName, String transferLineName, String transferStationName) {
        this.stationsPrinter = stationsPrinter;
        this.lineName = lineName;
        this.stationName = stationName;
        this.transferLineName = transferLineName;
        this.transferStationName = transferStationName;
    }

    /**
     * connects the station given by name and line to the station given by transferLine, -name,
     * and vice versa - iff the lines and stations are found. If not, invalid command is thrown.
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(Map<String, MetroLine> lines) {
        MetroLine line = lines.get(lineName);
        MetroLine transferLine = lines.get(transferLineName);
        if (line == null
                || line.findStationByName(stationName).isEmpty()
                || transferLine == null
                || transferLine.findStationByName(transferStationName).isEmpty()) {
            stationsPrinter.printError("Invalid Command");
        } else {
            line.findStationByName(stationName).get()
                    .setTransfer(new TransferStation(transferLineName, transferStationName));
            transferLine.findStationByName(transferStationName).get()
                    .setTransfer(new TransferStation(lineName, stationName));
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.CONNECT;
    }
}

package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;
import de.cofinpro.metro.model.TransferStation;

/**
 * ConnectCommand class is used for the /connect instruction on the command line.
 * The command has four arguments lineName, stationName, transferLineName and transferStationName.
 * Its execution connects two stations of two separate lines as transfer stations.
 */
public class ConnectCommand extends TwoStationsCommand {

    public ConnectCommand(MetroPrinter metroPrinter, String lineName, String stationName,
                          String transferLineName, String transferStationName) {
        super(metroPrinter, lineName, stationName, transferLineName, transferStationName);
    }

    @Override
    protected void executeTwoStationsCommand(MetroNet lines, Station station, Station transferStation) {
        station.addTransfer(new TransferStation(transferStation.getLine(), transferStation.getName()));
        transferStation.addTransfer(new TransferStation(station.getLine(), station.getName()));
    }

    @Override
    public CommandType getType() {
        return CommandType.CONNECT;
    }
}

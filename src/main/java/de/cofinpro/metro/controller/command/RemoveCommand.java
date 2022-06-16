package de.cofinpro.metro.controller.command;

import de.cofinpro.metro.io.MetroPrinter;
import de.cofinpro.metro.model.MetroLine;
import de.cofinpro.metro.model.MetroNet;
import de.cofinpro.metro.model.Station;
import de.cofinpro.metro.model.TransferStation;

/**
 * RemoveCommand class is used for the /remove to delete a station given from the given line.
 */
public class RemoveCommand implements LineCommand {

    private final MetroPrinter metroPrinter;
    private final String lineName;
    private final String stationName;

    public RemoveCommand(MetroPrinter printer, String lineName, String stationName) {
        this.metroPrinter = printer;
        this.lineName = lineName;
        this.stationName = stationName;
    }

    /**
     * removes the station given by name from the line given by name.
     * If line is not found by name: Invalid Command is print out.
     * If the station is not in the given line, nothing happens - in accordance with List.remove
     * @param lines lineName-key map containing all Metrolines in the current state
     */
    @Override
    public void execute(MetroNet lines) {
        MetroLine line = lines.get(lineName);
        if (line == null) {
            metroPrinter.printError("Invalid Command");
        } else {
            // remove the station from all transfer lists, where it is contained
            if (lines.findStation(lineName, stationName).isPresent()) {
                TransferStation stationAsTransferStation = new TransferStation(lineName, stationName);
                Station station = lines.findStation(lineName, stationName).get();
                for (TransferStation transfer: station.getTransfer()) {
                    lines.findStation(transfer.getLine(), transfer.getStation())
                            .ifPresent(transferStation -> transferStation.getTransfer().remove(stationAsTransferStation));
                }
            }
            line.removeStationByName(stationName);
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.REMOVE;
    }
}

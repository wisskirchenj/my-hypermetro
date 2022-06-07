package de.cofinpro.metro.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a station in a metro line, given by the station name and a possibly empty list of transfer stations
 * of other lines.
 */
@Data
public class Station {
    private final String name;
    private List<TransferStation> transfer = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        transfer.forEach(transferStation -> builder.append(" - ").append(transferStation.getStation())
                .append(" (").append(transferStation.getLine()).append(" line)"));
        return builder.toString();
    }

    /**
     * add the given transfer station to the transfer list
     * @param transferStation the transfer station to add
     */
    public void addTransfer(TransferStation transferStation) {
        transfer.add(transferStation);
    }
}

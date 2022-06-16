package de.cofinpro.metro.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a station in a metro line, given by the station name and a possibly empty list of transfer stations
 * of other lines.
 */
@Data
public class Station {

    private String line;
    private final String name;
    @SerializedName("time")
    private int timeToNextStationInLine;
    private final List<TransferStation> transfer = new ArrayList<>();

    public Station(String name) {
        this.name = name;
        this.timeToNextStationInLine = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        transfer.forEach(transferStation -> builder.append(" - ").append(transferStation));
        return builder.toString();
    }

    /**
     * add the given transfer station to the transfer list if not yet contained.
     * @param transferStation the transfer station to add
     */
    public void addTransfer(TransferStation transferStation) {
        if (!transfer.contains(transferStation)) {
            transfer.add(transferStation);
        }
    }
}

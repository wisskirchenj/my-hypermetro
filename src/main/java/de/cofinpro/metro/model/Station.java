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
    @SerializedName("prev")
    private List<String> previousInLine = new ArrayList<>();
    @SerializedName("next")
    private List<String> nextInLine = new ArrayList<>();

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

    public void addTransfer(TransferStation transferStation) {
        if (!transfer.contains(transferStation)) {
            transfer.add(transferStation);
        }
    }

    public void addPrevious(Station station) {
        if (!previousInLine.contains(station.getName())) {
            previousInLine.add(station.getName());
        }
    }

    public void addNext(Station station) {
        if (!nextInLine.contains(station.getName())) {
            nextInLine.add(station.getName());
        }
    }
}

package de.cofinpro.metro.model;

import lombok.Data;

/**
 * DTO representing a station in a metro line, given by the station name and a possible transfer station in
 * another line.
 */
@Data
public class Station {
    private final String name;
    private TransferStation transfer;

    @Override
    public String toString() {
        return name + (transfer == null ? "" : " - " + transfer);
    }
}

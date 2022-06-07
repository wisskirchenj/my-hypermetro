package de.cofinpro.metro.model;

import lombok.Data;

@Data
public class Station {
    private final String name;
    private TransferStation transfer;

    @Override
    public String toString() {
        return name + (transfer == null ? "" : " - " + transfer);
    }
}

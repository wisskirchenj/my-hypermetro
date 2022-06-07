package de.cofinpro.metro.model;

import lombok.Value;

@Value
public class TransferStation {
    String line;
    String station;

    @Override
    public String toString() {
        return station + " (" + line + " line)";
    }
}

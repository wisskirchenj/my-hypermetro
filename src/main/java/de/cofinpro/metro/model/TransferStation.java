package de.cofinpro.metro.model;

import lombok.Value;

/**
 * immutable DTO representing a transfer station at a metro line.
 */
@Value
public class TransferStation {
    String line;
    String station;

    @Override
    public String toString() {
        return station + " (" + line + " line)";
    }
}

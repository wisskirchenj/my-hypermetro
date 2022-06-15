package de.cofinpro.metro.model;

import lombok.Value;

@Value
class UpwardsTimeWeightedNode {

    Station station;
    UpwardsTimeWeightedNode parent;
    int timeWeight;
}

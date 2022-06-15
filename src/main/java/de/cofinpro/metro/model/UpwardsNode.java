package de.cofinpro.metro.model;

/**
 * internal simplified TreeNode structure, that only implements the Node's station data and the parent,
 * which is the minimal requirement to route back after finding the target station.
 */
class UpwardsNode {
    Station station;
    UpwardsNode parent;

    public UpwardsNode(Station station, UpwardsNode parent) {
        this.station = station;
        this.parent = parent;
    }
}

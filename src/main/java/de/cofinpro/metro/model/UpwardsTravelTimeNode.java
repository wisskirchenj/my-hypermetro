package de.cofinpro.metro.model;

import lombok.Value;

/**
 * internal simplified immutable TreeNode structure, that only implements the Node's station data, travel
 * time and the parent, which is the minimal requirement to route back after finding the target station.
 * This structure is used in the priority queue of Dijkstra algorithm with the time as priority parameter.
 */
@Value
class UpwardsTravelTimeNode {

    Station station;
    UpwardsTravelTimeNode parent;
    int time;
}

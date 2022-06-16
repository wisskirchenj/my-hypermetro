package de.cofinpro.metro.model;

import lombok.Value;

@Value
class UpwardsTravelTimeNode {

    Station station;
    UpwardsTravelTimeNode parent;
    int time;
}

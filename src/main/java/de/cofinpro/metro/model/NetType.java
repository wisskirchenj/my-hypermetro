package de.cofinpro.metro.model;

/**
 * enum to distinguish the CLASSICAL metro net format from stages up to 5, where each station hat at most one precessor
 * and successor and the EXTENDED metro net of stage 6 with bifurcations and loops (the London template..)
 */
public enum NetType {
    CLASSICAL, EXTENDED
}

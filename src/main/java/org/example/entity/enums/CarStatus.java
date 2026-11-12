package org.example.entity.enums;

public enum CarStatus {
    AVAILABLE,
    ON_RENT,
    IN_MAINTENANCE,
    RETIRED;

    public boolean canTransitionTo(CarStatus next) {
        return switch (this) {
            case AVAILABLE -> next == ON_RENT || next == IN_MAINTENANCE || next == RETIRED;
            case ON_RENT -> next == AVAILABLE || next == IN_MAINTENANCE;
            case IN_MAINTENANCE -> next == AVAILABLE || next == RETIRED;
            case RETIRED -> false;
        };
    }
}

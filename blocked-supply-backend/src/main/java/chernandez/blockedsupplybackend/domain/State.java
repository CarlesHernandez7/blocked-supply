package chernandez.blockedsupplybackend.domain;

import java.math.BigInteger;

public enum State {
    CREATED, IN_TRANSIT, STORED, DELIVERED;

    public static State fromBigInt(BigInteger value) {
        return switch (value.intValue()) {
            case 0 -> CREATED;
            case 1 -> IN_TRANSIT;
            case 2 -> STORED;
            case 3 -> DELIVERED;
            default -> throw new IllegalArgumentException("Invalid state value: " + value);
        };
    }

    public static State fromInt(int value) {
        return switch (value) {
            case 0 -> CREATED;
            case 1 -> IN_TRANSIT;
            case 2 -> STORED;
            case 3 -> DELIVERED;
            default -> throw new IllegalArgumentException("Invalid state value: " + value);
        };
    }
}

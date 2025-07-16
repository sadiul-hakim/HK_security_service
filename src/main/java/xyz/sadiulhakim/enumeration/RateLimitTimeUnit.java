package xyz.sadiulhakim.enumeration;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum RateLimitTimeUnit {
    MILLISECOND(1, "MilliSecond"),
    SECOND(2, "Second"),
    MINUTE(3, "Minute"),
    HOUR(4, "Hour"),
    DAY(5, "Day"),
    MONTH(6, "Month"),
    YEAR(7, "Year");

    private final int id;
    private final String name;

    RateLimitTimeUnit(int value, String name) {
        this.id = value;
        this.name = name;
    }

    public static RateLimitTimeUnit getById(int id) {
        for (RateLimitTimeUnit units : RateLimitTimeUnit.values()) {
            if (units.id == id) {
                return units;
            }
        }
        return SECOND;
    }

    public static RateLimitTimeUnit getByName(String name) {
        for (RateLimitTimeUnit units : RateLimitTimeUnit.values()) {
            if (Objects.equals(units.name, name)) {
                return units;
            }
        }
        return SECOND;
    }
}


package xyz.sadiulhakim.enumeration;

import lombok.Getter;

@Getter
public enum UserLevel {
    NORMAL(1, "Normal"),
    ADMIN(2, "Admin"),
    SUPER(3, "Super");
    private final String name;
    private final int id;

    UserLevel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserLevel getById(int id) {
        for (UserLevel item : values()) {
            if (item.getId() == id)
                return item;
        }
        return UserLevel.NORMAL;
    }

    public static UserLevel getByName(String name) {
        for (UserLevel item : values()) {
            if (item.getName().equals(name))
                return item;
        }
        return UserLevel.NORMAL;
    }
}

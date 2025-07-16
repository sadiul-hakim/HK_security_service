package xyz.sadiulhakim.enumeration;

import lombok.Getter;

@Getter
public enum HttpMethod {
    ANY(0, "any"), GET(1, "get"), POST(2, "post"),
    DELETE(3, "delete"), PUT(4, "put"), OPTIONS(5, "options");

    private final int id;
    private final String name;

    HttpMethod(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static HttpMethod getByName(String name) {
        for (HttpMethod item : HttpMethod.values()) {
            if (item.name.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static HttpMethod getById(int id) {
        for (HttpMethod item : HttpMethod.values()) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}

package com.mdg.whatsmylunch.domain;

public class Restaurant {
    private final int id;
    private final String name;

    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Restaurant of(int id, String name) {
        return new Restaurant(id, name);
    }
}

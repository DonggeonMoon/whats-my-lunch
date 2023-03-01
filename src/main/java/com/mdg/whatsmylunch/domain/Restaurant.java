package com.mdg.whatsmylunch.domain;

public record Restaurant(int id, String name) {
    public static Restaurant of(int id, String name) {
        return new Restaurant(id, name);
    }
}

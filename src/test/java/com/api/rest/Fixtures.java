package com.api.rest;

public class Fixtures {

    public static BlogUser User_John() {
        return new BlogUser(1L, "John", "Wick", 45);
    }

    public static BlogUser User_Tom() {
        return new BlogUser(2L, "Tom", "Holland", 23);
    }

    public static BlogUser User_Bob() {
        return new BlogUser(3L, "Bob", "Miller", 28);
    }

}

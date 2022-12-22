package com.api.rest.IT;

import com.api.rest.BlogUser;

public class FixturesIT {
    public static BlogUser User_John() {
        return new BlogUser("John", "Wick", 45);
    }

    public static BlogUser User_Tom() {
        return new BlogUser("Tom", "Holland", 23);
    }

    public static BlogUser User_Bob() {
        return new BlogUser("Bob", "Miller", 28);
    }
}

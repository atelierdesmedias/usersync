package org.xwiki.contrib.usersync.discourse.internal;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;

    private String username;

    private String name;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
}

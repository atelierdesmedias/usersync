package org.xwiki.contrib.usersync.discourse.internal;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;

    private String username;

    private String name;

    private String email;

    private String password;

    public User(String _username, String _name, String _email, String _password) {
        username = _username;
        name = _name;
        email = _email;
        password = _password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

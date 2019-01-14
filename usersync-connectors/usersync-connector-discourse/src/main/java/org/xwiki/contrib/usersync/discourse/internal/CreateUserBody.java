package org.xwiki.contrib.usersync.discourse.internal;

import com.google.gson.annotations.SerializedName;

public class CreateUserBody {
    private String username;

    private String name;

    private String email;

    private String password;

    public CreateUserBody(String _username, String _name, String _email, String _password) {
        username = _username;
        name = _name;
        email = _email;
        password = _password;
    }
}

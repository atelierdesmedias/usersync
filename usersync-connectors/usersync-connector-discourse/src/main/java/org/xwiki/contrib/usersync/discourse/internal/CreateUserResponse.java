package org.xwiki.contrib.usersync.discourse.internal;

import org.xwiki.contrib.usersync.discourse.internal.User;

public class CreateUserResponse {
    private Boolean success;

    private String message;

    private Integer user_id;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getUserId() { return user_id; }
}

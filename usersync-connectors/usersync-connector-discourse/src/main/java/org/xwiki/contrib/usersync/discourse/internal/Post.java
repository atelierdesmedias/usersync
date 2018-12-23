package org.xwiki.contrib.usersync.discourse.internal;

import com.google.json.annotation.SerializedName;

public class Post {
    private int userId;

    private int id;

    private String title;

    @SerializedName("body")
    private String text;

    public int getUserId() {
        return d;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}

package org.xwiki.contrib.usersync.discourse.internal;

import java.util.List;
//import org.simpleframework.xml.Path;
import retrofit2.Call;
import retrofit2.http.GET;

import org.xwiki.contrib.usersync.discourse.internal.Post;

public interface DiscourseService {
  @GET("posts")
  Call<List<Post>> getPosts();
}


package org.xwiki.contrib.usersync.discourse.internal;

import java.util.List;
import org.simpleframework.xml.Path;
import retrofit2.Call;
import retrofit2.http.GET;

import org.xwiki.contrib.usersync.discourse.internal.Repo;

public interface DiscourseService {
  //@GET("admin/users/list/active.json")
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}


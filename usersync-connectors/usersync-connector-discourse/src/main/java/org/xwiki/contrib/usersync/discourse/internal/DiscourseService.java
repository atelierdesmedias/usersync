package org.xwiki.contrib.usersync.discourse.internal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import org.xwiki.contrib.usersync.discourse.internal.GetUserResponse;

public interface DiscourseService {
  @GET("users/{username}.json")
  Call<GetUserResponse> getUser(@Path("username") String username, @Query("api_key") String apiKey, @Query("api_user") String apiUser);
}


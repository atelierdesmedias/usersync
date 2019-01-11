package org.xwiki.contrib.usersync.discourse.internal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

import org.xwiki.contrib.usersync.discourse.internal.User;
import org.xwiki.contrib.usersync.discourse.internal.GetUserResponse;
import org.xwiki.contrib.usersync.discourse.internal.CreateUserResponse;

public interface DiscourseService {
  @GET("users/{username}.json")
  Call<GetUserResponse> getUser(@Path("username") String username, @Query("api_key") String apiKey, @Query("api_username") String apiUsername);

  @POST("users")
  Call<CreateUserResponse> createUser(@Body User user, @Query("api_key") String apiKey, @Query("api_username") String apiUsername);
}


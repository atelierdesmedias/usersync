package org.xwiki.contrib.usersync.discourse.internal;

import retrofit2.Call;
import retrofit2.http.*;

import org.xwiki.contrib.usersync.discourse.internal.*;

public interface DiscourseService {
  @GET("users/{username}.json")
  Call<GetUserResponse> getUser(@Path("username") String username, @Query("api_key") String apiKey, @Query("api_username") String apiUsername);

  @POST("users")
  Call<CreateUserResponse> createUser(@Body CreateUserBody createUserBody, @Query("api_key") String apiKey, @Query("api_username") String apiUsername);

  @DELETE("/admin/users/{userid}.json")
  Call<DeleteUserResponse> deleteUser(@Path("userid") Integer userId, @Query("api_key") String apiKey, @Query("api_username") String apiUsername);
}


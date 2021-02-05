package org.xwiki.contrib.usersync.discourse.internal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Header;

public interface DiscourseService {
  @GET("users/{username}.json")
  Call<GetUserResponse> getUser(@Path("username") String username, @Header("Api-Key") String apiKey, @Header("Api-Username") String apiUsername);

  @POST("users")
  Call<CreateUserResponse> createUser(@Body CreateUserBody createUserBody, @Header("Api-Key") String apiKey, @Header("Api-Username") String apiUsername);

  @DELETE("/admin/users/{userid}.json")
  Call<DeleteUserResponse> deleteUser(@Path("userid") Integer userId, @Header("Api-Key") String apiKey, @Header("Api-Username") String apiUsername);
}


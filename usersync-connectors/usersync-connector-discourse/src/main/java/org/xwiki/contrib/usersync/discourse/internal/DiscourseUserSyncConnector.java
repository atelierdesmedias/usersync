/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.usersync.discourse.internal;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.usersync.UserSyncConnector;
import org.xwiki.contrib.usersync.UserSyncException;

import com.xpn.xwiki.objects.BaseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * {@link UserSyncConnector} implementation for Discourse.
 *
 * @version $Id$
 */
@Component
@Singleton
@Named("discourse")
public class DiscourseUserSyncConnector implements UserSyncConnector, Initializable
{
    private static final String PREFIX_CONFIGURATION = "usersync.discourse.";

    public static final String CONFIGURATION_URL = PREFIX_CONFIGURATION + "url";
    public static final String CONFIGURATION_API_KEY = PREFIX_CONFIGURATION + "api_key";
    public static final String CONFIGURATION_API_USERNAME = PREFIX_CONFIGURATION + "api_username";

    @Inject
    private ConfigurationSource configuration;

    HttpLoggingInterceptor logging;
    OkHttpClient.Builder httpClient;

    private Retrofit retrofit;
    private DiscourseService service;
    String discourseURL;
    String discourseApiKey;
    String discourseApiUsername;

    @Override
    public void initialize() throws InitializationException
    {
        // Get the URL of the discourse server to synchronize with
        discourseURL = this.configuration.getProperty(CONFIGURATION_URL);
        discourseApiKey = this.configuration.getProperty(CONFIGURATION_API_KEY);
        discourseApiUsername = this.configuration.getProperty(CONFIGURATION_API_USERNAME);

        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
            .baseUrl(discourseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

        service = retrofit.create(DiscourseService.class);
    }

    @Override
    public void createUser(BaseObject userObject) throws UserSyncException {
        // Get the user login
        String userId = userObject.getStringValue("id");

        // Get the user email
        String email = userObject.getStringValue("email");

        // Get the user password
        String password = userObject.getStringValue("password");

        // Get the user name
        String name = userObject.getStringValue("name");

        System.out.printf("creating user: %s / %s / %s / %s\n", userId, email, password, name);

        CreateUserBody createUserBody = new CreateUserBody(userId, name, email, password);

        Call<CreateUserResponse> call = service.createUser(createUserBody, discourseApiKey, discourseApiUsername);

        try {
            Response<CreateUserResponse> response = call.execute();
            if(response.isSuccessful()) {
                System.out.println("succeed!");
                CreateUserResponse createUserResponse = response.body();
                if (createUserResponse.getSuccess()) {
                    System.out.println("Success creating user with id:" + createUserResponse.getUserId());
                    userObject.setIntValue("id", createUserResponse.getUserId());
                } else {
                    throw new UserSyncException(createUserResponse.getMessage());
                }
            } else {
                System.out.println("Code: " + response.code());
                throw new UserSyncException("Bad response code:" + response.code());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new UserSyncException("Unknown error", exception);
        }
    }

    @Override
    public void modifyUser(BaseObject previousUser, BaseObject newUser) throws UserSyncException
    {
        // TODO
    }

    @Override
    public void deleteUser(BaseObject userObject) throws UserSyncException
    {
        // Get the user login
        String userName = userObject.getStringValue("id");

        System.out.printf("deleting user: %s\n", userName);

        Call<GetUserResponse> getUserResponseCall = service.getUser(userName, discourseApiKey, discourseApiUsername);



        try {
            Response<GetUserResponse> getUserResponse = getUserResponseCall.execute();
            if (getUserResponse.isSuccessful()) {
                System.out.printf("user name is " + getUserResponse.body().getUser().getEmail());
                Integer userId = getUserResponse.body().getUser().getId();
                System.out.printf("user id is " + userId);
                Call<DeleteUserResponse> deleteUserResponseCall = service.deleteUser(userId, discourseApiKey, discourseApiUsername);

                Response<DeleteUserResponse> deleteUserResponse = deleteUserResponseCall.execute();
                if(deleteUserResponse.isSuccessful()) {
                    System.out.println("succeed!");
                    if (deleteUserResponse.body().getDeleted()) {
                        System.out.println("Success deleting user");
                    } else {
                        throw new UserSyncException("Failed deleting user");
                    }
                } else {
                    System.out.println("Code: " + deleteUserResponse.code());
                    throw new UserSyncException("Bad response code for deleteUser:" + deleteUserResponse.code());
                }
            } else {
                System.out.println("Code: " + getUserResponse.code());
                throw new UserSyncException("Bad response code for getUser:" + getUserResponse.code());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new UserSyncException("Unknown error", exception);
        }
    }
}

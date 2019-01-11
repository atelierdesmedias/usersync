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

import java.util.List;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.usersync.UserSyncConnector;
import org.xwiki.contrib.usersync.UserSyncException;

import com.xpn.xwiki.objects.BaseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import org.xwiki.contrib.usersync.discourse.internal.DiscourseService;

/**
 * {@link UserSyncConnector} implementation for Discourse.
 *
 * @version $Id$
 */
@Component
@Singleton
@Named("discourse")
public class DiscourseUserSyncConnector implements UserSyncConnector
{
    private static final String PREFIX_CONFIGURATION = "usersync.discourse.";

    private static final String CONFIGURATION_URL = PREFIX_CONFIGURATION + "url";
    private static final String CONFIGURATION_API_KEY = PREFIX_CONFIGURATION + "api_key";
    private static final String CONFIGURATION_API_USERNAME = PREFIX_CONFIGURATION + "api_username";

    @Inject
    private ConfigurationSource configuration;

    HttpLoggingInterceptor logging;
    OkHttpClient.Builder httpClient;

    private Retrofit retrofit;
    private DiscourseService service;
    String discourseURL;
    String discourseApiKey;
    String discourseApiUsername;

    public DiscourseUserSyncConnector() {
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
    public void getUser(String userId) {
        Call<GetUserResponse> call = service.getUser(userId, discourseApiKey, discourseApiUsername);

        try {
            Response<GetUserResponse> response = call.execute();
            if(response.isSuccessful()) {
                System.out.println("succeed!");
                GetUserResponse getUserResponse = response.body();
                System.out.println("Name: " + getUserResponse.getUser().getName());
            } else {
                System.out.println("Code: " + response.code());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
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

        User user = new User(userId, name, email, password);

        Call<CreateUserResponse> call = service.createUser(user, discourseApiKey, discourseApiUsername);

        try {
            Response<CreateUserResponse> response = call.execute();
            if(response.isSuccessful()) {
                System.out.println("succeed!");
                CreateUserResponse createUserResponse = response.body();
                if (createUserResponse.getSuccess()) {
                    System.out.println("Success creating user");
                } else {
                    throw new UserSyncException(createUserResponse.getMessage());
                }
            } else {
                System.out.println("Code: " + response.code());
                throw new UserSyncException("Bad response code:" + response.code());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void modifyUser(BaseObject previousUser, BaseObject newUser)
    {
        // TODO
    }

    @Override
    public void deleteUser(BaseObject deletedUser)
    {
        // TODO
    }
}

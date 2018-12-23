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

import com.xpn.xwiki.objects.BaseObject;

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
    private static final String API_KEY = PREFIX_CONFIGURATION + "api_key";

    @Inject
    private ConfigurationSource configuration;

    @Override
    public void createUser(BaseObject user)
    {
        // Get the user login
        String userId = user.getReference().getName();

        // Get the user mail
        String mail = user.getStringValue("email");

        // Get the URL of the discourse server to synchronize with
        String discourseURL = this.configuration.getProperty(CONFIGURATION_URL);
        String discourseApiKey = this.configuration.getProperty(API_KEY);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        DiscourseService service = retrofit.create(DiscourseService.class);
        Call<List<Post>> call = service.getPosts();

        try {
            Response<List<Post>> response = call.execute();
            if(response.isSuccessful()) {
                System.out.println("succeed!");
                List<Post> posts = response.body();
                for(Post post : posts) {
                    System.out.println(post.getId() + " / " + post.getTitle());
                }
            } else {
                System.out.println("Code: " + response.code());
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

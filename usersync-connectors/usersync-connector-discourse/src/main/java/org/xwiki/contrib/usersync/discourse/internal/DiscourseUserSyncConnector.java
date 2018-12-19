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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.usersync.UserSyncConnector;

import com.xpn.xwiki.objects.BaseObject;

import retrofit2.Retrofit.Builder;

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

        // TODO
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

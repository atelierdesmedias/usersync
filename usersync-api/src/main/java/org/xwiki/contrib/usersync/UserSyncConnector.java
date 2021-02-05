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
package org.xwiki.contrib.usersync;

import org.xwiki.component.annotation.Role;

import com.xpn.xwiki.objects.BaseObject;

/**
 * Component role to implement to add support for a new external server where to update user accounts.
 * 
 * @version $Id$
 */
@Role
public interface UserSyncConnector
{
    /**
     * @param user the object containing the metadata of the new user
     */
    void createUser(BaseObject user) throws UserSyncException;

    /**
     * @param previousUser the object containing the previous metadata of the user
     * @param previousUser the object containing the new metadata of the user
     */
    void modifyUser(BaseObject previousUser, BaseObject newUser) throws UserSyncException;

    /**
     * @param deletedUser the object containing the metadata of the deleted user
     */
    void deleteUser(BaseObject deletedUser) throws UserSyncException;
}

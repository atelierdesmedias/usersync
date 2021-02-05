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
package org.xwiki.contrib.usersync.interval;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.usersync.UserSyncConnector;
import org.xwiki.contrib.usersync.UserSyncException;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.observation.AbstractEventListener;
import org.xwiki.observation.event.Event;

import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.internal.event.XObjectAddedEvent;
import com.xpn.xwiki.internal.event.XObjectDeletedEvent;
import com.xpn.xwiki.internal.event.XObjectEvent;
import com.xpn.xwiki.internal.event.XObjectUpdatedEvent;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.BaseObjectReference;

/**
 * Listed to user modification and call the registered user synchronization connectors.
 * 
 * @version $Id$
 */
@Component
@Singleton
@Named(UserSyncListener.NAME)
public class UserSyncListener extends AbstractEventListener
{
    public static final String NAME = "usersync";

    private static final String USERS_CLASSNAME = "XWiki.XWikiUsers";

    private static final EntityReference USER_OBJECT_REFERENCE = BaseObjectReference.any(USERS_CLASSNAME);

    @Inject
    private List<UserSyncConnector> connectors;

    /**
     * Default constructor.
     */
    public UserSyncListener()
    {
        super(NAME, new XObjectAddedEvent(USER_OBJECT_REFERENCE), new XObjectDeletedEvent(USER_OBJECT_REFERENCE),
            new XObjectUpdatedEvent(USER_OBJECT_REFERENCE));
    }

    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        for (UserSyncConnector connector : this.connectors) {
            onEvent(connector, (XObjectEvent) event, (XWikiDocument) source);
        }
    }

    private void onEvent(UserSyncConnector connector, XObjectEvent event, XWikiDocument document)
    {
        BaseObject newUser = document.getXObject(event.getReference());
        BaseObject previousUser = document.getOriginalDocument().getXObject(event.getReference());

        try {
            if (event instanceof XObjectAddedEvent) {
                connector.createUser(newUser);
            } else if (event instanceof XObjectDeletedEvent) {
                connector.deleteUser(previousUser);
            } else {
                connector.modifyUser(previousUser, newUser);
            }
        } catch (UserSyncException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

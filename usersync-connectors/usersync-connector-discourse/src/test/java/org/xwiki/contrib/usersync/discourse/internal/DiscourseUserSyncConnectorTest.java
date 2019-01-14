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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.contrib.usersync.UserSyncConnector;
import org.xwiki.contrib.usersync.UserSyncException;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.LocalDocumentReference;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.test.MockitoOldcoreRule;
import com.xpn.xwiki.test.reference.ReferenceComponentList;

/**
 * @version $Id$
 */
@ReferenceComponentList
public class DiscourseUserSyncConnectorTest
{
    public MockitoComponentMockingRule<UserSyncConnector> mocker =
        new MockitoComponentMockingRule<>(DiscourseUserSyncConnector.class);

    @Rule
    public MockitoOldcoreRule oldcore = new MockitoOldcoreRule(mocker);

    private BaseObject newUser;

    private BaseObject previousUser;

    @Before
    public void before()
    {
        XWikiDocument userDocument = new XWikiDocument(new DocumentReference("wiki", "XWiki", "UserLogin"));

        this.newUser = new BaseObject();
        this.newUser.setXClassReference(new LocalDocumentReference("XWiki", "XWikiUsers"));

        this.newUser.setStringValue("id", "SergeGainsbourg");
        this.newUser.setStringValue("email", "martin@phonations.com");
        this.newUser.setStringValue("password", "abcdefgh1234");
        this.newUser.setStringValue("name", "Serge Gainsbourg");

        userDocument.addXObject(this.newUser);

        this.previousUser = this.newUser.clone();

        userDocument.addXObject(this.previousUser);
    }

    @Test
    public void getUser() throws ComponentLookupException
    {
        try {
            // Call the component
            this.mocker.getComponentUnderTest().getUser("ThomasMortagne");
        } catch (UserSyncException exception) {
            Assert.fail(exception.getMessage());
        }
    }

    @Test
    public void createUser() throws ComponentLookupException
    {
        try {
            // Call the component
            this.mocker.getComponentUnderTest().createUser(this.newUser);
        } catch (UserSyncException exception) {
            Assert.fail(exception.getMessage());
        }
    }

    public void modifyUser() throws ComponentLookupException
    {
        // Modify the user
        this.newUser.setStringValue("email", "differentmail@domain.com");

        try {
            // Call the component
            this.mocker.getComponentUnderTest().modifyUser(this.previousUser, this.newUser);
        } catch (UserSyncException exception) {
            Assert.fail(exception.getMessage());
        }
    }

    @Test
    public void deleteUser() throws ComponentLookupException
    {
        try {
            // Call the component
            this.mocker.getComponentUnderTest().deleteUser(this.newUser);
        } catch (UserSyncException exception) {
            Assert.fail(exception.getMessage());
        }
    }
}

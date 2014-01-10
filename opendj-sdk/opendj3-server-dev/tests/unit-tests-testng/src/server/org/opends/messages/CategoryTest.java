/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2006-2008 Sun Microsystems, Inc.
 *      Portions Copyright 2014 ForgeRock AS
 */

package org.opends.messages;

import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 * Category Tester.
 *
 */
public class CategoryTest extends MessagesTestCase
{

    @DataProvider(name = "messageDescriptors")
    public Object[][] getMessageDescriptors() {
      return new Object[][] {
              {CoreMessages.ERR_ABANDON_OP_NO_SUCH_OPERATION}
      };
    }

    @Test(dataProvider = "messageDescriptors")
    public void testParseMessageId(MessageDescriptor md)
    {
      assertEquals(md.getCategory(), Category.parseMessageId(md.getId()));
    }

    @Test(dataProvider = "messageDescriptors")
    public void testParseMask(MessageDescriptor md)
    {
      assertEquals(md.getCategory(), Category.parseMask(md.getMask()));
    }

    @Test
    public void testGetMask()
    {
      assertNotNull(Category.ACCESS_CONTROL.getMask());
    }

}

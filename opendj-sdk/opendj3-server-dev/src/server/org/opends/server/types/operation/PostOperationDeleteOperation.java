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
package org.opends.server.types.operation;



import org.forgerock.opendj.ldap.ByteString;
import org.opends.server.types.DN;
import org.opends.server.types.Entry;



/**
 * This class defines a set of methods that are available for use by
 * post-operation plugins for delete operations.  Note that this
 * interface is intended only to define an API for use by plugins and
 * is not intended to be implemented by any custom classes.
 */
@org.opends.server.types.PublicAPI(
     stability=org.opends.server.types.StabilityLevel.UNCOMMITTED,
     mayInstantiate=false,
     mayExtend=false,
     mayInvoke=true)
public interface PostOperationDeleteOperation
       extends PostOperationOperation
{
  /**
   * Retrieves the raw, unprocessed entry DN as included in the client
   * request.
   *
   * @return  The raw, unprocessed entry DN as included in the client
   *          request.
   */
  public ByteString getRawEntryDN();



  /**
   * Retrieves the DN of the entry to delete.
   *
   * @return  The DN of the entry to delete.
   */
  public DN getEntryDN();



  /**
   * Retrieves the entry to be deleted.
   *
   * @return  The entry to be deleted.
   */
  public Entry getEntryToDelete();
}


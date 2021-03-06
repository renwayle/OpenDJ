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
 *      Portions Copyright 2015 ForgeRock AS
 */
package org.opends.server.tools;




/**
 * This class defines options for all the modify operations used
 * by the ldapmodify tool.
 */
public class LDAPModifyOptions extends LDAPToolOptions
{

  private boolean defaultAdd;

  /**
   * Creates the options instance.
   *
   */
  public LDAPModifyOptions()
  {
  }

  /**
   * Set whether to default to adding entries if no changetype is provided.
   *
   * @param  defaultAdd  If entries with no changetype should be considered add
   *                     requests.
   */
  public void setDefaultAdd(boolean defaultAdd)
  {
    this.defaultAdd = defaultAdd;
  }

  /**
   * Get the value of the defaultAdd flag.
   *
   * @return  <CODE>true</CODE> if a default changetype of "add" should be used,
   *          or <CODE>false</CODE> if not.
   */
  public boolean getDefaultAdd()
  {
    return defaultAdd;
  }
}


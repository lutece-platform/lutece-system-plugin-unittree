/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.unittree.business.action;

import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.rbac.RBACAction;

import java.util.Locale;


/**
 *
 * IAction
 *
 */
public interface IAction extends Localizable, RBACAction
{
    /**
     * Set the id action
     * @param nIdAction the nIdAction to set
     */
    void setIdAction( int nIdAction );

    /**
     * Get the id action
     * @return the nIdAction
     */
    int getIdAction(  );

    /**
    * Implements Localizable
    * @param locale The current locale
    */
    void setLocale( Locale locale );

    /**
     * Returns the Url
     * @return The Url
     */
    String getUrl(  );

    /**
     * Sets the Url
     * @param strUrl The Url
     */
    void setUrl( String strUrl );

    /**
     * Returns the NameKey
     * @return The NameKey
     */
    String getNameKey(  );

    /**
     * Returns the Name
     * @return The Name
     */
    String getName(  );

    /**
     * Sets the NameKey
     * @param strNameKey The NameKey
     */
    void setNameKey( String strNameKey );

    /**
     * Returns the DescriptionKey
     * @return The DescriptionKey
     */
    String getDescriptionKey(  );

    /**
     * Returns the Description
     * @return The Description
     */
    String getDescription(  );

    /**
     * Sets the DescriptionKey
     * @param strDescriptionKey The DescriptionKey
     */
    void setDescriptionKey( String strDescriptionKey );

    /**
     * Returns the IconUrl
     * @return The IconUrl
     */
    String getIconUrl(  );

    /**
     * Sets the IconUrl
     * @param strIconUrl The IconUrl
     */
    void setIconUrl( String strIconUrl );

    /**
     * Returns the permission associated to the action
     * @return The permission
     */
    String getPermission(  );

    /**
     * Sets the Permission
     * @param strPermission The Permission
     */
    void setPermission( String strPermission );

    /**
     * Get the action type
     * @return the action type
     */
    String getActionType(  );

    /**
     * Set the action type
     * @param strActionType the action type
     */
    void setActionType( String strActionType );
}

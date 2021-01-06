/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;

/**
 *
 * AbstractAction
 *
 */
public abstract class AbstractAction implements IAction
{
    private int _nIdAction;
    private String _strUrl;
    private String _strNameKey;
    private String _strDescriptionKey;
    private Locale _locale;
    private String _strIcon;
    private String _strPermission;

    /**
     * Set the id action
     * 
     * @param nIdAction
     *            the nIdAction to set
     */
    public void setIdAction( int nIdAction )
    {
        this._nIdAction = nIdAction;
    }

    /**
     * Get the id action
     * 
     * @return the nIdAction
     */
    public int getIdAction( )
    {
        return _nIdAction;
    }

    /**
     * Implements Localizable
     * 
     * @param locale
     *            The current locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Return the Url
     * 
     * @return The Url
     */
    public String getUrl( )
    {
        return _strUrl;
    }

    /**
     * Set the Url
     * 
     * @param strUrl
     *            The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the NameKey
     * 
     * @return The NameKey
     */
    public String getNameKey( )
    {
        return _strNameKey;
    }

    /**
     * Returns the Name
     * 
     * @return The Name
     */
    public String getName( )
    {
        return I18nService.getLocalizedString( _strNameKey, _locale );
    }

    /**
     * Sets the NameKey
     * 
     * @param strNameKey
     *            The NameKey
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = strNameKey;
    }

    /**
     * Returns the DescriptionKey
     * 
     * @return The DescriptionKey
     */
    public String getDescriptionKey( )
    {
        return _strDescriptionKey;
    }

    /**
     * Returns the Description
     * 
     * @return The Description
     */
    public String getDescription( )
    {
        return I18nService.getLocalizedString( _strDescriptionKey, _locale );
    }

    /**
     * Sets the DescriptionKey
     * 
     * @param strDescriptionKey
     *            The DescriptionKey
     */
    public void setDescriptionKey( String strDescriptionKey )
    {
        _strDescriptionKey = strDescriptionKey;
    }

    /**
     * Returns the icon
     * 
     * @return The icon
     */
    public String getIcon( )
    {
        return _strIcon;
    }

    /**
     * Sets the icon
     * 
     * @param strIcon
     *            The icon
     */
    public void setIcon( String strIcon )
    {
        _strIcon = strIcon;
    }

    /**
     * Returns the permission associated to the action
     * 
     * @return The permission
     */
    public String getPermission( )
    {
        return _strPermission;
    }

    /**
     * Sets the Permission
     * 
     * @param strPermission
     *            The Permission
     */
    public void setPermission( String strPermission )
    {
        _strPermission = strPermission;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + _nIdAction;

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        if ( getClass( ) != obj.getClass( ) )
        {
            return false;
        }

        AbstractAction other = (AbstractAction) obj;

        return _nIdAction == other._nIdAction;
    }
}

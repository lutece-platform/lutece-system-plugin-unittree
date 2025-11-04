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
package fr.paris.lutece.plugins.unittree.business.unit;

import fr.paris.lutece.portal.service.rbac.RBACResource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

/**
 *
 * Unit
 *
 */
public class Unit implements RBACResource, Serializable
{
    public static final String RESOURCE_TYPE = "UNIT_TYPE";
    public static final int ID_ROOT = 0;
    public static final int ID_NULL = -1;
    private int _nIdUnit;
    @NotNull
    private int _nIdParent;
    @NotNull
    private String _strCode;
    @NotNull
    private String _strLabel;
    @NotNull
    private String _strDescription;
    private Map<String, IUnitAttribute<?>> _mapAttributes = new HashMap<>( );

    /**
     * Get the id unit
     * 
     * @return the id unit
     */
    public int getIdUnit( )
    {
        return _nIdUnit;
    }

    /**
     * Set the id unit
     * 
     * @param nIdUnit
     *            the id unit
     */
    public void setIdUnit( int nIdUnit )
    {
        _nIdUnit = nIdUnit;
    }

    /**
     * Get the code
     * 
     * @return the code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Set the code
     * 
     * @param strCode
     *            the code
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Get the label
     * 
     * @return the label
     */
    public String getLabel( )
    {
        return _strLabel;
    }

    /**
     * Set the label
     * 
     * @param strLabel
     *            the label
     */
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    /**
     * Get the description
     * 
     * @return the description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Set the description
     * 
     * @param strDescription
     *            the description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Set the id parent
     * 
     * @param nIdParent
     *            the nIdParent to set
     */
    public void setIdParent( int nIdParent )
    {
        this._nIdParent = nIdParent;
    }

    /**
     * Get the id parent
     * 
     * @return the nIdParent
     */
    public int getIdParent( )
    {
        return _nIdParent;
    }

    /**
     * Check if the unit is the root unit
     * 
     * @return true if the unit is the root unit, false otherwise
     */
    public boolean isRoot( )
    {
        return _nIdUnit == ID_ROOT;
    }

    /**
     * Get the attribute from a given key
     * 
     * @param <T>
     *            the class of the attribute
     * @param strKey
     *            the key of the attribute
     * @return the attribute
     */
    public <T> IUnitAttribute<T> getAttribute( String strKey )
    {
        return (IUnitAttribute<T>) _mapAttributes.get( strKey );
    }

    /**
     * Add an attribute
     * 
     * @param attribute
     *            the attribute to add
     */
    public void addAttribute( IUnitAttribute<?> attribute )
    {
        _mapAttributes.put( attribute.getAttributeName( ), attribute );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceId( )
    {
        return Integer.toString( _nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        return _nIdUnit;
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

        Unit unit = (Unit) obj;
        return _nIdUnit == unit.getIdUnit( );
    }
}

/*
 * Copyright (c) 2002-2020, City of Paris
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

import org.apache.commons.lang.StringUtils;

/**
 *
 * UnitFilter
 *
 */
public class UnitFilter
{
    private int _nIdParent;
    private String _strLabel;
    private String _strDescription;
    private boolean _bIsWideSearch;

    /**
     * Constructor
     */
    public UnitFilter( )
    {
        _nIdParent = Unit.ID_NULL;
        _strLabel = StringUtils.EMPTY;
        _strDescription = StringUtils.EMPTY;
        _bIsWideSearch = false;
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
        this._strLabel = strLabel;
    }

    /**
     * Check if the filter contains the label
     * 
     * @return true if it contains, false otherwise
     */
    public boolean containsLabel( )
    {
        return StringUtils.isNotBlank( _strLabel );
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
        this._strDescription = strDescription;
    }

    /**
     * Check if the filter contains the description
     * 
     * @return true if it contains, false otherwise
     */
    public boolean containsDescription( )
    {
        return StringUtils.isNotBlank( _strDescription );
    }

    /**
     * Get the id parent
     * 
     * @return the id parent
     */
    public int getIdParent( )
    {
        return _nIdParent;
    }

    /**
     * Set the id parent
     * 
     * @param nIdParent
     *            the reference code
     */
    public void setIdParent( int nIdParent )
    {
        this._nIdParent = nIdParent;
    }

    /**
     * Check if the filter contains the id parent
     * 
     * @return true if the it contains, false otherwise
     */
    public boolean containsIdParent( )
    {
        return _nIdParent != -1;
    }

    /**
     * Set true if the search is wide, false otherwise
     * 
     * @param isWideSearch
     *            true if the search is wide, false otherwise
     */
    public void setWideSearch( boolean isWideSearch )
    {
        this._bIsWideSearch = isWideSearch;
    }

    /**
     * Return true if the search is wide, false otherwise
     * 
     * @return true if the search is wide, false otherwise
     */
    public boolean isWideSearch( )
    {
        return _bIsWideSearch;
    }
}

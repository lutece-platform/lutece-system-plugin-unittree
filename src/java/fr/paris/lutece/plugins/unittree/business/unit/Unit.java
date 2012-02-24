/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;


/**
 *
 * Unit
 *
 */
public class Unit implements RBACResource
{
    public static final String RESOURCE_TYPE = "UNIT_TYPE";
    public static final int ID_ROOT = 0;
    public static final int ID_NULL = -1;
    private int _nIdUnit;
    @NotNull
    private int _nIdParent;
    @NotBlank
    private String _strLabel;
    @NotBlank
    private String _strDescription;
    private List<Integer> _listIdsSector = new ArrayList<Integer>(  );

    /**
     * Get the id unit
     * @return the id unit
     */
    public int getIdUnit(  )
    {
        return _nIdUnit;
    }

    /**
     * Set the id unit
     * @param nIdUnit the id unit
     */
    public void setIdUnit( int nIdUnit )
    {
        _nIdUnit = nIdUnit;
    }

    /**
     * Get the label
     * @return the label
     */
    public String getLabel(  )
    {
        return _strLabel;
    }

    /**
     * Set the label
     * @param strLabel the label
     */
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    /**
     * Get the description
     * @return the description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Set the description
     * @param strDescription the description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Set the id parent
     * @param nIdParent the nIdParent to set
     */
    public void setIdParent( int nIdParent )
    {
        this._nIdParent = nIdParent;
    }

    /**
     * Get the id parent
     * @return the nIdParent
     */
    public int getIdParent(  )
    {
        return _nIdParent;
    }

    /**
     * Set the list of ids sector
     * @param listIdsSector the list of ids sector
     */
    public void setListIdsSector( List<Integer> listIdsSector )
    {
        this._listIdsSector = listIdsSector;
    }

    /**
     * Get the list of ids sector
     * @return the list of ids sector
     */
    public List<Integer> getListIdsSector(  )
    {
        return _listIdsSector;
    }

    /**
     * Add the id sector
     * @param nIdSector the id sector
     */
    public void addIdSector( int nIdSector )
    {
        if ( _listIdsSector == null )
        {
            _listIdsSector = new ArrayList<Integer>(  );
        }

        _listIdsSector.add( nIdSector );
    }

    /**
     * Check if the unit has the given id sector
     * @param nIdSector the id sector
     * @return true if the unit has the id sector, false otherwise
     */
    public boolean hasIdSector( int nIdSector )
    {
        if ( ( _listIdsSector != null ) && !_listIdsSector.isEmpty(  ) )
        {
            return _listIdsSector.contains( nIdSector );
        }

        return false;
    }

    /**
     * Check if the unit is the root unit
     * @return true if the unit is the root unit, false otherwise
     */
    public boolean isRoot(  )
    {
        return _nIdUnit == ID_ROOT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceId(  )
    {
        return Integer.toString( _nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }
}

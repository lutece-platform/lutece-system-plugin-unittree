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

import javax.validation.constraints.NotNull;


/**
 *
 *
 */
public class Unit implements RBACResource
{
    public static final String RESOURCE_TYPE = "UNIT_TYPE";
    public static final int ID_ROOT = 0;
    private int _nIdUnit;
    @NotNull
    private int _nIdParent;
    @NotBlank
    private String _strLabel;
    @NotBlank
    private String _strDescription;

    public int getIdUnit(  )
    {
        return _nIdUnit;
    }

    public void setIdUnit( int nIdUnit )
    {
        _nIdUnit = nIdUnit;
    }

    public String getLabel(  )
    {
        return _strLabel;
    }

    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    public String getDescription(  )
    {
        return _strDescription;
    }

    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     *
     * @param nIdParent the _nIdParent to set
     */
    public void setIdParent( int nIdParent )
    {
        this._nIdParent = nIdParent;
    }

    /**
     * @return the _nIdParent
     */
    public int getIdParent(  )
    {
        return _nIdParent;
    }

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

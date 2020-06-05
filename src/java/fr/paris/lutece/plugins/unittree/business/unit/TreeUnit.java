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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * UnitFilter
 *
 */
public class TreeUnit
{
    private Unit _unitNode;
    private List<TreeUnit> _subUnits;

    /**
     * constructor
     */
    public TreeUnit( )
    {
        _subUnits = new ArrayList<>( );
    }

    /**
     * constructor
     */
    public TreeUnit( Unit unit )
    {
        _unitNode = unit;
        _subUnits = new ArrayList<>( );
    }

    /**
     * get the unit node
     * 
     * @return the unit
     */
    public Unit getUnitNode( )
    {
        return _unitNode;
    }

    /**
     * set unit node
     * 
     * @param unitNode
     */
    public void setUnitNode( Unit unitNode )
    {
        this._unitNode = unitNode;
    }

    /**
     * get sub units
     * 
     * @return list of TreeUnit
     */
    public List<TreeUnit> getSubUnits( )
    {
        return _subUnits;
    }

    /**
     * set sub units
     * 
     * @param subUnits
     */
    public void setSubUnits( List<TreeUnit> subUnits )
    {
        this._subUnits = subUnits;
    }

    /**
     * add sub unit
     * 
     * @param subUnit
     */
    public void addSubUnit( Unit subUnit )
    {
        TreeUnit subTreeUnit = new TreeUnit( subUnit );
        this._subUnits.add( subTreeUnit );
    }
}

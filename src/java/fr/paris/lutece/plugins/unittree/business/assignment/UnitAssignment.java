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
package fr.paris.lutece.plugins.unittree.business.assignment;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import java.sql.Timestamp;

/**
 * This class represents an assignment to a unit
 */
public class UnitAssignment
{
    private static final int UNIT_DEFAULT_ID = -1;

    private int _nId;
    private int _nIdResource;
    private String _strResourceType;
    private Unit _assignedUnit;
    private Unit _assignorUnit;
    private Timestamp _dateAssignmentDate;
    private UnitAssignmentType _assignmentType;
    private boolean _bIsActive;

    /**
     * Constructor
     */
    public UnitAssignment( )
    {
        this._assignedUnit = new Unit( );
        this._assignorUnit = new Unit( );

        _assignedUnit.setIdUnit( UNIT_DEFAULT_ID );
        _assignorUnit.setIdUnit( UNIT_DEFAULT_ID );
    }

    /**
     * Gives the id
     * 
     * @return The id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the id
     * 
     * @param nId
     *            The id to set
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gives the resource id
     * 
     * @return The resource id
     */
    public int getIdResource( )
    {
        return _nIdResource;
    }

    /**
     * Sets the resource id
     * 
     * @param nIdResource
     *            The resource id to set
     */
    public void setIdResource( int nIdResource )
    {
        _nIdResource = nIdResource;
    }

    /**
     * Gives the resource type
     * 
     * @return The resource type
     */
    public String getResourceType( )
    {
        return _strResourceType;
    }

    /**
     * Sets the resource type
     * 
     * @param strResourceType
     *            The resource type to set
     */
    public void setResourceType( String strResourceType )
    {
        _strResourceType = strResourceType;
    }

    /**
     * Gives the assigned unit
     * 
     * @return The assigned unit
     */
    public Unit getAssignedUnit( )
    {
        return _assignedUnit;
    }

    /**
     * Sets the assigned unit
     * 
     * @param assignedUnit
     *            The assigned unit to set
     */
    public void setAssignedUnit( Unit assignedUnit )
    {
        _assignedUnit = assignedUnit;
    }

    /**
     * Gives the assignor unit
     * 
     * @return The assignor unit
     */
    public Unit getAssignorUnit( )
    {
        return _assignorUnit;
    }

    /**
     * Sets the assignor unit
     * 
     * @param assignorUnit
     *            The assignor unit to set
     */
    public void setAssignorUnit( Unit assignorUnit )
    {
        _assignorUnit = assignorUnit;
    }

    /**
     * Gives the assignment date
     * 
     * @return The assignment date
     */
    public Timestamp getAssignmentDate( )
    {
        return _dateAssignmentDate;
    }

    /**
     * Sets the assignment date
     * 
     * @param dateAssignmentDate
     *            The assignment date to set
     */
    public void setAssignmentDate( Timestamp dateAssignmentDate )
    {
        _dateAssignmentDate = dateAssignmentDate;
    }

    /**
     * Gives the assignment type
     * 
     * @return the assignment type
     */
    public UnitAssignmentType getAssignmentType( )
    {
        return _assignmentType;
    }

    /**
     * Sets the assignment type
     * 
     * @param assignmentType
     *            the assignment type to set
     */
    public void setAssignmentType( UnitAssignmentType assignmentType )
    {
        this._assignmentType = assignmentType;
    }

    /**
     * Tests if the assignment is active or not
     * 
     * @return {@code true} if the assignment is active, {@code false} otherwise
     */
    public boolean isActive( )
    {
        return _bIsActive;
    }

    /**
     * Sets if the assignment is active or not
     * 
     * @param bIsActive
     *            {@code true} if the assignment is active, {@code false} otherwise
     */
    public void setActive( boolean bIsActive )
    {
        _bIsActive = bIsActive;
    }

    /**
     * Gives the assigned unit id
     * 
     * @return The assigned unit id
     */
    public int getIdAssignedUnit( )
    {
        return _assignedUnit.getIdUnit( );
    }

    /**
     * Sets the assigned unit id
     * 
     * @param nIdAssignedUnit
     *            The assigned unit id
     */
    public void setIdAssignedUnit( int nIdAssignedUnit )
    {
        _assignedUnit.setIdUnit( nIdAssignedUnit );
    }

    /**
     * Gives the assignor unit id
     * 
     * @return The assignor unit id
     */
    public int getIdAssignorUnit( )
    {
        return _assignorUnit.getIdUnit( );
    }

    /**
     * Sets the assignor unit id
     * 
     * @param nIdAssignorUnit
     *            The assignor unit id
     */
    public void setIdAssignorUnit( int nIdAssignorUnit )
    {
        _assignorUnit.setIdUnit( nIdAssignorUnit );
    }

}

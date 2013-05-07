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
package fr.paris.lutece.plugins.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IUnitAttributeService
 *
 */
public interface IUnitAttributeService
{
    /**
    * Do create the additional attributes of the given unit
    * @param unit the unit
    * @param request the HTTP request
    */
    void doCreateUnit( Unit unit, HttpServletRequest request );

    /**
     * Do modify the additional attributes of the given unit
     * @param unit the unit
     * @param request the HTTP request
     */
    void doModifyUnit( Unit unit, HttpServletRequest request );

    /**
     * Do remove the additional attributes
     * @param nIdUnit the id unit
     * @param request the HTTP request
     */
    void doRemoveUnit( int nIdUnit, HttpServletRequest request );

    /**
     * Populate an unit additionnal attributes with the data of the <strong>database</strong>
     * @param unit the unit to populate
     */
    void populate( Unit unit );

    /**
     * Populate an unit additionnal attributes with the data of the <strong>request</strong>
     * @param unit the unit to populate
     * @param request the HTTP request
     * @throws UnitErrorException exception if there is an error (ex: mandatory field)
     */
    void populate( Unit unit, HttpServletRequest request )
        throws UnitErrorException;

    /**
     * Check if the given id unit can creabe sub units
     * @param nIdUnit the id unit
     * @return true if the unit can create sub unit
     */
    boolean canCreateSubUnit( int nIdUnit );

    /**
     * Update attributes to change the parent of a unit.
     * @param unitToMove The unit to change the parent of.
     * @param newUnitParent The new parent of the unit
     */
    void moveSubTree( Unit unitToMove, Unit newUnitParent );
}

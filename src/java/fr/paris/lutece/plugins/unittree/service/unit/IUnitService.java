/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.unittree.business.action.IAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import javax.xml.transform.Source;


/**
 *
 * IUnitService
 *
 */
public interface IUnitService
{
    String BEAN_UNIT_SERVICE = "unittree.unitService";

    // GET

    /**
     * Get the unit
     * @param nIdUnit the id unit
     * @param bGetIdsSector true if it must get the ids sector
     * @return an instance of {@link Unit}
     */
    Unit getUnit( int nIdUnit, boolean bGetIdsSector );

    /**
     * Get the root unit
     * @param bGetIdsSector true if it must get the ids sector
     * @return an instance of {@link Unit}
     */
    Unit getRootUnit( boolean bGetIdsSector );

    /**
     * Get the unit by id user
     * @param nIdUser the id user
     * @param bGetSectors true if it must get the ids sector
     * @return an instance of {@link Unit}
     */
    Unit getUnitByIdUser( int nIdUser, boolean bGetSectors );

    /**
     * Get all units
     * @param bGetIdsSector true if it must get the ids sector
     * @return a list of {@link Unit}
     */
    List<Unit> getAllUnits( boolean bGetIdsSector );

    /**
     * Get the units first level
     * @param bGetIdsSector true if it must get the ids sector
     * @return a list of {@link Unit}
     */
    List<Unit> getUnitsFirstLevel( boolean bGetIdsSector );

    /**
     * Get the sub units from a given id unit
     * @param nIdUnit the id unit
     * @param bGetIdsSector true if it must get the ids sector
     * @return a list of {@link Unit}
     */
    List<Unit> getSubUnits( int nIdUnit, boolean bGetIdsSector );

    /**
     * Get the list of actions
     * @param strActionType the action type
     * @param locale the locale
     * @param unit the unit
     * @param user the user
     * @return a list of {@link IAction}
     */
    List<IAction> getListActions( String strActionType, Locale locale, Unit unit, AdminUser user );

    /**
     * Get the sub units as a {@link ReferenceList}
     * @param nIdUnit the id unit
     * @param locale the locale
     * @return a {@link ReferenceList}
     */
    ReferenceList getSubUnitsAsReferenceList( int nIdUnit, Locale locale );

    /**
     * Get the XML units
     * @return an XML
     */
    String getXMLUnits(  );

    /**
     * Get the XSL of the tree
     * @return the XSL
     */
    Source getTreeXsl(  );

    // CHECKS

    /**
     * Check if the given id unit has sub units
     * @param nIdUnit the id unit
     * @return true if the unit has sub units, false otherwise
     */
    boolean hasSubUnits( int nIdUnit );

    /**
     * Check if the first unit in parameter is parent of the second
     * unit in parameter
     * @param unitParent the unit parent ?
     * @param unitRef of the unit ?
     * @return true if there is a parent link between those two units
     */
    boolean isParent( Unit unitParent, Unit unitRef );

    /**
     * Check if the given id unit, we can create sub unit.
     * <br />
     * Return false if the unit does not have sub units and has sectors
     * @param nIdUnit the id unit
     * @return true if we can create sub unit, false otherwise
     */
    boolean canCreateSubUnit( int nIdUnit );

    // CRUD OPERATIONS

    /**
    * Create a unit
    * @param unit the unit
    * @return the id unit
    */
    @Transactional( "unittree.transactionManager" )
    int createUnit( Unit unit );

    /**
     * Update the unit
     * @param unit the unit
     */
    @Transactional( "unittree.transactionManager" )
    void updateUnit( Unit unit );

    /**
     * Remove the unit only if the unit does not have sub units
     * @param nIdUnit the id unit
     */
    @Transactional( "unittree.transactionManager" )
    void removeUnit( int nIdUnit );
}

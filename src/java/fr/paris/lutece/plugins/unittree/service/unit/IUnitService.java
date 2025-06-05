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
package fr.paris.lutece.plugins.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.business.action.IAction;
import fr.paris.lutece.plugins.unittree.business.unit.TreeUnit;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.rbac.UnittreeRBACRecursiveType;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceList;
import java.util.Collection;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import javax.xml.transform.Source;

/**
 *
 * IUnitService
 *
 */
public interface IUnitService
{
    // GET

    /**
     * Get the unit
     * 
     * @param nIdUnit
     *            the id unit
     * @param bGetAdditionalInfos
     *            true if it must get the additional infos
     * @return an instance of {@link Unit}
     */
    Unit getUnit( int nIdUnit, boolean bGetAdditionalInfos );

    /**
     * Get the unit by code
     * 
     * @param strCode
     *            the unit code
     * @param bGetAdditionalInfos
     *            true if it must get the additional infos
     * @return an instance of {@link Unit}
     */
    Unit getUnitByCode( String strCode, boolean bGetAdditionalInfos );

    /**
     * Get the root unit
     * 
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return an instance of {@link Unit}
     */
    Unit getRootUnit( boolean bGetAdditionalInfos );

    /**
     * Get the unit by id user
     * 
     * @param nIdUser
     *            the id user
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return a list of {@link Unit}
     */
    List<Unit> getUnitsByIdUser( int nIdUser, boolean bGetAdditionalInfos );

    /**
     * Get all units
     * 
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return a list of {@link Unit}
     */
    List<Unit> getAllUnits( boolean bGetAdditionalInfos );

    /**
     * Get the units first level
     * 
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return a list of {@link Unit}
     */
    List<Unit> getUnitsFirstLevel( boolean bGetAdditionalInfos );

    /**
     * Get the direct sub units from a given id unit
     * 
     * @param nIdUnit
     *            the id unit
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return a list of {@link Unit}
     */
    List<Unit> getSubUnits( int nIdUnit, boolean bGetAdditionalInfos );

    /**
     * Get the unit parent list, from a given unit. The unit itselft is included
     * 
     * @param unit
     *            the unit
     * @return the list of parent units.
     */
    List<Unit> getListParentUnits( Unit unit );

    /**
     * Get all the sub units in the unit tree from the specified unit
     * 
     * @param unit
     *            the unit
     * @param bGetAdditionalInfos
     *            true if it must get the additionnal infos
     * @return a list of {@link Unit}
     */
    List<Unit> getAllSubUnits( Unit unit, boolean bGetAdditionalInfos );

    /**
     * Get the list of actions
     * 
     * @param strActionType
     *            the action type
     * @param locale
     *            the locale
     * @param unit
     *            the unit
     * @param user
     *            the user
     * @param recursiveType
     *            the recursive type
     * @return a list of {@link IAction}
     */
    List<IAction> getListActions( String strActionType, Locale locale, Unit unit, AdminUser user, UnittreeRBACRecursiveType recursiveType );

    /**
     * Get the direct sub units as a {@link ReferenceList}
     * 
     * @param nIdUnit
     *            the id unit
     * @param locale
     *            the locale
     * @return a {@link ReferenceList}
     */
    ReferenceList getSubUnitsAsReferenceList( int nIdUnit, Locale locale );

    /**
     * Get the XML units
     * 
     * @param user
     *            the admin user
     * @return an XML
     */
    String getXMLUnits( AdminUser user );

    /**
     * Get the XSL of the tree
     * 
     * @return the XSL
     */
    Source getTreeXsl( );

    /**
     * Return all the Unit with no children (level 0)
     * 
     * @return all the Unit with no children (level 0)
     */
    List<Unit> getUnitWithNoChildren( );

    // CHECKS

    /**
     * Check if the given id unit has sub units
     * 
     * @param nIdUnit
     *            the id unit
     * @return true if the unit has sub units, false otherwise
     */
    boolean hasSubUnits( int nIdUnit );

    /**
     * Check if the first unit in parameter is parent of the second unit in parameter
     * 
     * @param unitParent
     *            the parent unit
     * @param unitRef
     *            the unit to check
     * @return true if there is a parent link between those two units
     */
    boolean isParent( Unit unitParent, Unit unitRef );

    /**
     * Check if the given id unit, we can create sub unit. <br />
     * Return false if the unit can not be created
     * 
     * @param nIdUnit
     *            the id unit
     * @return true if we can create sub unit, false otherwise
     */
    boolean canCreateSubUnit( int nIdUnit );

    /**
     * Check if the given user is authorized to do the action for a given unit. <br />
     * This method calls the service RBACService to check the permission. However, the resource on which the permission must be checked is not necessarily the
     * given unit. It may be a parent unit of the given unit. <br />
     * There are 5 cases to handle :
     * <ul>
     * <li>1) The given user is admin => The user is always authorized</li>
     * <li>2) The given user does not belong to any unit => The user is not authorized</li>
     * <li>3) The given user belongs to the given unit => Check with RBACService with the given unit</li>
     * <li>4) The given user belongs to a parent unit of the given unit => Check with RBACService with the parent unit</li>
     * <li>5) The given user belongs to an unit who is not a parent unit of the given unit => The user is not authorized</li>
     * </ul>
     * 
     * @param unit
     *            the unit
     * @param strPermission
     *            the permission
     * @param user
     *            the user
     * @param recursiveType
     *            the recursive type of RBAC
     * @return true if he is authorized, false otherwise
     */
    boolean isAuthorized( Unit unit, String strPermission, AdminUser user, UnittreeRBACRecursiveType recursiveType );

    /**
     * See {@link #isAuthorized(Unit, String, AdminUser)}
     * 
     * @param strIdResource
     *            the id resource
     * @param strPermission
     *            the permission
     * @param user
     *            the user
     * @param recursiveType
     *            the recursive type of RBAC
     * @return true if he is authorized, false otherwise
     */
    boolean isAuthorized( String strIdResource, String strPermission, AdminUser user, UnittreeRBACRecursiveType recursiveType );

    // CRUD OPERATIONS

    /**
     * Create a unit
     * 
     * @param unit
     *            the unit
     * @param request
     *            the HTTP request
     * @return the id unit
     * @throws UnitErrorException
     *             exception if there is an application error
     */
    @Transactional
    int createUnit( Unit unit, HttpServletRequest request ) throws UnitErrorException;

    /**
     * Update the unit
     * 
     * @param unit
     *            the unit
     * @param request
     *            the HTTP request
     * @throws UnitErrorException
     *             exception if there is an application error
     */
    @Transactional
    void updateUnit( Unit unit, HttpServletRequest request ) throws UnitErrorException;

    /**
     * Remove the unit only if the unit does not have sub units
     * 
     * @param nIdUnit
     *            the id unit
     * @param request
     *            the HTTP request
     */
    @Transactional
    void removeUnit( int nIdUnit, HttpServletRequest request );

    /**
     * Change the parent of a unit.
     * 
     * @param unitToMove
     *            The unit to change the parent of.
     * @param newUnitParent
     *            The new parent of the unit
     * @return True if the sub tree was successfully move, false if the new parent is a child of the unit.
     */
    @Transactional
    boolean moveSubTree( Unit unitToMove, Unit newUnitParent );

    /**
     * Check if an unit is in a given list of unit
     * 
     * @param unitToCheck
     *            the unit to check
     * @param listUnits
     *            the list of units
     * @return true if the unit is in the given list, false otherwise
     */
    boolean isUnitInList( Unit unitToCheck, Collection<Unit> listUnits );

    /**
     * populate a TreeUnit with all sub units (recursive)
     * 
     * @param treeUnit
     *            the treeUnit
     * @param user
     *            the admin user (for RBAC)
     * @param bGetAdditionnalInfos
     *            if true, populate the tree units with additionnal infos
     */
    public void populateTreeUnit( TreeUnit treeUnit, AdminUser user, boolean bGetAdditionnalInfos );
}

/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.unittree.service.rbac;

import fr.paris.lutece.plugins.unittree.business.action.IAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provides methods to
 */
public class UnittreeRBACService
{

    private static IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /**
     * Check that a given user is allowed to access a resource for a given permission. This checking is made recursively over unit depending on recursive type
     * 
     * @param unit
     *            the unit object being considered
     * @param strPermission
     *            the permission needed
     * @param user
     *            the user trying to access the ressource
     * @param recursiveType
     *            the recursive type for the isAuthorized checking over units
     * @return true if the user can access the given resource with the given permission, false otherwise
     */
    public static boolean isAuthorized( Unit unit, String strPermission, AdminUser user, UnittreeRBACRecursiveType recursiveType )
    {
        IUnitService unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
        List<Unit> listUnits = new ArrayList<>( );

        switch( recursiveType )
        {
            case PARENT_RECURSIVE:
                listUnits = unitService.getListParentUnits( unit );
                break;
            case CHILDREN_RECURSIVE:
                listUnits = unitService.getAllSubUnits( unit, false );
                break;
            case NOT_RECURSIVE:
                listUnits.add( unit );
                break;
        }

        if ( !listUnits.isEmpty( ) )
        {
            for ( Unit unitToCheck : listUnits )
            {
                Collection<String> colRoles = RBACHome.findRoleKeys( Unit.RESOURCE_TYPE, Integer.toString( unitToCheck.getIdUnit( ) ), strPermission );
                for ( String strRole : colRoles )
                {
                    if ( RBACService.isUserInRole( user, strRole ) )
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Filter a collection of RBACAction for a given user
     *
     * @param collectionActions
     *            The collection of actions
     * @param resource
     *            The resource
     * @param user
     *            The user
     * @param recursiveType
     * @return The filtered collection
     */
    public static List<IAction> getAuthorizedActionsCollection( List<IAction> collectionActions, RBACResource resource, AdminUser user,
            UnittreeRBACRecursiveType recursiveType )
    {
        List<IAction> list = new ArrayList<>( );

        if ( resource instanceof Unit )
        {
            for ( IAction action : collectionActions )
            {
                if ( isAuthorized( (Unit) resource, action.getPermission( ), user, recursiveType ) )
                {
                    list.add( action );
                }
            }
        }
        else
        {
            for ( IAction action : collectionActions )
            {
                if ( RBACService.isAuthorized( resource, action.getPermission( ), user ) )
                {
                    list.add( action );
                }
            }
        }

        return list;
    }

    /**
     * Get the authorized unit collection based on given permission. The list is computed for being adapted to be reprensented as a tree ;
     * 
     * @param strPermission
     *            the permission to test
     * @param user
     *            the lutece AdminUser
     * @return the list of authorized unit collection, which can be represented as a tree.
     */
    public static List<Unit> getTreeableAuthorizedUnitCollection( String strPermission, AdminUser user )
    {
        // First get all the Unit from database
        List<Unit> listAllUnits = UnitHome.findAll( );

        Set<Unit> listAuthorizedUnit = new HashSet<>( );

        for ( Unit unit : listAllUnits )
        {
            if ( !_unitService.isUnitInList( unit, listAuthorizedUnit ) && isAuthorized( unit, strPermission, user, UnittreeRBACRecursiveType.NOT_RECURSIVE ) )
            {
                List<Unit> listAllSubUnits = _unitService.getAllSubUnits( unit, false );
                for ( Unit childUnit : listAllSubUnits )
                {
                    listAuthorizedUnit.add( childUnit );
                }
                List<Unit> parentUnits = _unitService.getListParentUnits( unit );
                for ( Unit parentUnit : parentUnits )
                {
                    listAuthorizedUnit.add( parentUnit );
                }
            }
        }

        return new ArrayList<>( listAuthorizedUnit );
    }

}

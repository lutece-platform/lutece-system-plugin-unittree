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
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * UnitUserService
 * 
 */
public class UnitUserService implements IUnitUserService
{

    private static final String PROPERTY_MULTI_AFFECTATION_ENABLED = "unittree.users.enableMultiAffectation";

    private static final boolean DEFAULT_MULTI_AFFECTATION_ENABLED = false;

    @Inject
    private IUnitService _unitService;

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminUser getUser( int nIdUser )
    {
        return AdminUserHome.findByPrimaryKey( nIdUser );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminUser> getUsers( int nIdUnit, Map<String, Unit> mapIdUserUnit, boolean isInDepthSearch )
    {
        List<AdminUser> listAdminUsers = new ArrayList<AdminUser>( );

        // First add the users from the current unit
        listAdminUsers.addAll( getUsers( nIdUnit, mapIdUserUnit ) );

        if ( isInDepthSearch )
        {
            // Then add the users from the sub units
            for ( Unit subUnit : _unitService.getSubUnits( nIdUnit, false ) )
            {
                // Recursive function in order to get all users from all sub units of the unit
                listAdminUsers.addAll( getUsers( subUnit.getIdUnit( ), mapIdUserUnit, isInDepthSearch ) );
            }
        }

        return listAdminUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminUser> getAvailableUsers( AdminUser currentUser, int nIdUnit, boolean bMultiAffectationEnabled )
    {
        List<AdminUser> listUsers = new ArrayList<AdminUser>( );
        Unit unit = _unitService.getUnit( nIdUnit, false );
        for ( AdminUser user : AdminUserHome.findUserList( ) )
        {
            if ( isUserAvailable( currentUser, user, unit, bMultiAffectationEnabled ) )
            {
                listUsers.add( user );
            }
        }

        return listUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminUser> getAvailableUsers( AdminUser currentUser, int nIdUnit )
    {
        boolean bMultiAffectationEnabled = isMultiAffectationEnabled( );
        return getAvailableUsers( currentUser, nIdUnit, bMultiAffectationEnabled );
    }

    // PROCESS

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessAddUser( int nIdUser, AdminUser currentUser, HttpServletRequest request )
    {
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService( ) )
        {
            service.doAddUser( nIdUser, currentUser, request );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessModifyUser( int nIdUser, AdminUser currentUser, HttpServletRequest request )
    {
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService( ) )
        {
            service.doModifyUser( nIdUser, currentUser, request );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessRemoveUser( int nIdUser, AdminUser currentUser, HttpServletRequest request )
    {
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService( ) )
        {
            service.doRemoveUser( nIdUser, currentUser, request );
        }
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserInUnit( int nIdUser, int nIdUnit )
    {
        return UnitHome.isUserInUnit( nIdUser, nIdUnit );
    }

    // CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public boolean addUserToUnit( int nIdUnit, int nIdUser )
    {
        if ( !isUserInUnit( nIdUser, nIdUnit ) )
        {
            Unit unit = UnitHome.findByPrimaryKey( nIdUnit );
            List<Unit> listUnits = _unitService.getUnitsByIdUser( nIdUser, false );
            boolean bMultiAffectationEnabled = isMultiAffectationEnabled( );
            if ( !bMultiAffectationEnabled && listUnits.size( ) > 0 )
            {
                return false;
            }
            for ( Unit userUnit : listUnits )
            {
                if ( _unitService.isParent( unit, userUnit ) )
                {
                    removeUserFromUnit( nIdUser, userUnit.getIdUnit( ) );
                }
            }
            UnitHome.addUserToUnit( nIdUnit, nIdUser );

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void removeUserFromUnit( int nIdUser, int nIdUnit )
    {
        UnitHome.removeUserFromUnit( nIdUser, nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void removeUsersFromUnit( int nIdUnit )
    {
        UnitHome.removeUsersFromUnit( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultiAffectationEnabled( )
    {
        return AppPropertiesService.getPropertyBoolean( PROPERTY_MULTI_AFFECTATION_ENABLED,
                DEFAULT_MULTI_AFFECTATION_ENABLED );
    }

    // PRIVATE METHODS

    /**
     * Tell if 2 users have groups in common
     * @param user1 User1
     * @param user2 User2
     * @return true or false
     */
    private boolean haveCommonWorkgroups( AdminUser user1, AdminUser user2 )
    {
        ReferenceList workgroups = AdminWorkgroupHome.getUserWorkgroups( user1 );

        if ( workgroups.size( ) == 0 )
        {
            return true;
        }

        for ( ReferenceItem item : workgroups )
        {
            if ( AdminWorkgroupHome.isUserInWorkgroup( user2, item.getCode( ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the user is available
     * @param currentUser the current user
     * @param userToCheck the user to check
     * @param unit the unit to check if the user is available for
     * @param bMultiAffectationEnabled Indicates if this method should allow a
     *            user to be in several units, or if it should return false if
     *            he is already in a unit.
     * @return true if the user is available, false otherwise
     */
    private boolean isUserAvailable( AdminUser currentUser, AdminUser userToCheck, Unit unit,
            boolean bMultiAffectationEnabled )
    {
        List<Unit> listUnits = _unitService.getUnitsByIdUser( userToCheck.getUserId( ), false );
        if ( !bMultiAffectationEnabled && listUnits.size( ) > 0 )
        {
            return false;
        }
        for ( Unit userUnit : listUnits )
        {
            // If the user is in the unit or in one if its parents
            if ( userUnit.getIdUnit( ) == unit.getIdUnit( ) || _unitService.isParent( userUnit, unit ) )
            {
                return false;
            }
        }
        // Check if the current user is admin => visibility to all users
        if ( currentUser.isAdmin( ) )
        {
            return true;
        }

        // Check if the current user is parent to the user to check
        if ( currentUser.isParent( userToCheck ) )
        {
            // Then check if they have the same workgroup, or the user to check does not have any workgroup
            if ( haveCommonWorkgroups( currentUser, userToCheck )
                    || !AdminWorkgroupHome.checkUserHasWorkgroup( userToCheck.getUserId( ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the list of {@link AdminUser} from a given id unit
     * @param nIdUnit the id unit
     * @param mapIdUserUnit the map of <idUser, Unit>
     * @return a list of {@link AdminUser}
     */
    private List<AdminUser> getUsers( int nIdUnit, Map<String, Unit> mapIdUserUnit )
    {
        Unit unit = _unitService.getUnit( nIdUnit, false );
        List<AdminUser> listUsers = new ArrayList<AdminUser>( );
        List<Integer> listIdUsers = UnitHome.findIdsUser( nIdUnit );

        if ( ( listIdUsers != null ) && !listIdUsers.isEmpty( ) )
        {
            for ( int nIdUser : listIdUsers )
            {
                AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );

                if ( user != null )
                {
                    listUsers.add( user );
                    mapIdUserUnit.put( Integer.toString( user.getUserId( ) ), unit );
                }
            }
        }

        return listUsers;
    }
}

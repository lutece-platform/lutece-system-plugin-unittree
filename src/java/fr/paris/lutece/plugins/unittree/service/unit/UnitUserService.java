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
package fr.paris.lutece.plugins.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class UnitUserService implements IUnitUserService
{
    // GET
    public AdminUser getUser( int nIdUser )
    {
        return AdminUserHome.findByPrimaryKey( nIdUser );
    }

    @Override
    public List<AdminUser> getUsers( int nIdUnit )
    {
        List<AdminUser> listUsers = new ArrayList<AdminUser>(  );
        List<Integer> listIdUsers = UnitHome.findIdUsers( nIdUnit );

        if ( ( listIdUsers != null ) && !listIdUsers.isEmpty(  ) )
        {
            for ( int nIdUser : listIdUsers )
            {
                AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );

                if ( user != null )
                {
                    listUsers.add( user );
                }
            }
        }

        return listUsers;
    }

    @Override
    public List<AdminUser> getAvailableUsers( AdminUser currentUser )
    {
        List<AdminUser> listUsers = new ArrayList<AdminUser>(  );
        List<Integer> listAllIdsUserInUnit = UnitHome.findAllIdsUsers(  );

        for ( AdminUser user : AdminUserHome.findUserList(  ) )
        {
            if ( isUserAvailable( currentUser, user, listAllIdsUserInUnit ) )
            {
                listUsers.add( user );
            }
        }

        return listUsers;
    }

    // PROCESS

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessAddUser( int nIdUser, AdminUser currentUser, HttpServletRequest request )
    {
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService(  ) )
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
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService(  ) )
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
        for ( IUnitUserAttributeService service : UnitUserAttributeManager.getListUnitUserAttributeService(  ) )
        {
            service.doRemoveUser( nIdUser, currentUser, request );
        }
    }

    // CHECKS

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isUserInUnit( int nIdUser )
    {
        return UnitHome.isUserInUnit( nIdUser );
    }

    // CRUD

    /**
    * {@inheritDoc}
    */
    @Override
    @Transactional( "unittree.transactionManager" )
    public boolean addUserToUnit( int nIdUnit, int nIdUser )
    {
        if ( !isUserInUnit( nIdUser ) )
        {
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
    public void removeUserFromUnit( int nIdUser )
    {
        UnitHome.removeUserFromUnit( nIdUser );
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

        if ( workgroups.size(  ) == 0 )
        {
            return true;
        }

        for ( ReferenceItem item : workgroups )
        {
            if ( AdminWorkgroupHome.isUserInWorkgroup( user2, item.getCode(  ) ) )
            {
                return true;
            }
        }

        return false;
    }

    private boolean isUserAvailable( AdminUser currentUser, AdminUser userToCheck, List<Integer> listIdsUserInUnit )
    {
        // Check if the user is already in the unit
        if ( ( listIdsUserInUnit != null ) && !listIdsUserInUnit.isEmpty(  ) &&
                listIdsUserInUnit.contains( userToCheck.getUserId(  ) ) )
        {
            return false;
        }

        // Check if the current user is admin => visibility to all users
        if ( currentUser.isAdmin(  ) )
        {
            return true;
        }

        // Check if the current user is parent to the user to check
        if ( currentUser.isParent( userToCheck ) )
        {
            // Then check if they have the same workgroup, or the user to check does not have any workgroup
            if ( haveCommonWorkgroups( currentUser, userToCheck ) ||
                    !AdminWorkgroupHome.checkUserHasWorkgroup( userToCheck.getUserId(  ) ) )
            {
                return true;
            }
        }

        return false;
    }
}

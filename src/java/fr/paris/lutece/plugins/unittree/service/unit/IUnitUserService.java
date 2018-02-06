/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
import fr.paris.lutece.portal.business.user.AdminUser;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * IUnitUserService
 *
 */
public interface IUnitUserService
{
    // GET

    /**
     * Get the user from a given id user
     * 
     * @param nIdUser
     *            the id user
     * @return an {@link AdminUser}
     */
    AdminUser getUser( int nIdUser );

    /**
     * Get the list of {@link AdminUser} from a given id unit
     * 
     * @param nIdUnit
     *            the id unit
     * @param mapIdUserUnit
     *            the map of <idUser, Unit>
     * @param isInDepthSearch
     *            true if it is an in depth search (search in the sub units too)
     * @return a list of {@link AdminUser}
     */
    List<AdminUser> getUsers( int nIdUnit, Map<String, Unit> mapIdUserUnit, boolean isInDepthSearch );

    /**
     * Get the list of available users for a given unit. current user can administer.
     * 
     * @param currentUser
     *            the current user
     * @param nIdUnit
     *            The id of the unit
     * @return a list of {@link AdminUser}. If multi affectation is not enabled, return users that can be administered by the current user and that are not
     *         associated with any unit. Otherwise returns users that are not associated directly or transitively to the unit and that the current user can
     *         administer.
     */
    List<AdminUser> getAvailableUsers( AdminUser currentUser, int nIdUnit );

    /**
     * Get the list of available users for a given unit.
     * 
     * @param currentUser
     *            The current user
     * @param nIdUnit
     *            The id of the unit
     * @param bMultiAffectationEnabled
     *            True to include users already associated to a unit, false to ignore them.
     * @return a list of {@link AdminUser}
     */
    List<AdminUser> getAvailableUsers( AdminUser currentUser, int nIdUnit, boolean bMultiAffectationEnabled );

    // PROCESS

    /**
     * Do process adding the user to the unit
     * 
     * @param nIdUser
     *            the id user
     * @param currentUser
     *            the current user
     * @param request
     *            the HTTP requesst
     */
    void doProcessAddUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    /**
     * Do process modifying the user
     * 
     * @param nIdUser
     *            the id user
     * @param currentUser
     *            the current user
     * @param request
     *            the HTTP request
     */
    void doProcessModifyUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    /**
     * Do process removing an user from an unit
     * 
     * @param nIdUser
     *            the id user
     * @param currentUser
     *            the current user
     * @param request
     *            the HTTP request
     */
    void doProcessRemoveUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    // CHECKS

    /**
     * Check if the given user is in a given unit
     * 
     * @param nIdUser
     *            the id user
     * @param nIdUnit
     *            The id of the unit
     * @return true if the user is in an unit, false otherwise
     */
    boolean isUserInUnit( int nIdUser, int nIdUnit );

    // CRUD

    /**
     * Add an user to an unit
     * 
     * @param nIdUnit
     *            the id unit
     * @param nIdUser
     *            the id user
     * @return true if the user is added to the unit, false otherwise
     */
    @Transactional( "unittree.transactionManager" )
    boolean addUserToUnit( int nIdUnit, int nIdUser );

    /**
     * Remove the user from a unit
     * 
     * @param nIdUser
     *            the id user
     * @param nIdUnit
     *            The id of the unit
     */
    @Transactional( "unittree.transactionManager" )
    void removeUserFromUnit( int nIdUser, int nIdUnit );

    /**
     * Remove users from a given id unit
     * 
     * @param nIdUnit
     *            the id unit
     */
    @Transactional( "unittree.transactionManager" )
    void removeUsersFromUnit( int nIdUnit );

    /**
     * Check if users can be affected to several units at the same time or not
     * 
     * @return True if multi affectation is enabled, false otherwise. If the property is not defined, the default value is used which is false.
     */
    boolean isMultiAffectationEnabled( );
}

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

import fr.paris.lutece.portal.business.user.AdminUser;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @param nIdUser the id user
     * @return an {@link AdminUser}
     */
    AdminUser getUser( int nIdUser );

    /**
     * Get the list of {@link AdminUser} from a given id unit
     * @param nIdUnit the id unit
     * @return a list of {@link AdminUser}
     */
    List<AdminUser> getUsers( int nIdUnit );

    /**
     * Get the list of available users
     * @param currentUser the current user
     * @return a list of {@link AdminUser}
     */
    List<AdminUser> getAvailableUsers( AdminUser currentUser );

    // PROCESS

    /**
     * Do process adding the user to the unit
     * @param nIdUser the id user
     * @param currentUser the current user
     * @param request the HTTP requesst
     */
    void doProcessAddUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    /**
     * Do process modifying the user
     * @param nIdUser the id user
     * @param currentUser the current user
     * @param request the HTTP request
     */
    void doProcessModifyUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    /**
     * Do process removing an user from an unit
     * @param nIdUser the id user
     * @param currentUser the current user
     * @param request the HTTP request
     */
    void doProcessRemoveUser( int nIdUser, AdminUser currentUser, HttpServletRequest request );

    // CHECKS

    /**
     * Check if the given user is in an unit
     * @param nIdUser the id user
     * @return true if the user is in an unit, false otherwises
     */
    boolean isUserInUnit( int nIdUser );

    // CRUD

    /**
     * Add an user to an unit
     * @param nIdUnit the id unit
     * @param nIdUser the id user
     * @return true if the user is added to the unit, false otherwise
     */
    @Transactional( "unittree.transactionManager" )
    boolean addUserToUnit( int nIdUnit, int nIdUser );

    /**
     * Remove the user from an unit
     * @param nIdUser the id user
     */
    @Transactional( "unittree.transactionManager" )
    void removeUserFromUnit( int nIdUser );

    /**
     * Remove users from a given id unit
     * @param nIdUnit the id unit
     */
    @Transactional( "unittree.transactionManager" )
    void removeUsersFromUnit( int nIdUnit );
}

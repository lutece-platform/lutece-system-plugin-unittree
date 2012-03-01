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
package fr.paris.lutece.plugins.unittree.business.unit;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 *
 */
public interface IUnitDAO
{
    /**
    * Get new primary key
    * @param plugin the plugin
    * @return a new primary key
    */
    int newPrimaryKey( Plugin plugin );

    /**
     * Load the unit
     * @param nIdUnit the id unit
     * @param plugin the plugin
     * @return an instance of {@link Unit}
     */
    Unit load( int nIdUnit, Plugin plugin );

    /**
     * Load an unit by id user
     * @param nIdUser the id user
     * @param plugin the plugin
     * @return an instance of {@link Unit}
     */
    Unit selectByIdUser( int nIdUser, Plugin plugin );

    /**
     * Select all units
     * @param plugin the plugin
     * @return a list of {@link Unit}
     */
    List<Unit> selectAll( Plugin plugin );

    /**
     * Select the units by filter
     * @param uFilter the filter
     * @param plugin the plugin
     * @return a list of {@link Unit}
     */
    List<Unit> selectByFilter( UnitFilter uFilter, Plugin plugin );

    /**
     * Select all ids user
     * @param plugin the plugin
     * @return a list of ids user
     */
    List<Integer> selectAllIdsUser( Plugin plugin );

    /**
     * Select the ids user from a given id unit
     * @param nIdUnit the id unit
     * @param plugin the plugin
     * @return a list of ids user
     */
    List<Integer> selectIdsUser( int nIdUnit, Plugin plugin );

    /**
     * Insert a new unit
     * @param unit the unit
     * @param plugin the plugin
     * @return the new primary key
     */
    int insert( Unit unit, Plugin plugin );

    /**
     * Check if the given id unit has sub units or not
     * @param nIdUnit the id unit
     * @param plugin the plugin
     * @return true if the unit has sub units, false otherwise
     */
    boolean hasSubUnits( int nIdUnit, Plugin plugin );

    /**
     * Remove a unit
     * @param nIdUnit the id unit
     * @param plugin the plugin
     */
    void remove( int nIdUnit, Plugin plugin );

    /**
     * Update a unit
     * @param unit the unit
     * @param plugin the plugin
     */
    void update( Unit unit, Plugin plugin );

    /**
     * Add an user to an unit
     * @param nIdUnit the id unit
     * @param nIdUser the id user
     * @param plugin the plugin
     */
    void addUserToUnit( int nIdUnit, int nIdUser, Plugin plugin );

    /**
     * Check if the user is in the unit
     * @param nIdUser the id user
     * @param plugin the plugin
     * @return true if the user is in the unit, false otherwise
     */
    boolean isUserInUnit( int nIdUser, Plugin plugin );

    /**
     * Remove a given user from the unit
     * @param nIdUser the id user
     * @param plugin the plugin
     */
    void removeUserFromUnit( int nIdUser, Plugin plugin );

    /**
     * Remove users from a given id unit
     * @param nIdUnit the id unit
     * @param plugin the plugin
     */
    void removeUsersFromUnit( int nIdUnit, Plugin plugin );
}

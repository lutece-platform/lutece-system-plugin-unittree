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
package fr.paris.lutece.plugins.unittree.business.unit;

import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import jakarta.enterprise.inject.spi.CDI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * UnitHome
 *
 */
public final class UnitHome
{
    private static Plugin _plugin = PluginService.getPlugin( UnitTreePlugin.PLUGIN_NAME );
    private static IUnitDAO _dao = CDI.current( ).select( IUnitDAO.class ).get( );

    /**
     * Private constructor
     */
    private UnitHome( )
    {
    }

    /**
     * Load the unit
     * 
     * @param nIdUnit
     *            the id unit
     * @return an instance of {@link Unit}
     */
    public static Unit findByPrimaryKey( int nIdUnit )
    {
        return _dao.load( nIdUnit, _plugin );
    }

    /**
     * Load the unit
     * 
     * @param strCode
     *            the code
     * @return an instance of {@link Unit}
     */
    public static Unit findByCode( String strCode )
    {
        return _dao.findByCode( strCode, _plugin );
    }

    /**
     * Load an unit by id user
     * 
     * @param nIdUser
     *            the id user
     * @return an instance of {@link Unit}
     */
    public static List<Unit> findByIdUser( int nIdUser )
    {
        return _dao.selectByIdUser( nIdUser, _plugin );
    }

    /**
     * Find the units by filter
     * 
     * @param uFilter
     *            the filter
     * @return a list of {@link Unit}
     */
    public static List<Unit> findByFilter( UnitFilter uFilter )
    {
        return _dao.selectByFilter( uFilter, _plugin );
    }

    /**
     * Select all units
     * 
     * @return a list of {@link Unit}
     */
    public static List<Unit> findAll( )
    {
        return _dao.selectAll( _plugin );
    }

    /**
     * Find all ids users
     * 
     * @return a list of ids user
     */
    public static List<Integer> findAllIdsUsers( )
    {
        return _dao.selectAllIdsUser( _plugin );
    }

    /**
     * Find the ids user
     * 
     * @param nIdUnit
     *            the id unit
     * @return the list of ids user
     */
    public static List<Integer> findIdsUser( int nIdUnit )
    {
        return _dao.selectIdsUser( nIdUnit, _plugin );
    }

    /**
     * Insert a new unit
     * 
     * @param unit
     *            the unit
     * @return the new primary key
     */
    public static int create( Unit unit )
    {
        return _dao.insert( unit, _plugin );
    }

    /**
     * Remove a unit
     * 
     * @param nIdUnit
     *            the id unit
     */
    public static void remove( int nIdUnit )
    {
        _dao.remove( nIdUnit, _plugin );
    }

    /**
     * Check if the given id unit has sub units
     * 
     * @param nIdUnit
     *            the id unit
     * @return true if it has sub units, false otherwise
     */
    public static boolean hasSubUnits( int nIdUnit )
    {
        return _dao.hasSubUnits( nIdUnit, _plugin );
    }

    /**
     * Retrieve list of direct children units
     * 
     * @param nIdUnit
     *            the id unit
     * @return List of children units
     */
    public static List<Unit> getDirectSubUnits( int nIdUnit )
    {
        return _dao.getSubUnits( nIdUnit, _plugin );
    }

    /**
     * Retrieve Set of all children units id
     * 
     * @param nIdUnit
     *            the id unit
     * @return Set of all children unit id
     */
    public static Set<Integer> getAllSubUnitsId( int nIdUnit )
    {
    	return _dao.getAllSubUnitsId( nIdUnit, _plugin );
    }

    /**
     * Remove a user from a unit
     * 
     * @param nIdUser
     *            the id user
     * @param nIdUnit
     *            The id of the unit
     */
    public static void removeUserFromUnit( int nIdUser, int nIdUnit )
    {
        _dao.removeUserFromUnit( nIdUser, nIdUnit, _plugin );
    }

    /**
     * Remove an unit from a given id unit
     * 
     * @param nIdUnit
     *            the id unit
     */
    public static void removeUsersFromUnit( int nIdUnit )
    {
        _dao.removeUsersFromUnit( nIdUnit, _plugin );
    }

    /**
     * Update a unit
     * 
     * @param unit
     *            the unit
     */
    public static void update( Unit unit )
    {
        _dao.update( unit, _plugin );
    }

    /**
     * Add the user to the unit
     * 
     * @param nIdUnit
     *            the id unit
     * @param nIdUser
     *            the id user
     */
    public static void addUserToUnit( int nIdUnit, int nIdUser )
    {
        _dao.addUserToUnit( nIdUnit, nIdUser, _plugin );
    }

    /**
     * Check if the user is in a given unit
     * 
     * @param nIdUser
     *            the id user
     * @param nIdUnit
     *            The id of the unit
     * @return true if the user is in the unit, false otherwise
     */
    public static boolean isUserInUnit( int nIdUser, int nIdUnit )
    {
        return _dao.isUserInUnit( nIdUser, nIdUnit, _plugin );
    }

    /**
     * Return all the Unit with no children (level 0)
     * 
     * @return all the Unit with no children (level 0)
     */
    public static List<Unit> getUnitWithNoChildren( )
    {
        return _dao.getUnitWithNoChildren( _plugin );
    }

    /**
     * Update the parent of a unit
     * 
     * @param nIdUnitToMove
     *            The id of the unit to change the parent of.
     * @param nIdNewParent
     *            The id of the new parent of the unit.
     */
    public static void updateParent( int nIdUnitToMove, int nIdNewParent )
    {
        _dao.updateParent( nIdUnitToMove, nIdNewParent, _plugin );
    }
}

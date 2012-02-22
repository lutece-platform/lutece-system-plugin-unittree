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
package fr.paris.lutece.plugins.unittree.business.unit;

import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * UnitHome
 *
 */
public final class UnitHome
{
    private static final String BEAN_UNIT_DAO = "unittree.unitDAO";
    private static Plugin _plugin = PluginService.getPlugin( UnitTreePlugin.PLUGIN_NAME );
    private static IUnitDAO _dao = (IUnitDAO) SpringContextService.getBean( BEAN_UNIT_DAO );

    /**
     * Private constructor
     */
    private UnitHome(  )
    {
    }

    /**
     * Load the unit
     * @param nIdUnit the id unit
     * @return an instance of {@link Unit}
     */
    public static Unit findByPrimaryKey( int nIdUnit )
    {
        return _dao.load( nIdUnit, _plugin );
    }

    public static List<Unit> findByFilter( UnitFilter uFilter )
    {
        return _dao.selectByFilter( uFilter, _plugin );
    }

    /**
     * Select all units
     * @return a list of {@link Unit}
     */
    public static List<Unit> findAll(  )
    {
        return _dao.selectAll( _plugin );
    }

    public static List<Integer> findAllIdsUsers(  )
    {
        return _dao.selectAllIdsUser( _plugin );
    }

    public static List<Integer> findIdUsers( int nIdUnit )
    {
        return _dao.selectIdsUser( nIdUnit, _plugin );
    }

    /**
     * Insert a new unit
     * @param unit the unit
     */
    public static int create( Unit unit )
    {
        return _dao.insert( unit, _plugin );
    }

    /**
     * Remove a unit
     * @param nIdUnit the id unit
     */
    public static void remove( int nIdUnit )
    {
        _dao.remove( nIdUnit, _plugin );
    }

    /**
     * Remove a unit
     * @param nIdUnit the id unit
     */
    public static void removeUser( int nIdUser )
    {
        _dao.removeUser( nIdUser, _plugin );
    }

    /**
     * Remove a unit
     * @param nIdUnit the id unit
     */
    public static void removeUsersByIdUnit( int nIdUnit )
    {
        _dao.removeUsersByIdUnit( nIdUnit, _plugin );
    }

    /**
     * Update a unit
     * @param unit the unit
     */
    public static void update( Unit unit )
    {
        _dao.update( unit, _plugin );
    }

    public static void addUserToUnit( int nIdUnit, int nIdUser )
    {
        _dao.addUserToUnit( nIdUnit, nIdUser, _plugin );
    }

    public static boolean isUserInUnit( int nIdUser )
    {
        return _dao.isUserInUnit( nIdUser, _plugin );
    }
}

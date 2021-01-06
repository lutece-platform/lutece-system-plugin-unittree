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
package fr.paris.lutece.plugins.unittree.business.assignment;

import java.util.List;

import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for UnitAssignment objects
 */

public final class UnitAssignmentHome
{
    private static Plugin _plugin = PluginService.getPlugin( UnitTreePlugin.PLUGIN_NAME );

    // Static variable pointed at the DAO instance
    private static IUnitAssignmentDAO _dao = SpringContextService.getBean( UnitAssignmentDAO.BEAN_NAME );

    /**
     * Private constructor
     */
    private UnitAssignmentHome( )
    {
        super( );
    }

    /**
     * Creates a unit assignment
     * 
     * @param unitAssignment
     *            The unit assignment to create
     * @return The unit assignment which has been created
     */
    public static UnitAssignment create( UnitAssignment unitAssignment )
    {
        _dao.insert( unitAssignment, _plugin );

        return unitAssignment;
    }

    /**
     * Deactivates a unit assignment
     * 
     * @param unitAssignment
     *            The unit assignment to deactivate
     */
    public static void deactivate( UnitAssignment unitAssignment )
    {
        _dao.deactivate( unitAssignment, _plugin );
    }

    /**
     * <p>
     * Finds the unit assignments associated to the specified couple {resource id, resource type}.
     * </p>
     * <p>
     * The result list contains active and inactive unit assignments.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @return the list of unit assignments
     */
    public static List<UnitAssignment> findByResource( int nIdResource, String strResourceType )
    {
        return _dao.selectByResource( nIdResource, strResourceType, _plugin );
    }

    /**
     * <p>
     * Finds the unit assignments associated to the specified unit.
     * </p>
     * <p>
     * The result list contains active and inactive unit assignments.
     * </p>
     * 
     * @param nIdUnit
     *            the unit id
     * @return the list of unit assignments
     */
    public static List<UnitAssignment> findByUnit( int nIdUnit )
    {
        return _dao.selectByUnit( nIdUnit, _plugin );
    }

    /**
     * <p>
     * Deactivate the units assignments associated to the specified resource.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     */
    public static void deactivateByResource( int nIdResource, String strResourceType )
    {
        _dao.deactivateByResource( nIdResource, strResourceType, _plugin );
    }

    /**
     * <p>
     * Delete the units assignments associated to the specified resource.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     */
    public static void deleteByResource( int nIdResource, String strResourceType )
    {
        _dao.deleteByResource( nIdResource, strResourceType, _plugin );
    }
}

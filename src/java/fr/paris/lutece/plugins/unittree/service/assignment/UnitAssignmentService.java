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
package fr.paris.lutece.plugins.unittree.service.assignment;

import fr.paris.lutece.plugins.unittree.business.assignment.IUnitAssignmentDAO;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the entry point for unit assignment. It provides methods for unit assignment.
 *
 */
public final class UnitAssignmentService
{
    private static final Plugin _plugin = PluginService.getPlugin( UnitTreePlugin.PLUGIN_NAME );

    // Static variable pointed at the DAO instance
    private static IUnitAssignmentDAO _dao = SpringContextService.getBean( IUnitAssignmentDAO.BEAN_NAME );

    /**
     * Constructor
     */
    private UnitAssignmentService( )
    {
        super( );
    }

    /**
     * Finds the current unit assignment for the specified couple {resource id, resource type}
     * 
     * @param nIdResource
     *            The resource id
     * @param strResourceType
     *            the resource type
     * @return the current unit assignment
     */
    public static UnitAssignment findCurrentAssignment( int nIdResource, String strResourceType )
    {
        return _dao.loadCurrentAssignment( nIdResource, strResourceType, _plugin );
    }

    /**
     * <p>
     * Finds the unit assignments associated to the specified couple {resource id, resource type}.
     * <p>
     * <p>
     * The unit assignments are sorted by ascending date.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @return the list of unit assignments
     */
    public static List<UnitAssignment> findAssignments( int nIdResource, String strResourceType )
    {
        List<UnitAssignment> listUnitAssignment = _dao.selectByResource( nIdResource, strResourceType, _plugin );

        return listUnitAssignment.stream( ).filter( unitAssignment -> unitAssignment.isActive( ) ).collect( Collectors.toList( ) );
    }

    /**
     * <p>
     * Finds the resources assigned to the unit .
     * <p>
     * <p>
     * The unit assignments are sorted by ascending date.
     * </p>
     * 
     * @param nIdUnit
     *            the Unit id
     * @return the list of unit assignments
     */
    public static List<UnitAssignment> findAssignmentsByUnit( int nIdUnit )
    {
        List<UnitAssignment> listUnitAssignment = _dao.selectByUnit( nIdUnit, _plugin );

        return listUnitAssignment.stream( ).filter( unitAssignment -> unitAssignment.isActive( ) ).collect( Collectors.toList( ) );
    }

}

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

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.List;

/**
 * This interface provides Data Access methods for UnitAssignment objects
 */

public interface IUnitAssignmentDAO
{
    /**
     * Inserts a new record in the table.
     * 
     * @param unitAssignment
     *            the unit assignment to insert
     * @param plugin
     *            the Plugin
     */
    void insert( UnitAssignment unitAssignment, Plugin plugin );

    /**
     * Deactivates a unit assignment
     * 
     * @param unitAssignment
     *            the unit assignment to deactivate
     * @param plugin
     *            the Plugin
     */
    void deactivate( UnitAssignment unitAssignment, Plugin plugin );

    /**
     * Loads the current unit assignment for the specified couple {resource id, resource type}
     * 
     * @param nIdResource
     *            The resource id
     * @param strResourceType
     *            the resource type
     * @param plugin
     *            the Plugin
     * @return The current unit assignment
     */
    UnitAssignment loadCurrentAssignment( int nIdResource, String strResourceType, Plugin plugin );

    /**
     * Loads the unit assignments associated to the specified couple {resource id, resource type}
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param plugin
     *            the plugin
     * @return the list of unit assignments
     */
    List<UnitAssignment> selectByResource( int nIdResource, String strResourceType, Plugin plugin );

    /**
     * Loads the unit assignments associated to the specified unit
     * 
     * @param nIdUnit
     *            the unit id
     * @param plugin
     *            the plugin
     * @return the list of unit assignments
     */
    List<UnitAssignment> selectByUnit( int nIdUnit, Plugin plugin );

    /**
     * Finds id resource from filter.
     *
     * @param filter the filter
     * @param plugin the plugin
     * @return the list of id resource
     */
    List<Integer> selectIdResourceByFilter(UnitAssignmentFilter filter , Plugin plugin );

    /**
     * Deactivate the unit assignments associated to the specified resource
     * 
     * @param nIdResource
     *            The resource id
     * @param strResourceType
     *            The resource type
     * @param plugin
     *            The plugin
     */
    void deactivateByResource( int nIdResource, String strResourceType, Plugin plugin );

    /**
     * Deletes the unit assignments associated to the specified resource
     * 
     * @param nIdResource
     *            The resource id
     * @param strResourceType
     *            The resource type
     * @param plugin
     *            The plugin
     */
    void deleteByResource( int nIdResource, String strResourceType, Plugin plugin );

}

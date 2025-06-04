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
package fr.paris.lutece.plugins.unittree.service.selection;

import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

/**
 * <p>
 * This interface represents a unit selection.
 * </p>
 * <p>
 * A unit selection is part of the workflow task to assign a resource to a unit. In this task, several unit selections can be configured. Then when the user
 * executes an action containing this task, the user will choose which unit selection she / he wants.
 * </p>
 *
 */
public interface IUnitSelection
{
    /**
     * <p>
     * Gives the id.
     * </p>
     * <p>
     * The id is used to find which unit selection has to be used in the task. The id must be unique among the unit selections.
     * </p>
     * 
     * @return the id
     */
    String getId( );

    /**
     * <p>
     * Tells if the unit selection is automatic or not.
     * </p>
     * <p>
     * A non-automatic unit selection means the user has to choose the unit she / he wants the resource to be assigned to.
     * </p>
     * <p>
     * An automatic unit selection can be used in the task to automatically assign a resource to a unit. A non-automatic unit selection cannot.
     * </p>
     * 
     * @return {@code true} if the unit selection is automatic, {@code false} otherwise
     */
    boolean isAutomatic( );

    /**
     * Gives the configuration handler associated to this unit selection
     * 
     * @return the configuration handler
     */
    IConfigurationHandler getConfigurationHandler( );

    /**
     * Gives the form handler associated to this unit selection
     * 
     * @return the form handler
     */
    ITaskFormHandler getTaskFormHandler( );

    /**
     * <p>
     * Selects the unit the resource will be assigned to.
     * </p>
     * <p>
     * This method is not responsible of storing the assignment. The store is done by the workflow task itself.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request containing information to select the unit. WARNING : if the unit selection is automatic, the request is {@code null}.
     * @param task
     *            the task associated to the unit selection
     * @return the id of the target unit
     * @throws AssignmentNotPossibleException
     *             if the assignment is not possible
     */
    int select( int nIdResource, String strResourceType, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException;
}

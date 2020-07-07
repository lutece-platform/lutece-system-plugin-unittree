/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.util.Locale;

import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

/**
 * <p>
 * This class is a handler for unit selection form
 * </p>
 * <p>
 * The unit selection form is displayed in the task form of the workflow task to assign a resource to a unit.
 * </p>
 *
 */
public interface ITaskFormHandler
{
    // Templates
    String TEMPLATE_AUTOMATIC_TASK_FORM = "admin/plugins/workflow/modules/unittree/unitselection/form/unit_selection_automatic_form.html";

    /**
     * Gives the title of the unit selection displayed in the task form.
     * 
     * @param locale
     *            the locale
     * @return the title
     */
    String getTitle( Locale locale );

    /**
     * Gives the HTML representing the form to display in the task form
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param locale
     *            the locale
     * @param task
     *            the task associated to the configuration
     * @return the HTML in a String
     * @throws AssignmentNotPossibleException
     *             if the assignment is not possible
     */
    String getDisplayedForm( int nIdResource, String strResourceType, Locale locale, ITask task ) throws AssignmentNotPossibleException;
}

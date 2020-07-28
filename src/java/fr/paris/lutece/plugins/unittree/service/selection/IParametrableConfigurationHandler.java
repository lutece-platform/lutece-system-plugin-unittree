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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

public interface IParametrableConfigurationHandler
{
    /**
     * Gives the HTML representing the configuration form to display in the workflow task
     * 
     * @param locale
     *            the locale
     * @param task
     *            the task associated to the configuration
     * @return the HTML in a String
     */
    String getCustomConfigForm( Locale locale, ITask task );

    /**
     * Saves the configuration
     * 
     * @param request
     *            the request containing the configuration
     * @param task
     *            the task associated to the configuration
     * @return the error or {@code null} if there is no error
     */
    String saveConfiguration( HttpServletRequest request, ITask task );

    /**
     * Removes the configuration
     * 
     * @param task
     *            the task associated to the configuration
     */
    void removeConfiguration( ITask task );

    /**
     * Get the {@link IParametrableUnitSelection}.
     * 
     * @return
     */
    IParametrableUnitSelection getParametrableUnitSelection( );

    String getBeanName( );

    String getTitle( Locale locale );
}

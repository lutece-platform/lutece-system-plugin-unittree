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
package fr.paris.lutece.plugins.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.web.unit.IUnitUserAttributeComponent;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * UnitUserAttributeManager
 *
 */
public final class UnitUserAttributeManager
{
    /**
     * Private constructor
     */
    private UnitUserAttributeManager( )
    {
    }

    /**
     * Get the list of unit user attribute components
     * 
     * @return a list of {@link IUnitUserAttributeComponent}
     */
    public static List<IUnitUserAttributeComponent> getListUnitUserAttributeComponents( )
    {
        return CDI.current( ).select( IUnitUserAttributeComponent.class ).stream( ).toList( );
    }

    /**
     * Get the list of unit user attribute services
     * 
     * @return a list of {@link IUnitUserAttributeService}
     */
    public static List<IUnitUserAttributeService> getListUnitUserAttributeService( )
    {
        return CDI.current( ).select( IUnitUserAttributeService.class ).stream( ).toList( );
    }

    /**
     * Fill the model for the unit user attribute component
     * 
     * @param request
     *            the HTTP request
     * @param adminUser
     *            the current user
     * @param model
     *            the model
     * @param strMark
     *            the marker
     */
    public static void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model, String strMark )
    {
        for ( IUnitUserAttributeComponent component : getListUnitUserAttributeComponents( ) )
        {
            component.fillModel( request, model );
        }

        model.put( strMark, getListUnitUserAttributeComponents( ) );
    }
}

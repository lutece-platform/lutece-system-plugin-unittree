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

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.web.unit.IUnitAttributeComponent;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * UnitAttributeManager
 *
 */
public final class UnitAttributeManager
{
    /**
     * Private constructor
     */
    private UnitAttributeManager( )
    {
    }

    /**
     * Get the list of unit attribute components
     * 
     * @return a list of {@link IUnitAttributeComponent}
     */
    public static List<IUnitAttributeComponent> getListUnitAttributeComponents( )
    {
        return SpringContextService.getBeansOfType( IUnitAttributeComponent.class );
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
        for ( IUnitAttributeComponent component : getListUnitAttributeComponents( ) )
        {
            component.fillModel( request, adminUser, model );
        }

        model.put( strMark, getListUnitAttributeComponents( ) );
    }

    /**
     * Get the list of unit attribute services
     * 
     * @return a list of {@link IUnitUserAttributeService}
     */
    public static List<IUnitAttributeService> getListUnitAttributeService( )
    {
        return SpringContextService.getBeansOfType( IUnitAttributeService.class );
    }

    /**
     * Populate an unit with the data of the <strong>request</strong>
     * 
     * @param unit
     *            the unit to populate
     * @param request
     *            the HTTP request
     * @throws UnitErrorException
     *             exception if there is an error (ex: mandatory field)
     */
    public static void populate( Unit unit, HttpServletRequest request ) throws UnitErrorException
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.populate( unit, request );
        }
    }

    /**
     * Populate an unit additionnal attributes with the data of the <strong>database</strong>
     * 
     * @param unit
     *            the unit to populate
     */
    public static void populate( Unit unit )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.populate( unit );
        }
    }

    /**
     * Do create the additional attributes of the given unit
     * 
     * @param unit
     *            the unit
     * @param request
     *            the HTTP request
     */
    public static void doCreateUnit( Unit unit, HttpServletRequest request )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.doCreateUnit( unit, request );
        }
    }

    /**
     * Do modify the additional attributes of the given unit
     * 
     * @param unit
     *            the unit
     * @param request
     *            the HTTP request
     */
    public static void doModifyUnit( Unit unit, HttpServletRequest request )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.doModifyUnit( unit, request );
        }
    }

    /**
     * Do remove the additional attributes
     * 
     * @param nIdUnit
     *            the id unit
     * @param request
     *            the HTTP request
     */
    public static void doRemoveUnit( int nIdUnit, HttpServletRequest request )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.doRemoveUnit( nIdUnit, request );
        }
    }

    /**
     * Check if the given id unit can creabe sub units
     * 
     * @param nIdUnit
     *            the id unit
     * @return true if the unit can create sub unit
     */
    public static boolean canCreateSubUnit( int nIdUnit )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            if ( !service.canCreateSubUnit( nIdUnit ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Update attributes to change the parent of a unit.
     * 
     * @param unitToMove
     *            The unit to change the parent of.
     * @param newUnitParent
     *            The new parent of the unit
     */
    public static void moveSubTree( Unit unitToMove, Unit newUnitParent )
    {
        for ( IUnitAttributeService service : UnitAttributeManager.getListUnitAttributeService( ) )
        {
            service.moveSubTree( unitToMove, newUnitParent );
        }
    }
}

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
package fr.paris.lutece.plugins.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 *
 * UnitResourceIdService
 *
 */
public class UnitResourceIdService extends ResourceIdService
{
    /** Permission for creating a space */
    public static final String PERMISSION_CREATE = "CREATE";

    /** Permission for modifying a space */
    public static final String PERMISSION_MODIFY = "MODIFY";

    /** Permission for deleting a space */
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_ADD_USER = "ADD_USER";
    public static final String PERMISSION_MODIFY_USER = "MODIFY_USER";
    public static final String PERMISSION_MOVE_USER = "MOVE_USER";
    public static final String PERMISSION_MOVE_UNIT = "MOVE_UNIT";
    public static final String PERMISSION_REMOVE_USER = "REMOVE_USER";
    public static final String PERMISSION_SEE_UNIT = "SEE_UNIT";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "unittree.unit.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "unittree.unit.permission.label.create";
    private static final String PROPERTY_LABEL_MODIFY = "unittree.unit.permission.label.modify";
    private static final String PROPERTY_LABEL_DELETE = "unittree.unit.permission.label.delete";
    private static final String PROPERTY_LABEL_ADD_USER = "unittree.unit.permission.label.addUser";
    private static final String PROPERTY_LABEL_MODIFY_USER = "unittree.unit.permission.label.modifyUser";
    private static final String PROPERTY_LABEL_MOVE_USER = "unittree.unit.permission.label.moveUser";
    private static final String PROPERTY_LABEL_REMOVE_USER = "unittree.unit.permission.label.removeUser";
    private static final String PROPERTY_LABEL_MOVE_UNIT = "unittree.unit.action.moveSubTree.name";
    private static final String PROPERTY_LABEL_SEE_UNIT = "unittree.unit.action.seeUnit.name";

    /**
     * Constructor
     */
    public UnitResourceIdService( )
    {
        setPluginName( UnitTreePlugin.PLUGIN_NAME );
    }

    /**
     * Initializes the service
     */
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( UnitResourceIdService.class.getName( ) );
        rt.setPluginName( UnitTreePlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Unit.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_ADD_USER );
        p.setPermissionTitleKey( PROPERTY_LABEL_ADD_USER );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFY_USER );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_USER );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MOVE_USER );
        p.setPermissionTitleKey( PROPERTY_LABEL_MOVE_USER );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_REMOVE_USER );
        p.setPermissionTitleKey( PROPERTY_LABEL_REMOVE_USER );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MOVE_UNIT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MOVE_UNIT );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_SEE_UNIT );
        p.setPermissionTitleKey( PROPERTY_LABEL_SEE_UNIT );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of resource ids
     * 
     * @param locale
     *            The current locale
     * @return A list of resource ids
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        IUnitService unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
        ReferenceList listResources = new ReferenceList( );

        for ( Unit unit : unitService.getAllUnits( false ) )
        {
            listResources.addItem( unit.getIdUnit( ), unit.getLabel( ) );
        }

        return listResources;
    }

    /**
     * Returns the Title of a given resource
     * 
     * @param strId
     *            The Id of the resource
     * @param locale
     *            The current locale
     * @return The Title of a given resource
     */
    public String getTitle( String strId, Locale locale )
    {
        if ( StringUtils.isNotBlank( strId ) && StringUtils.isNumeric( strId ) )
        {
            IUnitService unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
            int nIdUnit = Integer.parseInt( strId );
            Unit unit = unitService.getUnit( nIdUnit, false );

            if ( unit != null )
            {
                return unit.getLabel( );
            }
        }

        return StringUtils.EMPTY;
    }
}

/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.unittree.web.action;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitResourceIdService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * AddUnitUsersPluginAction
 *
 */
public class AddUnitUsersPluginAction extends AbstractPluginAction<IUnitSearchFields> implements IUnitPluginAction
{
    private static final String ACTION_NAME = "Add users to the unit";

    // PARAMETERS
    private static final String PARAMETER_ADD_USER = "addUsers";
    private static final String PARAMETER_ID_UNIT = "idUnit";

    // MARKS
    private static final String MARK_PERMISSION_ADD_USERS = "permissionAddUsers";

    // TEMPLATE
    private static final String TEMPLATE_BUTTON = "actions/add_users.html";

    // JSP
    private static final String JSP_URL_ADD_USERS = "jsp/admin/plugins/unittree/AddUsers.jsp";
    @Inject
    private IUnitService _unitService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) )
        {
            strIdUnit = Integer.toString( Unit.ID_ROOT );
        }

        model.put( MARK_PERMISSION_ADD_USERS,
            _unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_ADD_USER, adminUser ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getButtonTemplate(  )
    {
        return TEMPLATE_BUTTON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return ACTION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        return StringUtils.isNotBlank( request.getParameter( PARAMETER_ADD_USER ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
        IUnitSearchFields sessionFields ) throws AccessDeniedException
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_ADD_USERS );
        urlItem.addParameter( PARAMETER_ID_UNIT, strIdUnit );

        DefaultPluginActionResult result = new DefaultPluginActionResult(  );
        result.setRedirect( urlItem.getUrl(  ) );

        return result;
    }
}

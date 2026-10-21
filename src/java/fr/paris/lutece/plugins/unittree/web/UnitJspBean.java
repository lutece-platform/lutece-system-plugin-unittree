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
package fr.paris.lutece.plugins.unittree.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.unittree.business.action.UnitAction;
import fr.paris.lutece.plugins.unittree.business.action.UnitUserAction;
import fr.paris.lutece.plugins.unittree.business.unit.TreeUnit;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.rbac.UnittreeRBACRecursiveType;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitRemovalListener;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitUserService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitAttributeManager;
import fr.paris.lutece.plugins.unittree.service.unit.UnitResourceIdService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitUserAttributeManager;
import fr.paris.lutece.plugins.unittree.web.action.IUnitPluginAction;
import fr.paris.lutece.plugins.unittree.web.action.IUnitSearchFields;
import fr.paris.lutece.plugins.unittree.web.action.UnitUserSearchFields;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

/**
 *
 * UnitJspBean
 *
 */
@SessionScoped
@Named
public class UnitJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_UNITS = "UNITS_MANAGEMENT";
    private static final long serialVersionUID = 5997434357453313400L;

    // PROPERTIES
    private static final String PROPERTY_MANAGE_UNITS_PAGE_TITLE = "unittree.manageUnits.pageTitle";
    private static final String PROPERTY_CREATE_UNIT_PAGE_TITLE = "unittree.createUnit.pageTitle";
    private static final String PROPERTY_MODIFY_UNIT_PAGE_TITLE = "unittree.modifyUnit.pageTitle";
    private static final String PROPERTY_ADD_USERS_PAGE_TITLE = "unittree.addUsers.pageTitle";
    private static final String PROPERTY_MOVE_USER_PAGE_TITLE = "unittree.moveUser.pageTitle";
    private static final String PROPERTY_MOVE_UNIT_PAGE_TITLE = "unittree.moveSubTree.pageTitle";

    // MESSAGES
    private static final String MESSAGE_ERROR_NO_UNIT_SELECTED = "unittree.message.error.noUnitSelected";
    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "unittree.message.error.genericMessage";
    private static final String MESSAGE_ERROR_UNIT_NOT_FOUND = "unittree.message.error.unitNotFound";
    private static final String MESSAGE_ERROR_UNIT_HAS_SUB_UNITS = "unittree.message.error.unitHasSubUnits";
    private static final String MESSAGE_ERROR_USER_ALREADY_IN_UNIT = "unittree.message.error.userAlreadyInUnit";
    private static final String MESSAGE_ERROR_NO_SUB_UNITS = "unittree.message.error.noSubUnits";
    private static final String MESSAGE_DUPLICATE_CODE = "unittree.message.error.duplicateCode";
    private static final String MESSAGE_CONFIRM_REMOVE_UNIT = "unittree.message.removeUnit";
    private static final String MESSAGE_CONFIRM_REMOVE_USER = "unittree.message.removeUser";
    private static final String MESSAGE_ACCESS_DENIED = "unittree.message.accessDenied";
    private static final String MESSAGE_SUB_TREE_MOVED = "unittree.moveSubTree.subTreeMoved";
    private static final String MESSAGE_CANT_MOVE_SUB_TREE_TO_CHILD = "unittree.moveSubTree.cantMoveSubTreeToChild";

    // MARKS
    private static final String MARK_UNIT_TREE = "unitTree";
    private static final String MARK_PARENT_UNIT = "parentUnit";
    private static final String MARK_LIST_SUB_UNITS = "listSubUnits";
    private static final String MARK_LIST_UNIT_ACTIONS = "listUnitActions";
    private static final String MARK_LIST_UNIT_USER_ACTIONS = "listUnitUserActions";
    private static final String MARK_LIST_UNIT_USER_PLUGIN_ACTIONS = "listUnitUserPluginActions";
    private static final String MARK_UNIT = "unit";
    private static final String MARK_UNIT_TO_MOVE = "unitToMove";
    private static final String MARK_UNIT_PARENT = "unitParent";
    private static final String MARK_USER = "user";
    private static final String MARK_LIST_UNIT_ATTRIBUTES = "listUnitAttributes";
    private static final String MARK_LIST_UNIT_USER_ATTRIBUTES = "listUnitUserAttributes";
    private static final String MARK_MAP_ID_USER_UNIT = "mapIdUserUnit";
    private static final String MARK_MULTI_AFFECTATION_ENABLED = "multi_affection_enabled";
    private static final String MARK_FILTER_AFFECTED_USERS = "filterAffectedUsers";
    private static final String MARK_ADMIN_AVATAR = "adminAvatar";
    private static final String MARK_MAP_ID_USER_UNITS = "mapIdUserUnits";
    private static final String MARK_SELECTED_NODE_OPEN = "selectedNodeOpen";

    // PARAMETERS
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_ID_UNIT = "idUnit";
    private static final String PARAMETER_ID_PARENT = "idParent";
    private static final String PARAMETER_ID_USERS = "idUsers";
    private static final String PARAMETER_ID_USER = "idUser";
    private static final String PARAMETER_SESSION = "session";
    private static final String PARAMETER_SELECT_SUB_UNITS = "selectSubUnits";
    private static final String PARAMETER_ID_SELECTED_UNIT = "idSelectedUnit";
    private static final String PARAMETER_FILTER_AFFECTED_USERS = "filterAffectedUsers";
    private static final String PARAMETER_ID_UNIT_PARENT = "idUnitParent";
    private static final String PARAMETER_OPENNODE = "openNode";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_UNITS = "/admin/plugins/unittree/manage_units.html";
    private static final String TEMPLATE_CREATE_UNIT = "/admin/plugins/unittree/create_unit.html";
    private static final String TEMPLATE_MODIFY_UNIT = "/admin/plugins/unittree/modify_unit.html";
    private static final String TEMPLATE_ADD_USERS = "/admin/plugins/unittree/add_users.html";
    private static final String TEMPLATE_MOVE_USER = "/admin/plugins/unittree/move_user.html";
    private static final String TEMPLATE_MOVE_SUB_UNIT = "admin/plugins/unittree/move_sub_tree.html";

    // JSP
    private static final String JSP_MANAGE_UNITS = "ManageUnits.jsp";
    private static final String JSP_MOVE_USER = "MoveUser.jsp";
    private static final String JSP_URL_MANAGE_UNITS = "jsp/admin/plugins/unittree/ManageUnits.jsp";
    private static final String JSP_URL_CREATE_UNIT = "jsp/admin/plugins/unittree/CreateUnit.jsp";
    private static final String JSP_URL_MODIFY_UNIT = "jsp/admin/plugins/unittree/ModifyUnit.jsp";
    private static final String JSP_URL_MOVE_USER = "jsp/admin/plugins/unittree/MoveUser.jsp";
    private static final String JSP_URL_MOVE_SUB_TREE = "jsp/admin/plugins/unittree/MoveSubTree.jsp";
    private static final String JSP_URL_DO_REMOVE_UNIT = "jsp/admin/plugins/unittree/DoRemoveUnit.jsp";
    private static final String JSP_URL_ADD_USERS = "jsp/admin/plugins/unittree/AddUsers.jsp";
    private static final String JSP_URL_DO_REMOVE_USER = "jsp/admin/plugins/unittree/DoRemoveUser.jsp";
    private static final String JSP_URL_DO_CREATE_UNIT = "jsp/admin/plugins/unittree/DoCreateUnit.jsp";
    private static final String JSP_URL_DO_MODIFY_UNIT = "jsp/admin/plugins/unittree/DoModifyUnit.jsp";
    private static final String JSP_URL_DO_ADD_USERS = "jsp/admin/plugins/unittree/DoAddUsers.jsp";
    private static final String JSP_URL_DO_MOVE_USER = "jsp/admin/plugins/unittree/DoMoveUser.jsp";
    private static final String JSP_URL_DO_MOVE_SUB_TREE = "jsp/admin/plugins/unittree/DoMoveSubTree.jsp";

    private static final String METHOD_POST = "POST";

    private static final String OPENNODE_ENABLED = "1";

    // SERVICES
    @Inject
    private IUnitService _unitService;
    
    @Inject
    private IUnitUserService _unitUserService;
    
    private transient IUnitSearchFields _unitUserSearchFields = new UnitUserSearchFields( );

    // INSTANCE VARS
    private boolean _bAdminAvatar = PluginService.isPluginEnable( "adminavatar" );
    private TreeUnit _fullUnitTree;

    /**
     * Get manage units
     * 
     * @param request
     *            the HTTP request
     * @param response
     *            the response
     * @return the HTML code
     * @throws AccessDeniedException
     *             exception if there is the user does not have the permission
     */
    public IPluginActionResult getManageUnits( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_UNITS_PAGE_TITLE );

        // first - see if there is an invoked action
        IUnitPluginAction action = PluginActionManager.getPluginAction( request, IUnitPluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing unittree action {}", action.getName( ) );

            return action.process( request, response, getUser( ), _unitUserSearchFields );
        }

        // Get the selected unit
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        Unit unit = null;

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, false );
        }

        if ( unit == null )
        {
            unit = _unitService.getRootUnit( false );
        }

        // Check if there is some parameters in the session (for user search)
        if ( request.getParameter( PARAMETER_SESSION ) == null )
        {
            reInitSearchFields( request );
        }

        if ( _fullUnitTree == null )
        {
            initTreeUnit( request );
        }

        Map<String, Object> model = new HashMap<>( );

        // Add elements for user search form in the model
        Map<String, Unit> mapIdUserUnit = new HashMap<>( );
        _unitUserSearchFields.setInDepthSearch( request );

        List<AdminUser> listUsers = _unitUserService.getUsers( unit.getIdUnit( ), mapIdUserUnit, _unitUserSearchFields.isInDepthSearch( ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS;
        _unitUserSearchFields.fillModelForUserSearchForm( listUsers, strBaseUrl, request, model, unit );
        
        boolean bMultiAffectationEnabled = _unitUserService.isMultiAffectationEnabled( );
        if ( bMultiAffectationEnabled && CollectionUtils.isNotEmpty( listUsers ) )
        {
        	Map<String, List<Unit>> mapIdUserUnits = getUnitsByIdUser( listUsers );
        	model.put( MARK_MAP_ID_USER_UNITS, mapIdUserUnits );
        }

        model.put( MARK_UNIT_TREE, _fullUnitTree );
        model.put( MARK_UNIT, unit );
        model.put( MARK_LIST_SUB_UNITS, _unitService.getSubUnits( unit.getIdUnit( ), false ) );
        model.put( MARK_MAP_ID_USER_UNIT, mapIdUserUnit );
        model.put( MARK_ADMIN_AVATAR, _bAdminAvatar );
        model.put( MARK_MULTI_AFFECTATION_ENABLED, bMultiAffectationEnabled );
        model.put( MARK_SELECTED_NODE_OPEN, OPENNODE_ENABLED.equals( request.getParameter(PARAMETER_OPENNODE) ) );

        // Add actions in the model
        model.put( MARK_LIST_UNIT_ACTIONS,
                _unitService.getListActions( UnitAction.ACTION_TYPE, getLocale( ), unit, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) );
        model.put( MARK_LIST_UNIT_USER_ACTIONS,
                _unitService.getListActions( UnitUserAction.ACTION_TYPE, getLocale( ), unit, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) );
        PluginActionManager.fillModel( request, getUser( ), model, IUnitPluginAction.class, MARK_LIST_UNIT_USER_PLUGIN_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_UNITS, getLocale( ), model );

        IPluginActionResult result = new DefaultPluginActionResult( );
        result.setHtmlContent( getAdminPage( template.getHtml( ) ) );
        AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );

        return result;
    }

    /**
     * Get create unit
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException
     *             exception if the user does not have the permission
     */
    public String getCreateUnit( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_CREATE_UNIT_PAGE_TITLE );

        Unit unitParent = null;
        String strIdParent = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotBlank( strIdParent ) && StringUtils.isNumeric( strIdParent ) )
        {
            int nIdParent = Integer.parseInt( strIdParent );
            unitParent = _unitService.getUnit( nIdParent, false );
        }

        if ( unitParent == null )
        {
            unitParent = _unitService.getRootUnit( false );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unitParent, UnitResourceIdService.PERMISSION_CREATE, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE )
                || !_unitService.canCreateSubUnit( unitParent.getIdUnit( ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_PARENT_UNIT, unitParent );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_CREATE_UNIT ) );
        UnitAttributeManager.fillModel( request, getUser( ), model, MARK_LIST_UNIT_ATTRIBUTES );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_UNIT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Get modify unit
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException
     *             exception if the user does not have the permission
     */
    public String getModifyUnit( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MODIFY_UNIT_PAGE_TITLE );

        Unit unit = null;
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, true );
        }

        if ( unit == null )
        {
            throw new AppException( );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MODIFY, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        Unit parentUnit = _unitService.getUnit( unit.getIdParent( ), false );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_UNIT, unit );
        model.put( MARK_PARENT_UNIT, parentUnit );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_MODIFY_UNIT ) );
        UnitAttributeManager.fillModel( request, getUser( ), model, MARK_LIST_UNIT_ATTRIBUTES );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_UNIT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Get confirm remove unit
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     */
    public String getConfirmRemoveUnit( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_DELETE, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_ID_UNIT, strIdUnit );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_REMOVE_UNIT ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_UNIT, null, null, JSP_URL_DO_REMOVE_UNIT, null, AdminMessage.TYPE_CONFIRMATION,
                parameters, JSP_URL_MANAGE_UNITS );
    }

    /**
     * Get add users
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException
     *             exception if the user does not have the permission
     */
    public String getAddUsers( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_ADD_USERS_PAGE_TITLE );

        // Get the selected unit
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        Unit unit = null;

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, false );
        }

        if ( unit == null )
        {
            unit = _unitService.getRootUnit( false );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_ADD_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        if ( request.getParameter( PARAMETER_SESSION ) == null )
        {
            reInitSearchFields( request );
        }

        Map<String, Object> model = new HashMap<>( );

        boolean bMultiAffectationEnabled = _unitUserService.isMultiAffectationEnabled( );
        boolean bIncludeMultiAffectedUsers = bMultiAffectationEnabled && !Boolean.valueOf( request.getParameter( PARAMETER_FILTER_AFFECTED_USERS ) );
        List<AdminUser> listAvailableUsers = _unitUserService.getAvailableUsers( getUser( ), unit.getIdUnit( ), bIncludeMultiAffectedUsers );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ADD_USERS;

        _unitUserSearchFields.fillModelForUserSearchForm( listAvailableUsers, strBaseUrl, request, model, unit );

        model.put( MARK_UNIT, unit );
        model.put( MARK_MULTI_AFFECTATION_ENABLED, bMultiAffectationEnabled );
        model.put( MARK_FILTER_AFFECTED_USERS, !bIncludeMultiAffectedUsers );
        model.put( MARK_ADMIN_AVATAR, _bAdminAvatar );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_ADD_USERS ) );
        UnitUserAttributeManager.fillModel( request, getUser( ), model, MARK_LIST_UNIT_USER_ATTRIBUTES );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_USERS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Get move user
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException
     *             exception if the user does not have the permission
     */
    public String getMoveUser( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MOVE_USER_PAGE_TITLE );

        Unit unit = null;
        AdminUser user = null;
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );
        int nIdUnit;
        int nIdUser = Unit.ID_NULL;

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) && StringUtils.isNotBlank( strIdUser )
                && StringUtils.isNumeric( strIdUser ) )
        {
            nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, false );
            nIdUser = Integer.parseInt( strIdUser );
            user = _unitUserService.getUser( nIdUser );
        }

        if ( ( unit == null ) || ( user == null ) || !_unitUserService.isUserInUnit( nIdUser, unit.getIdUnit( ) ) )
        {
            throw new AppException( );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MOVE_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        List<Unit> listSubUnits = null;
        Unit unitParent = null;
        String strIdSelectedUnit = request.getParameter( PARAMETER_ID_SELECTED_UNIT );

        if ( StringUtils.isNotBlank( strIdSelectedUnit )
                && ( StringUtils.isNumeric( strIdSelectedUnit ) || Integer.toString( Unit.ID_NULL ).equals( strIdSelectedUnit ) ) )
        {
            int nIdSelectedUnit = Integer.parseInt( strIdSelectedUnit );
            Unit selectedUnit = _unitService.getUnit( nIdSelectedUnit, false );

            if ( selectedUnit != null )
            {
                unitParent = _unitService.getUnit( selectedUnit.getIdParent( ), false );
            }

            listSubUnits = _unitService.getSubUnits( nIdSelectedUnit, false );
        }

        if ( listSubUnits == null )
        {
            // We need to get the unit parent parent
            unitParent = _unitService.getUnit( unit.getIdParent( ), false );

            if ( unitParent != null )
            {
                unitParent = _unitService.getUnit( unitParent.getIdParent( ), false );
            }

            listSubUnits = _unitService.getSubUnits( unit.getIdParent( ), false );
        }

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_UNIT, unit );
        model.put( MARK_UNIT_PARENT, unitParent );
        model.put( MARK_USER, user );
        model.put( MARK_LIST_SUB_UNITS, listSubUnits );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_MOVE_USER ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MOVE_USER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Get confirm remove code mapping
     * 
     * @param request
     *            the HTTP request
     * @return the HTML code
     */
    public String getConfirmRemoveUser( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) || StringUtils.isBlank( strIdUser )
                || !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_REMOVE_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
        	return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_ID_UNIT, strIdUnit );
        parameters.put( PARAMETER_ID_USER, strIdUser );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_REMOVE_USER ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_USER, null, null, JSP_URL_DO_REMOVE_USER, null, AdminMessage.TYPE_CONFIRMATION,
                parameters, JSP_URL_MANAGE_UNITS );
    }

    // DO

    /**
     * Do create an unit
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doCreateUnit( HttpServletRequest request ) throws AccessDeniedException
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdParent = request.getParameter( PARAMETER_ID_PARENT );

        // The user has clicked on the cancel button => redirect to the manage page
        if ( StringUtils.isNotBlank( strCancel ) || StringUtils.isBlank( strIdParent ) || !StringUtils.isNumeric( strIdParent ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdParent );

            return url.getUrl( );
        }

        checkSecurityToken( request, JSP_URL_DO_CREATE_UNIT );

        int nIdParent = Integer.parseInt( strIdParent );

        // Check permissions
        if ( !_unitService.isAuthorized( strIdParent, UnitResourceIdService.PERMISSION_CREATE, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE )
                || !_unitService.canCreateSubUnit( nIdParent ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdParent ) );
        }

        Unit unit = new Unit( );

        // Populate the bean
        populate( unit, request );

        try
        {
            UnitAttributeManager.populate( unit, request );
        }
        catch( UnitErrorException ue )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, ue.getI18nErrorMessage( ), AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_CREATE_UNIT, nIdParent ) );
        }

        // Check mandatory fields
        Set<ConstraintViolation<Unit>> constraintViolations = BeanValidationUtil.validate( unit );

        if ( CollectionUtils.isNotEmpty( constraintViolations ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_CREATE_UNIT, nIdParent ) );
        }

        Unit sameCodeUnit = _unitService.getUnitByCode( unit.getCode(), false );
        if ( sameCodeUnit != null )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_DUPLICATE_CODE, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_CREATE_UNIT, nIdParent ) );
        }

        try
        {
            _unitService.createUnit( unit, request );
        }
        catch( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( "{} when creating an unit ", ex.getMessage( ), ex );
            // Revert
            _unitService.removeUnit( unit.getIdUnit( ), request );

            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdParent ) );
        }

        initTreeUnit( request );
        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, strIdParent );
        url.addParameter( PARAMETER_OPENNODE, OPENNODE_ENABLED );

        return url.getUrl( );
    }

    /**
     * Dp modify an unit
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyUnit( HttpServletRequest request ) throws AccessDeniedException
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        // The user has clicked on the cancel button => redirect to the manage page
        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl( );
        }

        checkSecurityToken( request, JSP_URL_DO_MODIFY_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP,
                    AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );

        Unit unit = _unitService.getUnit( nIdUnit, false );

        if ( unit == null )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_UNIT_NOT_FOUND, AdminMessage.TYPE_STOP,
                    AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MODIFY, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdUnit ) );
        }

        // Populate the bean
        populate( unit, request );

        try
        {
            UnitAttributeManager.populate( unit, request );
        }
        catch( UnitErrorException ue )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, ue.getI18nErrorMessage( ), AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MODIFY_UNIT, nIdUnit ) );
        }

        // Check mandatory fields
        Set<ConstraintViolation<Unit>> constraintViolations = BeanValidationUtil.validate( unit );

        if ( CollectionUtils.isNotEmpty( constraintViolations ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MODIFY_UNIT, nIdUnit ) );
        }

        Unit sameCodeUnit = _unitService.getUnitByCode( unit.getCode(), false );
        if ( sameCodeUnit !=null && sameCodeUnit.getIdUnit() != unit.getIdUnit() )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_DUPLICATE_CODE, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MODIFY_UNIT, nIdUnit ) );
        }

        try
        {
            _unitService.updateUnit( unit, request );
        }
        catch( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( "{} when modifying an unit ", ex.getMessage( ), ex );

            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdUnit ) );
        }

        initTreeUnit( request );
        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

        return url.getUrl( );
    }

    /**
     * Do remove an unit
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemoveUnit( HttpServletRequest request ) throws AccessDeniedException
    {
        checkRemovalRequest( request, JSP_URL_DO_REMOVE_UNIT );

        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_DELETE, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );
        int nIdParent = Unit.ID_ROOT;
        Unit unit = _unitService.getUnit( nIdUnit, false );

        if ( unit != null )
        {
            if ( _unitService.hasSubUnits( nIdUnit ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_HAS_SUB_UNITS, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
            }

            nIdParent = unit.getIdParent( );

            try
            {
                // Notify registered listener
                List<IUnitRemovalListener> listRemovalListener = CDI.current( ).select( IUnitRemovalListener.class ).stream( ).toList( );
                for ( IUnitRemovalListener removalLister : listRemovalListener )
                {
                    try
                    {
                        removalLister.notify( nIdUnit );
                    }
                    catch( UnitErrorException e )
                    {
                        return AdminMessageService.getMessageUrl( request, e.getI18nErrorMessage( ), JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
                    }
                }
                _unitService.removeUnit( nIdUnit, request );
            }
            catch( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( "{} when deleting an unit ", ex.getMessage( ), ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_ERROR );
            }
        }

        initTreeUnit( request );
        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdParent );
        url.addParameter(PARAMETER_OPENNODE, OPENNODE_ENABLED );

        return url.getUrl( );
    }

    /**
     * Dp add users
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doAddUsers( HttpServletRequest request ) throws AccessDeniedException
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl( );
        }

        checkSecurityToken( request, JSP_URL_DO_ADD_USERS );

        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_ADD_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
        	UrlItem url = new UrlItem( JSP_URL_ADD_USERS );
        	url.addParameter( PARAMETER_ID_UNIT, strIdUnit ); 
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, url.getUrl( ), AdminMessage.TYPE_STOP );
        }

        String [ ] listIdUsers = request.getParameterValues( PARAMETER_ID_USERS );

        if ( ( listIdUsers == null ) || ( listIdUsers.length == 0 ) || StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
        	UrlItem url = new UrlItem( JSP_URL_ADD_USERS );
        	url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, url.getUrl( ), AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );

        for ( String strIdUser : listIdUsers )
        {
            if ( StringUtils.isNotBlank( strIdUser ) && StringUtils.isNumeric( strIdUser ) )
            {
                int nIdUser = Integer.parseInt( strIdUser );

                if ( _unitUserService.isUserInUnit( nIdUser, nIdUnit ) )
                {
                	UrlItem url = new UrlItem( JSP_URL_ADD_USERS );
                	url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_USER_ALREADY_IN_UNIT, url.getUrl( ), AdminMessage.TYPE_STOP );
                }

                _unitUserService.doProcessAddUser( nIdUser, getUser( ), request );
                _unitUserService.addUserToUnit( nIdUnit, nIdUser );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl( );
    }

    /**
     * Do move user
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doMoveUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdSelectedUnit = request.getParameter( PARAMETER_ID_SELECTED_UNIT );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl( );
        }

        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) || StringUtils.isBlank( strIdSelectedUnit )
                || ( !StringUtils.isNumeric( strIdSelectedUnit ) && !Integer.toString( Unit.ID_NULL ).equals( strIdSelectedUnit ) )
                || StringUtils.isBlank( strIdUser ) || !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, strIdUnit ) );
        }

        // The navigation in the sub units only displays the page again
        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SELECT_SUB_UNITS ) ) )
        {
            checkSecurityToken( request, JSP_URL_DO_MOVE_USER );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_MOVE_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, strIdUnit ) );
        }

        // Check if the user has clicked on "selectSubUnits" => display the same page with the sub units
        String strSelectSubUnits = request.getParameter( PARAMETER_SELECT_SUB_UNITS );
        int nIdSelectedUnit = Integer.parseInt( strIdSelectedUnit );

        if ( StringUtils.isNotBlank( strSelectSubUnits ) )
        {
            // Check if the selected unit has sub units
            List<Unit> listSubUnits = _unitService.getSubUnits( nIdSelectedUnit, false );

            if ( ( listSubUnits == null ) || listSubUnits.isEmpty( ) )
            {
                return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_NO_SUB_UNITS, AdminMessage.TYPE_STOP,
                        getJspMoveUser( request, strIdUser, strIdUnit ) );
            }

            UrlItem url = new UrlItem( JSP_MOVE_USER );
            url.addParameter( PARAMETER_ID_USER, strIdUser );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
            url.addParameter( PARAMETER_ID_SELECTED_UNIT, strIdSelectedUnit );

            return url.getUrl( );
        }

        // The user must have the permission to move on both units (from and to)
        if ( !_unitService.isAuthorized( strIdSelectedUnit, UnitResourceIdService.PERMISSION_MOVE_USER, getUser( ),
                UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, strIdUnit ) );
        }

        int nIdUser = Integer.parseInt( strIdUser );
        int nIdUnit = Integer.parseInt( strIdUnit );
        AdminUser user = _unitUserService.getUser( nIdUser );
        Unit unit = _unitService.getUnit( nIdUnit, false );
        Unit selectedUnit = _unitService.getUnit( nIdSelectedUnit, false );

        if ( ( user != null ) && ( unit != null ) && ( selectedUnit != null ) )
        {
            try
            {
                // Remove the user from the unit
                _unitUserService.removeUserFromUnit( nIdUser, nIdUnit );
                _unitUserService.doProcessRemoveUser( nIdUser, getUser( ), request );
                // Then add the user to the new unit
                _unitUserService.addUserToUnit( nIdSelectedUnit, nIdUser );
                _unitUserService.doProcessAddUser( nIdUser, getUser( ), request );
            }
            catch( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( "{} when moving a user ", ex.getMessage( ), ex );

                return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR,
                        getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdUnit ) );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl( );
    }

    /**
     * Do remove user
     * 
     * @param request
     *            the HTTP request
     * @return the JSP return
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemoveUser( HttpServletRequest request ) throws AccessDeniedException
    {
        checkRemovalRequest( request, JSP_URL_DO_REMOVE_USER );

        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) || StringUtils.isBlank( strIdUser )
                || !StringUtils.isNumeric( strIdUser ) )
        {
        	return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_REMOVE_USER, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );
        int nIdUser = Integer.parseInt( strIdUser );
        Unit unit = _unitService.getUnit( nIdUnit, false );
        AdminUser user = _unitUserService.getUser( nIdUser );

        if ( ( unit != null ) && ( user != null ) )
        {
            try
            {
                _unitUserService.removeUserFromUnit( nIdUser, nIdUnit );
                _unitUserService.doProcessRemoveUser( nIdUser, getUser( ), request );
            }
            catch( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( "{} when deleting a user ", ex.getMessage( ), ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, JSP_URL_MANAGE_UNITS, AdminMessage.TYPE_ERROR );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl( );
    }

    /**
     * Get a page to change the parent of a unit
     * 
     * @param request
     *            The request
     * @return The HTML code to display
     * @throws AccessDeniedException
     *             if the user is not allowed to access the feature
     */
    public String getMoveSubTree( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MOVE_UNIT_PAGE_TITLE );

        String strIdUnitToMove = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT_PARENT );
        Unit unitToMove = _unitService.getUnit( Integer.parseInt( strIdUnitToMove ), false );
        int nIdUnit;

        if ( StringUtils.isNotBlank( strIdUnit ) )
        {
            nIdUnit = Integer.parseInt( strIdUnit );
        }
        else
        {
            nIdUnit = unitToMove.getIdParent( );
        }

        Unit unit = _unitService.getUnit( nIdUnit, false );

        // Check permissions
        if ( !_unitService.isAuthorized( unitToMove, UnitResourceIdService.PERMISSION_MOVE_UNIT, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        List<Unit> listSubUnits = null;

        if ( unit == null )
        {
            Unit rootUnit = _unitService.getRootUnit( false );
            listSubUnits = new ArrayList<>( );

            if ( unitToMove.getIdUnit( ) != rootUnit.getIdUnit( ) )
            {
                listSubUnits.add( rootUnit );
            }
        }
        else
        {
            listSubUnits = _unitService.getSubUnits( unit.getIdUnit( ), false );

            // We check that the unit to move is not contained in the sub unit list.
            for ( Unit subUnit : listSubUnits )
            {
                if ( subUnit.getIdUnit( ) == unitToMove.getIdUnit( ) )
                {
                    listSubUnits.remove( subUnit );

                    break;
                }
            }
        }

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_UNIT_TO_MOVE, unitToMove );
        model.put( MARK_UNIT, unit );
        model.put( MARK_LIST_SUB_UNITS, listSubUnits );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, JSP_URL_DO_MOVE_SUB_TREE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MOVE_SUB_UNIT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Do change the parent of a unit
     * 
     * @param request
     *            The request
     * @return The next URL to display
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doMoveSubTree( HttpServletRequest request ) throws AccessDeniedException
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnitToMove = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotEmpty( strCancel ) )
        {
            UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS );
            urlItem.addParameter( PARAMETER_ID_UNIT, strIdUnitToMove );

            return urlItem.getUrl( );
        }

        checkSecurityToken( request, JSP_URL_DO_MOVE_SUB_TREE );

        String strIdUnitParent = request.getParameter( PARAMETER_ID_UNIT_PARENT );
        if ( StringUtils.isBlank( strIdUnitParent ) || !StringUtils.isNumeric( strIdUnitParent ) || StringUtils.isBlank( strIdUnitToMove )
                || !StringUtils.isNumeric( strIdUnitToMove ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_NO_UNIT_SELECTED, AdminMessage.TYPE_ERROR,
                    getJspOfUnit( request, JSP_URL_MOVE_SUB_TREE, strIdUnitToMove ) );
        }

        int nIdUnitToMove = Integer.parseInt( strIdUnitToMove );
        int nIdUnitParent = Integer.parseInt( strIdUnitParent );
        Unit unitToMove = _unitService.getUnit( nIdUnitToMove, false );
        Unit unitParent = _unitService.getUnit( nIdUnitParent, false );

        if ( ( unitToMove == null ) || ( unitParent == null ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_ERROR_UNIT_NOT_FOUND, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdUnitToMove ) );
        }

        // Check permissions, as the move page does
        if ( !_unitService.isAuthorized( unitToMove, UnitResourceIdService.PERMISSION_MOVE_UNIT, getUser( ), UnittreeRBACRecursiveType.PARENT_RECURSIVE ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP,
                    getJspOfUnit( request, JSP_URL_MANAGE_UNITS, nIdUnitToMove ) );
        }

        if ( _unitService.moveSubTree( unitToMove, unitParent ) )
        {
            UrlItem urlItem = new UrlItem( JSP_URL_MANAGE_UNITS );
            urlItem.addParameter( PARAMETER_ID_UNIT, unitToMove.getIdUnit( ) );

            initTreeUnit( request );
            return AdminMessageService.getMessageUrl( request, MESSAGE_SUB_TREE_MOVED, urlItem.getUrl( ), AdminMessage.TYPE_INFO );
        }

        return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_CANT_MOVE_SUB_TREE_TO_CHILD, AdminMessage.TYPE_ERROR,
                getJspOfUnit( request, JSP_URL_MOVE_SUB_TREE, nIdUnitToMove ) );
    }

    // PRIVATE METHODS

    /**
     * Return the url of a jsp of a unit
     * 
     * @param request
     *            the HTTP request
     * @param strJspUrl
     *            the url of the jsp, relative to the webapp
     * @param nIdUnit
     *            the id of the unit
     * @return the url of the jsp of the unit
     */
    private String getJspOfUnit( HttpServletRequest request, String strJspUrl, int nIdUnit )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + strJspUrl );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl( );
    }

    /**
     * Return the url of a jsp of a unit, or of the jsp manage units when the id of the unit is not a number
     * 
     * @param request
     *            the HTTP request
     * @param strJspUrl
     *            the url of the jsp, relative to the webapp
     * @param strIdUnit
     *            the id of the unit
     * @return the url of the jsp of the unit
     */
    private String getJspOfUnit( HttpServletRequest request, String strJspUrl, String strIdUnit )
    {
        int nIdUnit = NumberUtils.toInt( strIdUnit, Unit.ID_NULL );

        if ( nIdUnit == Unit.ID_NULL )
        {
            return AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS;
        }

        return getJspOfUnit( request, strJspUrl, nIdUnit );
    }

    /**
     * Return the url of the jsp move user
     * 
     * @param request
     *            the HTTP request
     * @param strIdUser
     *            the id of the user
     * @param strIdUnit
     *            the id of the unit of the user
     * @return the url of the jsp move user
     */
    private String getJspMoveUser( HttpServletRequest request, String strIdUser, String strIdUnit )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MOVE_USER );
        url.addParameter( PARAMETER_ID_USER, NumberUtils.toInt( strIdUser, Unit.ID_NULL ) );
        url.addParameter( PARAMETER_ID_UNIT, NumberUtils.toInt( strIdUnit, Unit.ID_NULL ) );

        return url.getUrl( );
    }

    /**
     * Check the security token of an action
     * 
     * @param request
     *            the HTTP request
     * @param strAction
     *            the action (key of the token)
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    private void checkSecurityToken( HttpServletRequest request, String strAction ) throws AccessDeniedException
    {
        if ( !getSecurityTokenService( ).validate( request, strAction ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
    }

    /**
     * Check that a removal is posted from its confirmation page, with its security token
     * 
     * @param request
     *            the HTTP request
     * @param strAction
     *            the action (key of the token)
     * @throws AccessDeniedException
     *             if the request is not a POST or if the security token is invalid
     */
    private void checkRemovalRequest( HttpServletRequest request, String strAction ) throws AccessDeniedException
    {
        if ( !METHOD_POST.equalsIgnoreCase( request.getMethod( ) ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        checkSecurityToken( request, strAction );
    }

    /**
     * Reinit the search fields
     * 
     * @param request
     *            the HTTP request
     */
    private void reInitSearchFields( HttpServletRequest request )
    {
        _unitUserSearchFields = new UnitUserSearchFields( request );
    }

    /**
     * Reinit the tree
     * 
     * @param request
     *            the HTTP request
     */
    private void initTreeUnit( HttpServletRequest request )
    {
        _fullUnitTree = new TreeUnit( _unitService.getRootUnit( false ) );
        _unitService.populateTreeUnit( _fullUnitTree, AdminUserService.getAdminUser( request ), false );
    }
    
    /**
     * Get the units by id user
     * 
     * @return The units by id user
     */
    private Map<String, List<Unit>> getUnitsByIdUser( List<AdminUser> listUsers )
    {
    	Map<String, List<Unit>> mapIdUserUnits = new HashMap<>( );

    	for ( AdminUser user : listUsers )
    	{
    		List<Unit> listUnits = _unitService.getUnitsByIdUser( user.getUserId( ), false );
    		
    		if ( CollectionUtils.isNotEmpty( listUnits ) )
    		{
    			mapIdUserUnits.put( String.valueOf( user.getUserId( ) ), listUnits );
    		}
    	}

        return mapIdUserUnits;
    }
}

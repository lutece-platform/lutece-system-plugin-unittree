/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.unittree.business.action.UnitAction;
import fr.paris.lutece.plugins.unittree.business.action.UnitUserAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitUserService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitResourceIdService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitUserAttributeManager;
import fr.paris.lutece.plugins.unittree.web.action.IUnitPluginAction;
import fr.paris.lutece.plugins.unittree.web.action.IUnitSearchFields;
import fr.paris.lutece.plugins.unittree.web.action.UnitUserSearchFields;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.UniqueIDGenerator;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;

import javax.xml.transform.Source;


/**
 *
 * UnitJspBean
 *
 */
public class UnitJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_UNITS = "UNITS_MANAGEMENT";

    // BEAN
    private static final String BEAN_UNIT_USER_SERVICE = "unittree.unitUserService";
    private static final String BEAN_SECTOR_SERVICE = "unittree.sectorService";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_UNITS_PAGE_TITLE = "unittree.manageUnits.pageTitle";
    private static final String PROPERTY_CREATE_UNIT_PAGE_TITLE = "unittree.createUnit.pageTitle";
    private static final String PROPERTY_MODIFY_UNIT_PAGE_TITLE = "unittree.modifyUnit.pageTitle";
    private static final String PROPERTY_ADD_USERS_PAGE_TITLE = "unittree.addUsers.pageTitle";
    private static final String PROPERTY_MODIFY_USER_PAGE_TITLE = "unittree.modifyUser.pageTitle";
    private static final String PROPERTY_MOVE_USER_PAGE_TITLE = "unittree.moveUser.pageTitle";

    // MESSAGES
    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "unittree.message.error.genericMessage";
    private static final String MESSAGE_ERROR_UNIT_NOT_FOUND = "unittree.message.error.unitNotFound";
    private static final String MESSAGE_ERROR_UNIT_HAS_SUB_UNITS = "unittree.message.error.unitHasSubUnits";
    private static final String MESSAGE_ERROR_USER_ALREADY_IN_AN_UNIT = "unittree.message.error.userAlreadyInAnUnit";
    private static final String MESSAGE_ERROR_NO_SUB_UNITS = "unittree.message.error.noSubUnits";
    private static final String MESSAGE_CONFIRM_REMOVE_UNIT = "unittree.message.removeUnit";
    private static final String MESSAGE_CONFIRM_REMOVE_USER = "unittree.message.removeUser";
    private static final String MESSAGE_ACCESS_DENIED = "unittree.message.accessDenied";

    // MARKS
    private static final String MARK_UNIT_TREE = "unitTree";
    private static final String MARK_PARENT_UNIT = "parentUnit";
    private static final String MARK_LIST_SUB_UNITS = "listSubUnits";
    private static final String MARK_LIST_UNIT_ACTIONS = "listUnitActions";
    private static final String MARK_LIST_UNIT_USER_ACTIONS = "listUnitUserActions";
    private static final String MARK_LIST_UNIT_USER_PLUGIN_ACTIONS = "listUnitUserPluginActions";
    private static final String MARK_UNIT = "unit";
    private static final String MARK_USER = "user";
    private static final String MARK_LIST_UNIT_USER_ATTRIBUTES = "listUnitUserAttributes";
    private static final String MARK_LIST_SECTORS = "listSectors";
    private static final String MARK_HAS_SUB_UNITS = "hasSubUnits";

    // PARAMETERS
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_ID_UNIT = "idUnit";
    private static final String PARAMETER_ID_PARENT = "idParent";
    private static final String PARAMETER_ID_USERS = "idUsers";
    private static final String PARAMETER_ID_USER = "idUser";
    private static final String PARAMETER_SESSION = "session";
    private static final String PARAMETER_SELECT_SUB_UNITS = "selectSubUnits";
    private static final String PARAMETER_ID_SELECTED_UNIT = "idSelectedUnit";
    private static final String PARAMETER_ID_SECTOR = "idSector";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_UNITS = "/admin/plugins/unittree/manage_units.html";
    private static final String TEMPLATE_CREATE_UNIT = "/admin/plugins/unittree/create_unit.html";
    private static final String TEMPLATE_MODIFY_UNIT = "/admin/plugins/unittree/modify_unit.html";
    private static final String TEMPLATE_ADD_USERS = "/admin/plugins/unittree/add_users.html";
    private static final String TEMPLATE_MODIFY_USER = "/admin/plugins/unittree/modify_user.html";
    private static final String TEMPLATE_MOVE_USER = "/admin/plugins/unittree/move_user.html";

    // JSP
    private static final String JSP_MANAGE_UNITS = "ManageUnits.jsp";
    private static final String JSP_MOVE_USER = "MoveUser.jsp";
    private static final String JSP_URL_MANAGE_UNITS = "jsp/admin/plugins/unittree/ManageUnits.jsp";
    private static final String JSP_URL_DO_REMOVE_UNIT = "jsp/admin/plugins/unittree/DoRemoveUnit.jsp";
    private static final String JSP_URL_ADD_USERS = "jsp/admin/plugins/unittree/AddUsers.jsp";
    private static final String JSP_URL_DO_REMOVE_USER = "jsp/admin/plugins/unittree/DoRemoveUser.jsp";

    // XSL
    private static final String UNIT_TREE_XSL_UNIQUE_PREFIX = UniqueIDGenerator.getNewId(  ) + "SpacesTree";
    private static final String XSL_PARAMETER_ID_CURRENT_UNIT = "id-current-unit";

    // SERVICES
    private IUnitService _unitService = (IUnitService) SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
    private IUnitUserService _unitUserService = (IUnitUserService) SpringContextService.getBean( BEAN_UNIT_USER_SERVICE );
    private ISectorService _sectorService = (ISectorService) SpringContextService.getBean( BEAN_SECTOR_SERVICE );
    private IUnitSearchFields _unitUserSearchFields = new UnitUserSearchFields(  );

    // GET

    /**
     * Get manage units
     * @param request the HTTP request
     * @param response the response
     * @return the HTML code
     * @throws AccessDeniedException exception if there is the user does not have the permission
     */
    public IPluginActionResult getManageUnits( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_UNITS_PAGE_TITLE );

        // first - see if there is an invoked action
        IUnitPluginAction action = PluginActionManager.getPluginAction( request, IUnitPluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing unittree action " + action.getName(  ) );

            return action.process( request, response, getUser(  ), _unitUserSearchFields );
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

        // Build the html for units tree
        String strXmlUnits = _unitService.getXMLUnits(  );
        Source sourceXsl = _unitService.getTreeXsl(  );
        Map<String, String> htXslParameters = new HashMap<String, String>(  );
        htXslParameters.put( XSL_PARAMETER_ID_CURRENT_UNIT, Integer.toString( unit.getIdUnit(  ) ) );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strHtmlUnitsTree = xmlTransformerService.transformBySourceWithXslCache( strXmlUnits, sourceXsl,
                UNIT_TREE_XSL_UNIQUE_PREFIX, htXslParameters, null );

        Map<String, Object> model = new HashMap<String, Object>(  );

        // Add elements for user search form in the model
        List<AdminUser> listUsers = _unitUserService.getUsers( unit.getIdUnit(  ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_UNITS;
        _unitUserSearchFields.fillModelForUserSearchForm( listUsers, strBaseUrl, request, model, unit );

        model.put( MARK_UNIT_TREE, strHtmlUnitsTree );
        model.put( MARK_UNIT, unit );
        model.put( MARK_LIST_SUB_UNITS, _unitService.getSubUnits( unit.getIdUnit(  ), false ) );

        // Add actions in the model
        model.put( MARK_LIST_UNIT_ACTIONS,
            _unitService.getListActions( UnitAction.ACTION_TYPE, getLocale(  ), unit, getUser(  ) ) );
        model.put( MARK_LIST_UNIT_USER_ACTIONS,
            _unitService.getListActions( UnitUserAction.ACTION_TYPE, getLocale(  ), unit, getUser(  ) ) );
        PluginActionManager.fillModel( request, getUser(  ), model, IUnitPluginAction.class,
            MARK_LIST_UNIT_USER_PLUGIN_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_UNITS, getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );
        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );
        AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );

        return result;
    }

    /**
     * Get create unit
     * @param request the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException exception if the user does not have the permission
     */
    public String getCreateUnit( HttpServletRequest request )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_CREATE_UNIT_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

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
        if ( !_unitService.isAuthorized( unitParent, UnitResourceIdService.PERMISSION_CREATE, getUser(  ) ) ||
                !_unitService.canCreateSubUnit( unitParent.getIdUnit(  ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        model.put( MARK_PARENT_UNIT, unitParent );
        model.put( MARK_LIST_SECTORS, _sectorService.findAll(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_UNIT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get modify unit
     * @param request the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException exception if the user does not have the permission
     */
    public String getModifyUnit( HttpServletRequest request )
        throws AccessDeniedException
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
            throw new AppException(  );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MODIFY, getUser(  ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        Unit parentUnit = _unitService.getUnit( unit.getIdParent(  ), false );
        boolean bHasSubUnits = _unitService.hasSubUnits( unit.getIdUnit(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_UNIT, unit );
        model.put( MARK_PARENT_UNIT, parentUnit );

        if ( !bHasSubUnits )
        {
            // If the unit does not have sub units, then display all sectors
            model.put( MARK_LIST_SECTORS, _sectorService.findAll(  ) );
        }
        else
        {
            // Otherwise, only display the associated sectors
            model.put( MARK_LIST_SECTORS, _sectorService.findByIdUnit( unit.getIdUnit(  ) ) );
        }

        model.put( MARK_HAS_SUB_UNITS, bHasSubUnits );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_UNIT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get confirm remove unit
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getConfirmRemoveUnit( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_UNIT );
        url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_UNIT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Get add users
     * @param request the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException exception if the user does not have the permission
     */
    public String getAddUsers( HttpServletRequest request )
        throws AccessDeniedException
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
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_ADD_USER, getUser(  ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        if ( request.getParameter( PARAMETER_SESSION ) == null )
        {
            reInitSearchFields( request );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );

        List<AdminUser> listAvailableUsers = _unitUserService.getAvailableUsers( getUser(  ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ADD_USERS;

        _unitUserSearchFields.fillModelForUserSearchForm( listAvailableUsers, strBaseUrl, request, model, unit );

        model.put( MARK_UNIT, unit );
        UnitUserAttributeManager.fillModel( request, getUser(  ), model, MARK_LIST_UNIT_USER_ATTRIBUTES );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get modify the user
     * @param request the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException exception if the user does not have the permission
     */
    public String getModifyUser( HttpServletRequest request )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MODIFY_USER_PAGE_TITLE );

        Unit unit = null;
        AdminUser user = null;
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) &&
                StringUtils.isNotBlank( strIdUser ) && StringUtils.isNumeric( strIdUser ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, false );

            int nIdUser = Integer.parseInt( strIdUser );
            user = _unitUserService.getUser( nIdUser );
        }

        if ( ( unit == null ) || ( user == null ) )
        {
            throw new AppException(  );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MODIFY_USER, getUser(  ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_UNIT, unit );
        model.put( MARK_USER, user );
        UnitUserAttributeManager.fillModel( request, getUser(  ), model, MARK_LIST_UNIT_USER_ATTRIBUTES );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get move user
     * @param request the HTTP request
     * @return the HTML code
     * @throws AccessDeniedException exception if the user does not have the permission
     */
    public String getMoveUser( HttpServletRequest request )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MOVE_USER_PAGE_TITLE );

        Unit unit = null;
        AdminUser user = null;
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );
        int nIdUnit = Unit.ID_NULL;
        int nIdUser = Unit.ID_NULL;

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) &&
                StringUtils.isNotBlank( strIdUser ) && StringUtils.isNumeric( strIdUser ) )
        {
            nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit, false );
            nIdUser = Integer.parseInt( strIdUser );
            user = _unitUserService.getUser( nIdUser );
        }

        if ( ( unit == null ) || ( user == null ) || !_unitUserService.isUserInUnit( nIdUser ) )
        {
            throw new AppException(  );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MOVE_USER, getUser(  ) ) )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        ReferenceList listSubUnits = null;
        String strIdSelectedUnit = request.getParameter( PARAMETER_ID_SELECTED_UNIT );

        if ( StringUtils.isNotBlank( strIdSelectedUnit ) &&
                ( StringUtils.isNumeric( strIdSelectedUnit ) ||
                Integer.toString( Unit.ID_NULL ).equals( strIdSelectedUnit ) ) )
        {
            int nIdSelectedUnit = Integer.parseInt( strIdSelectedUnit );
            listSubUnits = _unitService.getSubUnitsAsReferenceList( nIdSelectedUnit, getLocale(  ) );
        }

        if ( listSubUnits == null )
        {
            listSubUnits = _unitService.getSubUnitsAsReferenceList( unit.getIdParent(  ), getLocale(  ) );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_UNIT, unit );
        model.put( MARK_USER, user );
        model.put( MARK_LIST_SUB_UNITS, listSubUnits );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MOVE_USER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get confirm remove code mapping
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getConfirmRemoveUser( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) ||
                StringUtils.isBlank( strIdUser ) || !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_REMOVE_USER, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_USER );
        url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
        url.addParameter( PARAMETER_ID_USER, strIdUser );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_USER, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    // DO

    /**
     * Do create an unit
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doCreateUnit( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdParent = request.getParameter( PARAMETER_ID_PARENT );

        if ( StringUtils.isNotBlank( strCancel ) || StringUtils.isBlank( strIdParent ) ||
                !StringUtils.isNumeric( strIdParent ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdParent );

            return url.getUrl(  );
        }

        int nIdParent = Integer.parseInt( strIdParent );

        // Check permissions
        if ( !_unitService.isAuthorized( strIdParent, UnitResourceIdService.PERMISSION_CREATE, getUser(  ) ) ||
                !_unitService.canCreateSubUnit( nIdParent ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        Unit unit = new Unit(  );
        // Populate the bean
        populate( unit, request );

        // Check mandatory fields
        Set<ConstraintViolation<Unit>> constraintViolations = BeanValidationUtil.validate( unit );

        if ( constraintViolations.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Ids sector
        String[] strIdsSectors = request.getParameterValues( PARAMETER_ID_SECTOR );

        if ( ( strIdsSectors != null ) && ( strIdsSectors.length > 0 ) )
        {
            for ( String strIdSector : strIdsSectors )
            {
                if ( StringUtils.isNotBlank( strIdSector ) && StringUtils.isNumeric( strIdSector ) )
                {
                    int nIdSector = Integer.parseInt( strIdSector );
                    unit.addIdSector( nIdSector );
                }
            }
        }

        try
        {
            _unitService.createUnit( unit );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when creating an unit ", ex );
            // Revert
            _unitService.removeUnit( unit.getIdUnit(  ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, unit.getIdUnit(  ) );

        return url.getUrl(  );
    }

    /**
     * Dp modify an unit
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doModifyUnit( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl(  );
        }

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );

        // Do no get the sectors because the list will be deleted, and we store the new id sectors
        Unit unit = _unitService.getUnit( nIdUnit, false );

        if ( unit == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( unit, UnitResourceIdService.PERMISSION_MODIFY, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        // Populate the bean
        populate( unit, request );

        // Check mandatory fields
        Set<ConstraintViolation<Unit>> constraintViolations = BeanValidationUtil.validate( unit );

        if ( constraintViolations.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Ids sector
        String[] strIdsSectors = request.getParameterValues( PARAMETER_ID_SECTOR );

        if ( ( strIdsSectors != null ) && ( strIdsSectors.length > 0 ) )
        {
            for ( String strIdSector : strIdsSectors )
            {
                if ( StringUtils.isNotBlank( strIdSector ) && StringUtils.isNumeric( strIdSector ) )
                {
                    int nIdSector = Integer.parseInt( strIdSector );
                    unit.addIdSector( nIdSector );
                }
            }
        }

        try
        {
            _unitService.updateUnit( unit );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when modifying an unit ", ex );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, unit.getIdUnit(  ) );

        return url.getUrl(  );
    }

    /**
     * Do remove an unit
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doRemoveUnit( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );
        int nIdParent = Unit.ID_ROOT;
        Unit unit = _unitService.getUnit( nIdUnit, false );

        if ( unit != null )
        {
            if ( _unitService.hasSubUnits( nIdUnit ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_UNIT_HAS_SUB_UNITS,
                    AdminMessage.TYPE_STOP );
            }

            nIdParent = unit.getIdParent(  );

            try
            {
                _unitService.removeUnit( nIdUnit );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when deleting an unit ", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdParent );

        return url.getUrl(  );
    }

    /**
     * Dp add users
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doAddUsers( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl(  );
        }

        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_ADD_USER, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String[] listIdUsers = request.getParameterValues( PARAMETER_ID_USERS );

        if ( ( listIdUsers == null ) || ( listIdUsers.length == 0 ) || StringUtils.isBlank( strIdUnit ) ||
                !StringUtils.isNumeric( strIdUnit ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );

        for ( String strIdUser : listIdUsers )
        {
            if ( StringUtils.isNotBlank( strIdUser ) && StringUtils.isNumeric( strIdUser ) )
            {
                int nIdUser = Integer.parseInt( strIdUser );

                if ( _unitUserService.isUserInUnit( nIdUser ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_USER_ALREADY_IN_AN_UNIT,
                        AdminMessage.TYPE_STOP );
                }
                else
                {
                    _unitUserService.doProcessAddUser( nIdUser, getUser(  ), request );
                    _unitUserService.addUserToUnit( nIdUnit, nIdUser );
                }
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl(  );
    }

    /**
     * Do modify user
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doModifyUser( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) ||
                StringUtils.isBlank( strIdUser ) || !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_MODIFY_USER, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );
        int nIdUser = Integer.parseInt( strIdUser );
        Unit unit = _unitService.getUnit( nIdUnit, false );
        AdminUser user = _unitUserService.getUser( nIdUser );

        if ( ( unit != null ) && ( user != null ) )
        {
            _unitUserService.doProcessModifyUser( nIdUser, getUser(  ), request );
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl(  );
    }

    /**
     * Do move user
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doMoveUser( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdSelectedUnit = request.getParameter( PARAMETER_ID_SELECTED_UNIT );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );

            return url.getUrl(  );
        }

        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) ||
                StringUtils.isBlank( strIdSelectedUnit ) ||
                ( !StringUtils.isNumeric( strIdSelectedUnit ) &&
                !Integer.toString( Unit.ID_NULL ).equals( strIdSelectedUnit ) ) || StringUtils.isBlank( strIdUser ) ||
                !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_MOVE_USER, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        // Check if the user has clicked on "selectSubUnits" => display the same page with the sub units
        String strSelectSubUnits = request.getParameter( PARAMETER_SELECT_SUB_UNITS );
        int nIdSelectedUnit = Integer.parseInt( strIdSelectedUnit );

        if ( StringUtils.isNotBlank( strSelectSubUnits ) )
        {
            // Check if the selected unit has sub units
            List<Unit> listSubUnits = _unitService.getSubUnits( nIdSelectedUnit, false );

            if ( ( listSubUnits == null ) || listSubUnits.isEmpty(  ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_SUB_UNITS, AdminMessage.TYPE_STOP );
            }

            UrlItem url = new UrlItem( JSP_MOVE_USER );
            url.addParameter( PARAMETER_ID_USER, strIdUser );
            url.addParameter( PARAMETER_ID_UNIT, strIdUnit );
            url.addParameter( PARAMETER_ID_SELECTED_UNIT, strIdSelectedUnit );

            return url.getUrl(  );
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
                _unitUserService.removeUserFromUnit( nIdUser );
                _unitUserService.doProcessRemoveUser( nIdUser, getUser(  ), request );
                // Then add the user to the new unit
                _unitUserService.addUserToUnit( nIdSelectedUnit, nIdUser );
                _unitUserService.doProcessAddUser( nIdUser, getUser(  ), request );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when deleting an unit ", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl(  );
    }

    /**
     * Do remove user
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doRemoveUser( HttpServletRequest request )
    {
        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        String strIdUser = request.getParameter( PARAMETER_ID_USER );

        if ( StringUtils.isBlank( strIdUnit ) || !StringUtils.isNumeric( strIdUnit ) ||
                StringUtils.isBlank( strIdUser ) || !StringUtils.isNumeric( strIdUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permissions
        if ( !_unitService.isAuthorized( strIdUnit, UnitResourceIdService.PERMISSION_REMOVE_USER, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        int nIdUnit = Integer.parseInt( strIdUnit );
        int nIdUser = Integer.parseInt( strIdUser );
        Unit unit = _unitService.getUnit( nIdUnit, false );
        AdminUser user = _unitUserService.getUser( nIdUser );

        if ( ( unit != null ) && ( user != null ) )
        {
            try
            {
                _unitUserService.removeUserFromUnit( nIdUser );
                _unitUserService.doProcessRemoveUser( nIdUser, getUser(  ), request );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when deleting an unit ", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }
        }

        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, nIdUnit );

        return url.getUrl(  );
    }

    // PRIVATE METHODS

    /**
     * Reinit the search fields
     * @param request the HTTP request
     */
    private void reInitSearchFields( HttpServletRequest request )
    {
        _unitUserSearchFields = new UnitUserSearchFields( request );
    }
}

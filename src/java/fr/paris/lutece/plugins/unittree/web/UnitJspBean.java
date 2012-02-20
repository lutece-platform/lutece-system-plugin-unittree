/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.xml.transform.Source;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.unittree.business.Unit;
import fr.paris.lutece.plugins.unittree.service.IUnitService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.UniqueIDGenerator;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;


/**
 *
 * UnitJspBean
 *
 */
public class UnitJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_UNITS = "UNITS_MANAGEMENT";

    // BEAN
    private static final String BEAN_UNIT_SERVICE = "unittree.unitService";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_UNITS_PAGE_TITLE = "unittree.manageUnits.pageTitle";
    private static final String PROPERTY_ERRROR_PAGE_TITLE = "unittree.error.pageTitle";
    private static final String PROPERTY_CREATE_UNIT_PAGE_TITLE = "unittree.createUnit.pageTitle";

    // MESSAGES
    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "unittree.message.error.genericMessage";

    // MARKS
    private static final String MARK_ERROR_MESSAGE = "errorMessage";
    private static final String MARK_ID_CURRENT_UNIT = "idCurrentUnit";
    private static final String MARK_UNIT_TREE = "unitTree";
    private static final String MARK_ID_PARENT = "idParent";

    // PARAMETERS
    private static final String PARAMETER_ERROR_MESSAGE = "errorMessage";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_ID_UNIT = "idUnit";
    private static final String PARAMETER_ID_PARENT = "idParent";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_UNITS = "/admin/plugins/unittree/manage_units.html";
    private static final String TEMPLATE_ERROR = "/admin/plugins/unittree/error.html";
    private static final String TEMPLATE_CREATE_UNIT = "/admin/plugins/unittree/create_unit.html";

    // JSP
    private static final String JSP_MANAGE_UNITS = "ManageUnits.jsp";
    private static final String UNIT_TREE_XSL_UNIQUE_PREFIX = UniqueIDGenerator.getNewId(  ) + "SpacesTree";
    private static final String XSL_PARAMETER_ID_CURRENT_UNIT = "id-current-unit";

    // SERVICES
    private IUnitService _unitService = (IUnitService) SpringContextService.getBean( BEAN_UNIT_SERVICE );

    // GET

    /**
     * Get manage code mappings
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getManageUnits( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_UNITS_PAGE_TITLE );

        String strIdUnit = request.getParameter( PARAMETER_ID_UNIT );
        Unit unit = null;

        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            unit = _unitService.getUnit( nIdUnit );
        }

        if ( unit == null )
        {
            unit = _unitService.getRootUnit(  );
        }

        // Spaces
        String strXmlSpaces = _unitService.getXMLUnits(  );
        Source sourceXsl = _unitService.getTreeXsl(  );
        Map<String, String> htXslParameters = new HashMap<String, String>(  );
        htXslParameters.put( XSL_PARAMETER_ID_CURRENT_UNIT, Integer.toString( unit.getIdUnit(  ) ) );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strSpacesTree = xmlTransformerService.transformBySourceWithXslCache( strXmlSpaces, sourceXsl,
                UNIT_TREE_XSL_UNIQUE_PREFIX, htXslParameters, null );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_UNIT_TREE, strSpacesTree );
        model.put( MARK_ID_CURRENT_UNIT, unit.getIdUnit(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_UNITS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String getCreateUnit( HttpServletRequest request )
    {
        // TODO : Check permission
        setPageTitleProperty( PROPERTY_CREATE_UNIT_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

        boolean bHasParent = false;
        String strIdParent = request.getParameter( PARAMETER_ID_PARENT );

        if ( StringUtils.isNotBlank( strIdParent ) && StringUtils.isNumeric( strIdParent ) )
        {
            int nIdParent = Integer.parseInt( strIdParent );
            Unit unitParent = _unitService.getUnit( nIdParent );

            if ( unitParent != null )
            {
                model.put( MARK_ID_PARENT, strIdParent );
                bHasParent = true;
            }
        }

        if ( !bHasParent )
        {
            model.put( MARK_ID_PARENT, Unit.ID_ROOT );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_UNIT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get error
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getError( HttpServletRequest request )
    {
        String strErrorMessage = request.getParameter( PARAMETER_ERROR_MESSAGE );

        if ( StringUtils.isBlank( strErrorMessage ) )
        {
            strErrorMessage = I18nService.getLocalizedString( MESSAGE_ERROR_GENERIC_MESSAGE, request.getLocale(  ) );
        }

        return getError( request, strErrorMessage );
    }

    /**
     * Get error
     * @param request the HTTP request
     * @param strErrorMessage the error message
     * @return the HTML code
     */
    public String getError( HttpServletRequest request, String strErrorMessage )
    {
        setPageTitleProperty( PROPERTY_ERRROR_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ERROR_MESSAGE, strErrorMessage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ERROR, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    // DO
    public String doCreateUnit( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        String strIdParent = request.getParameter( PARAMETER_ID_PARENT );
        UrlItem url = new UrlItem( JSP_MANAGE_UNITS );
        url.addParameter( PARAMETER_ID_UNIT, strIdParent );
        
        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return url.getUrl();
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

        _unitService.createUnit( unit );

        return url.getUrl();
    }
}

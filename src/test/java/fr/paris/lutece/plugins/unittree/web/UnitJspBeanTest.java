/*
 * Copyright (c) 2002-2025, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * UnitJspBean Test: the actions that change the tree require the security token
 */
public class UnitJspBeanTest extends LuteceTestCase
{
    private static final String JSP_URL_DO_REMOVE_UNIT = "jsp/admin/plugins/unittree/DoRemoveUnit.jsp";
    private static final String JSP_URL_DO_MODIFY_UNIT = "jsp/admin/plugins/unittree/DoModifyUnit.jsp";
    private static final String JSP_URL_DO_MOVE_SUB_TREE = "jsp/admin/plugins/unittree/DoMoveSubTree.jsp";
    private static final String PARAMETER_ID_UNIT = "idUnit";

    private @Inject UnitJspBean _bean;
    private @Inject ISecurityTokenService _securityTokenService;
    private int _nIdUnit;

    @BeforeEach
    protected void createUnit( )
    {
        Unit unit = new Unit( );
        unit.setIdParent( Unit.ID_ROOT );
        unit.setCode( "junit_csrf" );
        unit.setLabel( "junit_csrf" );
        unit.setDescription( "junit_csrf" );
        _nIdUnit = UnitHome.create( unit );
    }

    @AfterEach
    protected void removeUnit( )
    {
        UnitHome.remove( _nIdUnit );
    }

    private MockHttpServletRequest getRemoveUnitRequest( String strMethod )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( strMethod );
        request.setParameter( PARAMETER_ID_UNIT, Integer.toString( _nIdUnit ) );

        return request;
    }

    @Test
    public void testDoRemoveUnitNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doRemoveUnit( request ) );
        assertNotNull( UnitHome.findByPrimaryKey( _nIdUnit ) );
    }

    @Test
    public void testDoRemoveUnitInvalidToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_REMOVE_UNIT ) + "b" );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doRemoveUnit( request ) );
        assertNotNull( UnitHome.findByPrimaryKey( _nIdUnit ) );
    }

    @Test
    public void testDoRemoveUnitGet( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "GET" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_REMOVE_UNIT ) );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doRemoveUnit( request ) );
        assertNotNull( UnitHome.findByPrimaryKey( _nIdUnit ) );
    }

    @Test
    public void testDoRemoveUserNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idUser", "1" );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doRemoveUser( request ) );
    }

    @Test
    public void testDoRemoveUserTokenOfAnotherAction( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idUser", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_REMOVE_UNIT ) );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doRemoveUser( request ) );
    }

    @Test
    public void testDoModifyUnitNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "code", "junit_csrf_modified" );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doModifyUnit( request ) );
    }

    @Test
    public void testDoCreateUnitNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idParent", Integer.toString( Unit.ID_ROOT ) );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doCreateUnit( request ) );
    }

    @Test
    public void testDoMoveSubTreeNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idUnitParent", Integer.toString( Unit.ID_ROOT ) );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doMoveSubTree( request ) );
    }

    @Test
    public void testDoAddUsersNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idUsers", "1" );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doAddUsers( request ) );
    }

    @Test
    public void testDoMoveUserNoToken( )
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( "idUser", "1" );
        request.setParameter( "idSelectedUnit", Integer.toString( Unit.ID_ROOT ) );

        assertThrows( AccessDeniedException.class, ( ) -> _bean.doMoveUser( request ) );
    }

    @Test
    public void testDoModifyUnknownUnitReturnsToTheUnitTree( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( PARAMETER_ID_UNIT, "999999" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_MODIFY_UNIT ) );

        String strUrl = _bean.doModifyUnit( request );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( "jsp/admin/plugins/unittree/ManageUnits.jsp" ), strBackUrl );
    }

    @Test
    public void testDoMoveSubTreeIntoItselfReturnsToTheMoveScreen( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), UnitJspBean.RIGHT_MANAGE_UNITS );
        _bean.init( request, UnitJspBean.RIGHT_MANAGE_UNITS );
        request.setParameter( "idUnitParent", Integer.toString( _nIdUnit ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_MOVE_SUB_TREE ) );

        String strUrl = _bean.doMoveSubTree( request );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( "jsp/admin/plugins/unittree/MoveSubTree.jsp?idUnit=" + _nIdUnit ), strBackUrl );
    }

    @Test
    public void testDoMoveSubTreeWithoutParentReturnsToTheMoveScreen( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getRemoveUnitRequest( "POST" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, JSP_URL_DO_MOVE_SUB_TREE ) );

        String strUrl = _bean.doMoveSubTree( request );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( "jsp/admin/plugins/unittree/MoveSubTree.jsp?idUnit=" + _nIdUnit ), strBackUrl );
    }
}

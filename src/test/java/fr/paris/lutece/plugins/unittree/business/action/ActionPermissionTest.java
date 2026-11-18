/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.unittree.business.action;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.unittree.service.unit.UnitResourceIdService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The actions of the units are displayed according to their permission: it must be a permission of the resource type of the units
 */
public class ActionPermissionTest extends LuteceTestCase
{
    private static final List<String> LIST_PERMISSIONS = List.of( UnitResourceIdService.PERMISSION_CREATE, UnitResourceIdService.PERMISSION_MODIFY,
            UnitResourceIdService.PERMISSION_DELETE, UnitResourceIdService.PERMISSION_ADD_USER, UnitResourceIdService.PERMISSION_MOVE_USER,
            UnitResourceIdService.PERMISSION_REMOVE_USER, UnitResourceIdService.PERMISSION_MOVE_UNIT, UnitResourceIdService.PERMISSION_SEE_UNIT );

    @Test
    public void testActionPermissionsAreRegistered( )
    {
        List<IAction> listActions = new ArrayList<>( ActionHome.getActionsList( UnitAction.ACTION_TYPE ) );
        listActions.addAll( ActionHome.getActionsList( UnitUserAction.ACTION_TYPE ) );

        assertFalse( listActions.isEmpty( ) );

        for ( IAction action : listActions )
        {
            assertTrue( LIST_PERMISSIONS.contains( action.getPermission( ) ), "Unregistered permission " + action.getPermission( ) + " of " + action.getUrl( ) );
        }
    }
}

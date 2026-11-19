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
package fr.paris.lutece.plugins.unittree.business.unit;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.unittree.service.unit.AdminUserUnitRemovalListener;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The deletion of a back office user is refused while the user belongs to units
 */
public class AdminUserUnitRemovalListenerTest extends LuteceTestCase
{
    private static final int ID_USER = 654321;

    @Test
    public void testUserOfAUnitRefusesTheDeletion( )
    {
        AdminUserUnitRemovalListener listener = new AdminUserUnitRemovalListener( );
        String strIdUser = Integer.toString( ID_USER );
        assertTrue( listener.canBeRemoved( strIdUser ) );

        Unit unit = new Unit( );
        unit.setIdParent( Unit.ID_ROOT );
        unit.setCode( "removal_listener" );
        unit.setDescription( "removal listener" );
        unit.setLabel( "Removal listener unit" );
        int nIdUnit = UnitHome.create( unit );
        UnitHome.addUserToUnit( nIdUnit, ID_USER );
        try
        {
            assertFalse( listener.canBeRemoved( strIdUser ) );
            String strMessage = listener.getRemovalRefusedMessage( strIdUser, Locale.ENGLISH );
            assertTrue( strMessage.contains( "Removal listener unit" ), strMessage );

            UnitHome.removeUserFromUnit( ID_USER, nIdUnit );
            assertTrue( listener.canBeRemoved( strIdUser ) );
            assertTrue( listener.canBeRemoved( "abc" ) );
        }
        finally
        {
            UnitHome.removeUsersFromUnit( nIdUnit );
            UnitHome.remove( nIdUnit );
        }
    }
}

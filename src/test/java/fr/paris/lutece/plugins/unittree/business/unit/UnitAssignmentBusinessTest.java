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
package fr.paris.lutece.plugins.unittree.business.unit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.test.LuteceTestCase;

public class UnitAssignmentBusinessTest extends LuteceTestCase
{
	@Test
    public void testCRUD( )
    {
        UnitAssignment assignment = new UnitAssignment( );
        assignment.setIdAssignedUnit( 1 );
        assignment.setIdResource( 2 );
        assignment.setResourceType( "type" );
        assignment.setActive( true );
        assignment.setAssignmentType( UnitAssignmentType.CREATION );
        assignment.setAssignmentDate( Timestamp.valueOf( LocalDateTime.now( ) ) );

        UnitAssignmentHome.create( assignment );

        List<UnitAssignment> list = UnitAssignmentHome.findByResource( 2, "type" );
        assertEquals( 1, list.size( ) );
        assertEquals( assignment.getIdAssignedUnit( ), list.get( 0 ).getIdAssignedUnit( ) );
        assertEquals( assignment.getAssignmentType( ), list.get( 0 ).getAssignmentType( ) );
        assertEquals( assignment.getAssignmentDate( ).getTime(), list.get( 0 ).getAssignmentDate( ).getTime() );
        assertTrue( list.get( 0 ).isActive( ) );

        UnitAssignmentHome.deactivate( assignment );
        list = UnitAssignmentHome.findByResource( 2, "type" );
        assertEquals( 1, list.size( ) );
        assertFalse( list.get( 0 ).isActive( ) );

        UnitAssignmentHome.deleteByResource( 2, "type" );
        list = UnitAssignmentHome.findByResource( 2, "type" );
        assertEquals( 0, list.size( ) );
    }
}

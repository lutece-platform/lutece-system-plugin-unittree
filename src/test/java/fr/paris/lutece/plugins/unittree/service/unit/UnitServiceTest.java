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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.unittree.business.unit.MockUnit;
import fr.paris.lutece.plugins.unittree.business.unit.TreeUnit;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

public class UnitServiceTest extends LuteceTestCase
{
    private @Inject UnitService _unitService;

    @Test
    public void testAllSubUnitsOfNotExistingUnit( )
    {
        Unit unit = MockUnit.create( );
        unit.setIdUnit( 9999 );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unit, false );

        assertEquals( 0, listSubUnits.size( ) );
    }

    @Test
    public void testAllSubUnitsOfUnitWithoutSubUnit( )
    {
        Unit unit = insertUnitInDatabase( );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unit, false );

        assertEquals( 0, listSubUnits.size( ) );
        UnitHome.remove( unit.getIdUnit( ) );
    }

    private Unit insertUnitInDatabase( )
    {
        Unit unit = MockUnit.create( );
        UnitHome.create( unit );
        return unit;
    }

    @Test
    public void testAllSubUnitsOfUnitWithOneSubUnit( )
    {
        Unit unitParent = insertUnitInDatabase( );
        Unit unitChild = insertUnitInDatabase( );
        setParentUnit( unitChild, unitParent );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unitParent, false );

        assertThatSubUnitListsAreEqual( listSubUnits, unitChild );
    }

    private void setParentUnit( Unit unitChild, Unit unitParent )
    {
        unitChild.setIdParent( unitParent.getIdUnit( ) );
        UnitHome.updateParent( unitChild.getIdUnit( ), unitParent.getIdUnit( ) );
    }

    private void assertThatSubUnitListsAreEqual( List<Unit> listUnits1, Unit... listUnits2 )
    {
        assertEquals( listUnits2.length, listUnits1.size( ) );

        List<Integer> listUnitIds = new ArrayList<>( );

        for ( Unit unit : listUnits2 )
        {
            listUnitIds.add( unit.getIdUnit( ) );
        }

        for ( Unit unit : listUnits1 )
        {
            assertTrue( listUnitIds.contains( unit.getIdUnit( ) ) );
        }
    }

    @Test
    public void testAllSubUnitsOfUnitWithTwoDirectSubUnits( )
    {
        Unit unitParent = insertUnitInDatabase( );
        Unit unitChild1 = insertUnitInDatabase( );
        Unit unitChild2 = insertUnitInDatabase( );
        setParentUnit( unitChild1, unitParent );
        setParentUnit( unitChild2, unitParent );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unitParent, false );

        assertThatSubUnitListsAreEqual( listSubUnits, unitChild1, unitChild2 );
    }

    @Test
    public void testAllSubUnitsOfUnitWithSubSubUnit( )
    {
        Unit unitParent = insertUnitInDatabase( );
        Unit unitChild1 = insertUnitInDatabase( );
        Unit unitChild2 = insertUnitInDatabase( );
        setParentUnit( unitChild1, unitParent );
        setParentUnit( unitChild2, unitChild1 );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unitParent, false );

        assertThatSubUnitListsAreEqual( listSubUnits, unitChild1, unitChild2 );
    }

    @Test
    public void testAllSubUnitsWithComplexUnitTree( )
    {
        Unit unitParent = insertUnitInDatabase( );
        Unit unitChild1 = insertUnitInDatabase( );
        Unit unitChild1_1 = insertUnitInDatabase( );
        Unit unitChild2 = insertUnitInDatabase( );
        Unit unitChild2_1 = insertUnitInDatabase( );
        Unit unitChild2_1_1 = insertUnitInDatabase( );
        Unit unitChild2_2 = insertUnitInDatabase( );
        setParentUnit( unitChild1, unitParent );
        setParentUnit( unitChild1_1, unitChild1 );
        setParentUnit( unitChild2, unitParent );
        setParentUnit( unitChild2_1, unitChild2 );
        setParentUnit( unitChild2_1_1, unitChild2_1 );
        setParentUnit( unitChild2_2, unitChild2 );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unitParent, false );

        assertThatSubUnitListsAreEqual( listSubUnits, unitChild1, unitChild1_1, unitChild2, unitChild2_1, unitChild2_1_1, unitChild2_2 );
    }

    @Test
    public void testAllSubUnitsOfUnitInTheMiddleOfComplexUnitTree( )
    {
        Unit unitParent = insertUnitInDatabase( );
        Unit unitChild1 = insertUnitInDatabase( );
        Unit unitChild1_1 = insertUnitInDatabase( );
        Unit unitChild2 = insertUnitInDatabase( );
        Unit unitChild2_1 = insertUnitInDatabase( );
        Unit unitChild2_1_1 = insertUnitInDatabase( );
        Unit unitChild2_2 = insertUnitInDatabase( );
        setParentUnit( unitChild1, unitParent );
        setParentUnit( unitChild1_1, unitChild1 );
        setParentUnit( unitChild2, unitParent );
        setParentUnit( unitChild2_1, unitChild2 );
        setParentUnit( unitChild2_1_1, unitChild2_1 );
        setParentUnit( unitChild2_2, unitChild2 );

        List<Unit> listSubUnits = _unitService.getAllSubUnits( unitChild2, false );

        assertThatSubUnitListsAreEqual( listSubUnits, unitChild2_1, unitChild2_1_1, unitChild2_2 );
    }

    /**
     * A permission given on a unit is valid for its sub units: the tree of a user holding a role on a unit shows the unit, its sub units and the path to it
     */
    @Test
    public void testTreeOfUserWithPermissionOnUnits( )
    {
        Unit unitA = insertUnitInDatabase( );
        Unit unitA1 = insertUnitInDatabase( );
        Unit unitA1a = insertUnitInDatabase( );
        Unit unitB = insertUnitInDatabase( );
        Unit unitB1 = insertUnitInDatabase( );
        Unit unitB1a = insertUnitInDatabase( );
        Unit unitB2 = insertUnitInDatabase( );
        setParentUnit( unitA1, unitA );
        setParentUnit( unitA1a, unitA1 );
        setParentUnit( unitB1, unitB );
        setParentUnit( unitB1a, unitB1 );
        setParentUnit( unitB2, unitB );

        String strRoleKey = "unittree_tree_test_" + System.currentTimeMillis( );
        RBAC rbacA = createRBAC( strRoleKey, unitA, RBAC.WILDCARD_PERMISSIONS_KEY );
        RBAC rbacB1 = createRBAC( strRoleKey, unitB1, UnitResourceIdService.PERMISSION_SEE_UNIT );

        AdminUser user = new AdminUser( );
        user.setUserLevel( 1 );
        Map<String, RBACRole> mapRoles = new HashMap<>( );
        mapRoles.put( strRoleKey, new RBACRole( strRoleKey, strRoleKey ) );
        user.setRoles( mapRoles );

        try
        {
            TreeUnit treeUnit = new TreeUnit( _unitService.getRootUnit( false ) );
            _unitService.populateTreeUnit( treeUnit, user, false );

            Set<Integer> setIdUnitsOfTree = new HashSet<>( );
            collectIdUnits( treeUnit, setIdUnitsOfTree );

            assertTrue( setIdUnitsOfTree.contains( unitA.getIdUnit( ) ) );
            assertTrue( setIdUnitsOfTree.contains( unitA1.getIdUnit( ) ) );
            assertTrue( setIdUnitsOfTree.contains( unitA1a.getIdUnit( ) ) );
            assertTrue( setIdUnitsOfTree.contains( unitB.getIdUnit( ) ) );
            assertTrue( setIdUnitsOfTree.contains( unitB1.getIdUnit( ) ) );
            assertTrue( setIdUnitsOfTree.contains( unitB1a.getIdUnit( ) ) );
            assertFalse( setIdUnitsOfTree.contains( unitB2.getIdUnit( ) ) );
        }
        finally
        {
            RBACHome.remove( rbacA.getRBACId( ) );
            RBACHome.remove( rbacB1.getRBACId( ) );
        }
    }

    private RBAC createRBAC( String strRoleKey, Unit unit, String strPermission )
    {
        RBAC rbac = new RBAC( );
        rbac.setRoleKey( strRoleKey );
        rbac.setResourceTypeKey( Unit.RESOURCE_TYPE );
        rbac.setResourceId( String.valueOf( unit.getIdUnit( ) ) );
        rbac.setPermissionKey( strPermission );

        return RBACHome.create( rbac );
    }

    private void collectIdUnits( TreeUnit treeUnit, Set<Integer> setIdUnits )
    {
        setIdUnits.add( treeUnit.getUnitNode( ).getIdUnit( ) );

        for ( TreeUnit subTreeUnit : treeUnit.getSubUnits( ) )
        {
            collectIdUnits( subTreeUnit, setIdUnits );
        }
    }
}

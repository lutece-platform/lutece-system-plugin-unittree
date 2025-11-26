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
package fr.paris.lutece.plugins.unittree.business.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

public class UnitHomeTest extends LuteceTestCase
{ 
    /**
     * The structure of the tree used for tests is as follows :
     * 
     *                            0
     *                          (ROOT)
     *          __________________|____________________
     *         |                  |                    |
     *         1                  2                    3
     *    _____|_____        _____|_____         ______|______ 
     *   |     |     |      |     |     |       |             |
     *   4     5     6      7     8     9      10             11
     *   |
     *  12 
     */
	@Test
    public void testGetAllSubUnitsId( )
    {   
		deleteAllUnitsExceptRootUnit( );
		
    	//Level 0
		//Root unit is the only unit existing in database at this this stage
		int idUnit0 = Unit.ID_ROOT;
    	
    	//Level 1
    	int idUnit1 = createUnit( Unit.ID_ROOT, "unit 1", "unit 1", "unit 1" );
    	int idUnit2 = createUnit( Unit.ID_ROOT, "unit 2", "unit 2", "unit 2" );
    	int idUnit3 = createUnit( Unit.ID_ROOT, "unit 3", "unit 3", "unit 3" );
    	
    	//Level 2
    	int idUnit4 = createUnit( idUnit1, "unit 4", "unit 4", "unit 4" );
    	int idUnit5 = createUnit( idUnit1, "unit 5", "unit 5", "unit 5" );
    	int idUnit6 = createUnit( idUnit1, "unit 6", "unit 6", "unit 6" );
    	
    	int idUnit7 = createUnit( idUnit2, "unit 7", "unit 7", "unit 7" );
    	int idUnit8 = createUnit( idUnit2, "unit 8", "unit 8", "unit 8" );
    	int idUnit9 = createUnit( idUnit2, "unit 9", "unit 9", "unit 9" );
    	
    	int idUnit10 = createUnit( idUnit3, "unit 10", "unit 10", "unit 10" );
    	int idUnit11 = createUnit( idUnit3, "unit 11", "unit 11", "unit 11" );
    	
    	//Level 3
    	int idUnit12 = createUnit( idUnit4, "unit 12", "unit 12", "unit 12" );
		
		//Testing that non existent unit have no sub unit
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 9999 ) );
 	
    	//Testing retrieval of all sub unit ids of unit 0
		assertThatSubUnitListsAreEqual( Arrays.asList( idUnit1, idUnit2, idUnit3, idUnit4, idUnit5, idUnit6, idUnit7, idUnit8, idUnit9, idUnit10, idUnit11, idUnit12 ), getAllSubUnitsId( idUnit0 ) );
 	    
    	//Testing retrieval of all sub unit ids of unit 1
		assertThatSubUnitListsAreEqual( Arrays.asList( idUnit4, idUnit5, idUnit6, idUnit12 ), getAllSubUnitsId( idUnit1 ) );
	    	
    	//Testing retrieval of all sub unit ids of unit 2
		assertThatSubUnitListsAreEqual( Arrays.asList( idUnit7, idUnit8, idUnit9), getAllSubUnitsId( idUnit2 ) );
 
    	//Testing retrieval of all sub unit ids of unit 3
		assertThatSubUnitListsAreEqual( Arrays.asList( idUnit10, idUnit11), getAllSubUnitsId( idUnit3 ) );
	    	
    	//Testing retrieval of all sub unit ids of unit 4
		assertThatSubUnitListsAreEqual( Arrays.asList( idUnit12 ),  getAllSubUnitsId( idUnit4 ) );
   	
    	//Testing retrieval of all sub unit ids of unit 5
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 5 ) );
 	    	
    	//Testing retrieval of all sub unit ids of unit 6
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 6 ) );
	    	
    	//Testing retrieval of all sub unit ids of unit 7
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 7 ) );
    	
    	//Testing retrieval of all sub unit ids of unit 8
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 8 ) );
	    	
    	//Testing retrieval of all sub unit ids of unit 9
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 9 ) );
    	
    	//Testing retrieval of all sub unit ids of unit 10
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 10 ) );
   	
    	//Testing retrieval of all sub unit ids of unit 11
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 11 ) );
    	
    	//Testing retrieval of all sub unit ids of unit 12
		assertThatSubUnitListsAreEqual( Arrays.asList( ), getAllSubUnitsId( 11 ) );
    }
	
	/**
	 * Delete all units from previous tests except root unit 
	 * 
	 */
	protected void deleteAllUnitsExceptRootUnit( )
    {
		for( Unit unit : UnitHome.findAll( ) )
    	{
    		int idUnit = unit.getIdUnit();
    		if( idUnit != Unit.ID_ROOT )
    		{
    			UnitHome.remove( idUnit );
    		}
    	}
    }
    
    /*
     * Creates a unit
     * 
     * @param nIdParent
     *            the unit parent
     * @param strCode
     *            the unit code
     * @param strDescription
     *            the unit description
     * @param strLabel
     *            the unit label 
     */
    private int createUnit( int nIdParent, String strCode, String strDescription, String strLabel )
    {
    	Unit unit = new Unit( );
    	unit.setIdParent( nIdParent );
    	unit.setCode( strCode );
    	unit.setDescription( strDescription );
    	unit.setLabel( strLabel );   	
    	return UnitHome.create( unit );
    }
	
    /**
     * Test that the content of actual sub unit ids list contains is exactly the same as the expected sub unit ids list
     * 
     * @param subUnitIdsExpectedList expected sub unit ids list
     * @param subUnitIdsFoundList actual sub unit ids list
     */
	private void assertThatSubUnitListsAreEqual( List<Integer> subUnitIdsExpectedList, List<Integer> subUnitIdsActualList )
    {
        assertEquals( subUnitIdsExpectedList.size( ), subUnitIdsActualList.size( ) );

        for ( Integer idUnit : subUnitIdsExpectedList )
        {
            assertTrue( subUnitIdsActualList.contains( idUnit ) );
        }
    }
	
	/**
	 * returns all sub units of the unit with the id passed in parameter
	 * 
	 * @param idUnit
	 *          the id unit
	 * @return list of sub units ids
	 */
	private List<Integer> getAllSubUnitsId( int idUnit )
	{
		return UnitHome.getAllSubUnitsId( idUnit )
                       .stream( )
                       .collect( Collectors.toCollection( ArrayList::new ) );
	}
}
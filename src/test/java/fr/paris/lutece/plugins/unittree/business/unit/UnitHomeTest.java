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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

public class UnitHomeTest extends LuteceTestCase
{ 
	@BeforeEach
    protected void setUp( ) throws Exception
    {
    	createTestUnitTree( );
    }
	
	@AfterEach
    protected void tearDown( ) throws Exception
    {
		//Removing all units created by the previous test (all units except ROOT unit in fact)
    	for(Unit unit : UnitHome.findAll( ) )
    	{
    		int idUnit = unit.getIdUnit();
    		if( idUnit != 0)
    		{
    			UnitHome.remove( idUnit );
    		}
    	}
    }
	
	/**
     * Create the unit tree used in this test class. The structure of the tree is as follows :
     * 
     *                           ROOT
     *          __________________|____________________
     *         |                  |                    |
     *         1                  2                    3
     *    _____|_____        _____|_____         ______|______ 
     *   |     |     |      |     |     |       |             |
     *   4     5     6      7     8     9       10           11
     *   |
     *  12 
     */
    private void createTestUnitTree( )
    {
    	//Units creation
    	//Level 0
    	//ROOT unit is already created by SQL scripts executed before this test class
    	
    	//Level 1
    	createUnit( 1, 0, "1", "unit 1", "unit 1" );
    	createUnit( 2, 0, "2", "unit 2", "unit 2" );
    	createUnit( 3, 0, "3", "unit 3", "unit 3" );
    	
    	//Level 2
    	createUnit( 4, 1, "4", "unit 4", "unit 4" );
    	createUnit( 5, 1, "5", "unit 5", "unit 5" );
    	createUnit( 6, 1, "6", "unit 6", "unit 6" );
    	createUnit( 7, 2, "7", "unit 7", "unit 7" );
    	createUnit( 8, 2, "8", "unit 8", "unit 8" );
    	createUnit( 9, 2, "9", "unit 9", "unit 9" );
    	createUnit( 10, 3, "10", "unit 10", "unit 10" );
    	createUnit( 11, 3, "11", "unit 11", "unit 11" );
    	
    	//Level 3
    	createUnit( 12, 4, "12", "unit 12", "unit 12" );
    }
    
    /*
     * Creates a unit
     * 
     * @param nIdUnit
     *            the unit id
     * @param nIdParent
     *            the unit pareny
     * @param strCode
     *            the unit code
     * @param strDescription
     *            the unit description
     * @param strLabel
     *            the unit label 
     */
    private void createUnit( int nIdUnit, int nIdParent, String strCode, String strDescription, String strLabel )
    {
    	Unit unit = new Unit( );
    	unit.setIdUnit( nIdUnit );
    	unit.setIdParent( nIdParent );
    	unit.setCode( strCode );
    	unit.setDescription( strDescription );
    	unit.setLabel( strLabel );   	
    	UnitHome.create( unit );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfNonExistentUnit( )
    {   	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 9999 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnitROOT( )
    {    	
    	assertEquals( Arrays.asList( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ), getAllSubUnitsId( 0 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit1( )
    {  	    	
    	assertEquals( Arrays.asList( 4, 5, 6, 12 ), getAllSubUnitsId( 1 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnit2( )
    {  	    	
    	assertEquals( Arrays.asList( 7, 8, 9), getAllSubUnitsId( 2 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnit3( )
    {  	 	
    	assertEquals( Arrays.asList( 10, 11), getAllSubUnitsId( 3 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnit4( )
    {  	    	
    	assertEquals( Arrays.asList( 12 ), getAllSubUnitsId( 4 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnit5( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 5 ) );
    }
    
	@Test
	public void testGetAllSubUnitsIdOfUnit6( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 6 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit7( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 7 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit8( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 8 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit9( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 9 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit10( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 10 ) );
    }
    
	@Test
    public void testGetAllSubUnitsIdOfUnit11( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 11 ) );
    }
	
	@Test
    public void testGetAllSubUnitsIdOfUnit12( )
    {  	    	
    	assertEquals( Arrays.asList( ), getAllSubUnitsId( 12 ) );
    }
	
	/**
	 * returns and sort all sub units of the unit of the unit id passed in parameter
	 * 
	 * @param idUnit
	 *          the id unit
	 * @return list of sub units ids
	 */
	private List<Integer> getAllSubUnitsId( int idUnit )
	{
		List<Integer> subUnitsIdsList= UnitHome.getAllSubUnitsId( idUnit )
                                               .stream( )
                                               .collect( Collectors.toCollection( ArrayList::new ) );
        Collections.sort( subUnitsIdsList, Comparator.comparing( Integer::intValue ) );
        return subUnitsIdsList;
	}
}
package fr.paris.lutece.plugins.unittree.business.unit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.test.LuteceTestCase;

public class UnitAssignmentBusinessTest extends LuteceTestCase
{
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
        assertEquals( assignment.getAssignmentDate( ), list.get( 0 ).getAssignmentDate( ) );
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

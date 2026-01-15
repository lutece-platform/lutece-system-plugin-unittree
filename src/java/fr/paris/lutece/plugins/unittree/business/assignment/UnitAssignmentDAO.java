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
package fr.paris.lutece.plugins.unittree.business.assignment;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for UnitAssignment objects
 */
@ApplicationScoped
public class UnitAssignmentDAO implements IUnitAssignmentDAO
{

    public static final String BEAN_NAME = "unittree.unitAssignmentDAO";

    private static final String SQL_QUERY_SELECTALL = "SELECT id, unittree_unit_assignment.id_resource, unittree_unit_assignment.resource_type, id_assignor_unit, id_assigned_unit, assignment_date, assignment_type, is_active,"
            + " u_assignor.id_parent as id_parent_assignor_unit, u_assignor.label as label_assignor_unit, u_assignor.code as code_assignor_unit, u_assignor.description as description_assignor_unit,"
            + " u_assigned.id_parent as id_parent_assigned_unit, u_assigned.label as label_assigned_unit, u_assigned.code as code_assigned_unit, u_assigned.description as description_assigned_unit"
            + " FROM unittree_unit_assignment  LEFT JOIN  unittree_unit u_assignor on u_assignor.id_unit = unittree_unit_assignment.id_assignor_unit left JOIN unittree_unit u_assigned on u_assigned.id_unit = unittree_unit_assignment.id_assigned_unit  ";
    private static final String SQL_QUERY_SELECT_CURRENT = SQL_QUERY_SELECTALL
            + " WHERE id_resource = ? AND resource_type = ? AND is_active = 1 ORDER BY assignment_date DESC, id DESC";
    private static final String SQL_QUERY_SELECT_BY_RESOURCE = SQL_QUERY_SELECTALL
            + " WHERE id_resource = ? AND resource_type = ? ORDER BY assignment_date ASC, id ASC";
    private static final String SQL_QUERY_SELECT_BY_UNIT = SQL_QUERY_SELECTALL
            + " WHERE id_assigned_unit = ? ORDER BY resource_type ASC, id_resource ASC, assignment_date ASC, id ASC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO unittree_unit_assignment ( id, id_resource, resource_type, id_assigned_unit, id_assignor_unit, assignment_type, is_active ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DESACTIVATE = "UPDATE unittree_unit_assignment SET is_active = 0 WHERE id = ? ";
    private static final String SQL_QUERY_DESACTIVATE_BY_RESOURCE = " UPDATE unittree_unit_assignment SET is_active = 0 WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = " DELETE FROM unittree_unit_assignment WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT_IDRESOURCE = "SELECT id_resource FROM unittree_unit_assignment WHERE 1=1 ";

    private static final String SQL_QUERY_SELECT_BY_FILTER = SQL_QUERY_SELECTALL + " WHERE 1=1 ";

    private static final String CONSTANT_AND_ACTIVE = " AND is_active = ?";
    private static final String CONSTANT_AND_RESOURCETYPE = " AND resource_type = ?";
    private static final String CONSTANT_AND_ASSIGNEDUNIT = " AND id_assigned_unit IN (?";
    private static final String CONSTANT_AND_RESOURCEID = " AND id_resource IN (?";
    private static final String SQL_CLOSE_PARENTHESIS = " ) ";
    private static final String SQL_ADITIONAL_PARAMETER = ",?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( UnitAssignment unitAssignment, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, unitAssignment.getId( ) );
            daoUtil.setInt( ++nIndex, unitAssignment.getIdResource( ) );
            daoUtil.setString( ++nIndex, unitAssignment.getResourceType( ) );
            daoUtil.setInt( ++nIndex, unitAssignment.getAssignedUnit( ).getIdUnit( ) );
            daoUtil.setInt( ++nIndex, unitAssignment.getAssignorUnit( ).getIdUnit( ) );
            daoUtil.setString( ++nIndex, unitAssignment.getAssignmentType( ).getAssignmentTypeCode( ) );
            daoUtil.setBoolean( ++nIndex, unitAssignment.isActive( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                unitAssignment.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public UnitAssignment loadCurrentAssignment( int nIdResource, String strResourceType, Plugin plugin )
    {
        UnitAssignment unitAssignment = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CURRENT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                unitAssignment = dataToUnitAssignment( daoUtil );
            }
        }
        return unitAssignment;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deactivate( UnitAssignment unitAssignment, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DESACTIVATE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, unitAssignment.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UnitAssignment> selectByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        List<UnitAssignment> listUnitAssignments = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listUnitAssignments.add( dataToUnitAssignment( daoUtil ) );
            }
        }
        return listUnitAssignments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UnitAssignment> selectByUnit( int nIdUnit, Plugin plugin )
    {
        List<UnitAssignment> listUnitAssignments = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_UNIT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdUnit );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listUnitAssignments.add( dataToUnitAssignment( daoUtil ) );
            }
        }
        return listUnitAssignments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectIdResourceByFilter(UnitAssignmentFilter filter , Plugin plugin )
    {
        List<Integer> listResourceId = new ArrayList<>( );

        String filterSQL = filterBuildSql( filter );

        if( filterSQL != null )
        {
            String sSQL = SQL_QUERY_SELECT_IDRESOURCE +
                    filterSQL;

            try ( DAOUtil daoUtil = new DAOUtil(sSQL, plugin ) )
            {
                if ( filterInsertData( filter , daoUtil ) == -1 )
                {
                    return listResourceId;
                }

                daoUtil.executeQuery( );

                while ( daoUtil.next( ) )
                {
                    listResourceId.add( daoUtil.getInt( "id_resource" ) );
                }
            }
        }

        return listResourceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UnitAssignment> getListByFilter(UnitAssignmentFilter filter , Plugin plugin )
    {
        List<UnitAssignment> listUnitAssignments = new ArrayList<>( );

        String filterSQL = filterBuildSql( filter );

        if( filterSQL != null )
        {
            String sSQL = SQL_QUERY_SELECT_BY_FILTER +
                    filterSQL;

            try ( DAOUtil daoUtil = new DAOUtil(sSQL, plugin ) )
            {
                if ( filterInsertData( filter , daoUtil ) == -1 )
                {
                    return listUnitAssignments;
                }

                daoUtil.executeQuery( );

                while ( daoUtil.next( ) )
                {
                    listUnitAssignments.add( dataToUnitAssignment( daoUtil ) );
                }
            }
        }

        return listUnitAssignments;
    }

    /**
     * Create condition sql for where filter
     *
     * @param filter the filter
     * @return sql filter, null if the request can t give data
     */
    private String filterBuildSql(UnitAssignmentFilter filter )
    {

        if( (filter.getIdAssignedUnit() != null && filter.getIdAssignedUnit().isEmpty()) ||
                (filter.getIdResource() != null && filter.getIdResource().isEmpty()) )
        {
            return null;
        }

        StringBuilder sbSQL = new StringBuilder();

        if ( filter.getActive() != -1 )
        {
            sbSQL.append( CONSTANT_AND_ACTIVE );
        }

        if ( filter.getResourceType() != null )
        {
            sbSQL.append( CONSTANT_AND_RESOURCETYPE );
        }

        if ( filter.getIdAssignedUnit() != null )
        {
            sbSQL.append( CONSTANT_AND_ASSIGNEDUNIT );
            for ( int i = 1; i < filter.getIdAssignedUnit().size(); i++ )
            {
                sbSQL.append( SQL_ADITIONAL_PARAMETER );
            }
            sbSQL.append(SQL_CLOSE_PARENTHESIS);
        }

        if ( filter.getIdResource() != null )
        {
            sbSQL.append( CONSTANT_AND_RESOURCEID );
            for ( int i = 1; i < filter.getIdResource().size(); i++ )
            {
                sbSQL.append( SQL_ADITIONAL_PARAMETER );
            }
            sbSQL.append(SQL_CLOSE_PARENTHESIS);
        }

        return sbSQL.toString();
    }

    /**
     * insert filter data in DAOUtil, on the same order of filterBuildSql
     *
     * @param filter the filter
     * @param daoUtil the daoUtil
     * @return index of inserted data -1 if the request can t give data
     */
    private int filterInsertData(UnitAssignmentFilter filter , DAOUtil daoUtil )
    {
        if( (filter.getIdAssignedUnit() != null && filter.getIdAssignedUnit().isEmpty()) ||
                (filter.getIdResource() != null && filter.getIdResource().isEmpty()) )
        {
            return -1;
        }

        int nIndex = 0;

        if ( filter.getActive() != -1 )
        {
            daoUtil.setInt( ++nIndex , filter.getActive() );
        }

        if ( filter.getResourceType() != null )
        {
            daoUtil.setString( ++nIndex , filter.getResourceType() );
        }

        if ( filter.getIdAssignedUnit() != null )
        {
            for ( Integer integer : filter.getIdAssignedUnit() ) {
                daoUtil.setInt(++nIndex, integer);
            }
        }

        if ( filter.getIdResource() != null )
        {
            for ( Integer integer : filter.getIdResource() ) {
                daoUtil.setInt(++nIndex, integer);
            }
        }

        return nIndex;
    }


    /**
     * Creates a {@code UnitAssignment} object from the data of the specified {@code DAOUtil}
     * 
     * @param daoUtil
     *            the {@code DAOUtil} containing the data
     * @return a new {@code UnitAssignment} object
     */
    private UnitAssignment dataToUnitAssignment( DAOUtil daoUtil )
    {
        UnitAssignment unitAssignment = new UnitAssignment( );
        unitAssignment.setId( daoUtil.getInt( "id" ) );
        unitAssignment.setIdResource( daoUtil.getInt( "id_resource" ) );
        unitAssignment.setResourceType( daoUtil.getString( "resource_type" ) );
        unitAssignment.setAssignmentDate( daoUtil.getTimestamp( "assignment_date" ) );
        unitAssignment.setAssignmentType( UnitAssignmentType.findByCode( daoUtil.getString( "assignment_type" ) ) );
        unitAssignment.setActive( daoUtil.getBoolean( "is_active" ) );

        Unit unitAssignor = unitAssignment.getAssignorUnit( );
        unitAssignor.setIdUnit( daoUtil.getInt( "id_assignor_unit" ) );
        unitAssignor.setIdParent( daoUtil.getInt( "id_parent_assignor_unit" ) );
        unitAssignor.setLabel( daoUtil.getString( "label_assignor_unit" ) );
        unitAssignor.setDescription( daoUtil.getString( "description_assignor_unit" ) );
        unitAssignor.setCode( daoUtil.getString( "code_assignor_unit" ) );

        Unit unitAssigned = unitAssignment.getAssignedUnit( );
        unitAssigned.setIdUnit( daoUtil.getInt( "id_assigned_unit" ) );
        unitAssigned.setIdParent( daoUtil.getInt( "id_parent_assigned_unit" ) );
        unitAssigned.setLabel( daoUtil.getString( "label_assigned_unit" ) );
        unitAssigned.setDescription( daoUtil.getString( "description_assigned_unit" ) );
        unitAssigned.setCode( daoUtil.getString( "code_assigned_unit" ) );

        return unitAssignment;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deactivateByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DESACTIVATE_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );

            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void deleteByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );

            daoUtil.executeUpdate( );
        }

    }
}

/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * UnitDAO
 * 
 */
public class UnitDAO implements IUnitDAO
{
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_OR = " OR ";
    private static final String SQL_ORDER_BY_LABEL_ASC = " ORDER BY label ASC ";
    private static final String SQL_FILTER_ID_PARENT = " id_parent = ? ";
    private static final String SQL_FILTER_LABEL = " label = ? ";
    private static final String SQL_FILTER_DESCRIPTION = " description = ? ";

    // Table unittree_unit
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_unit ) FROM unittree_unit ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO unittree_unit ( id_unit, id_parent, label, description ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT = " SELECT id_unit, id_parent, label, description FROM unittree_unit WHERE id_unit = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_unit, id_parent, label, description FROM unittree_unit ";
    private static final String SQL_QUERY_SELECT_BY_ID_USER = " SELECT u.id_unit, u.id_parent, u.label, u.description "
            + " FROM unittree_unit u INNER JOIN unittree_unit_user uu ON u.id_unit = uu.id_unit"
            + " WHERE uu.id_user = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM unittree_unit WHERE id_unit = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE unittree_unit SET label = ?, description = ? WHERE id_unit = ? ";
    private static final String SQL_QUERY_HAS_SUB_UNIT = " SELECT id_unit FROM unittree_unit WHERE id_parent = ? ";
    private static final String SQL_QUERY_SELECT_BY_SECTOR = " SELECT uu.id_unit, uu.id_parent, uu.label, uu.description "
            + " FROM unittree_unit_sector uus INNER JOIN unittree_unit uu ON uus.id_unit = uu.id_unit"
            + " INNER JOIN unittree_sector us ON us.id_sector = uus.id_sector WHERE us.id_sector = ? ";
    private static final String SQL_QUERY_SELECT_NO_CHILDREN = " SELECT id_unit, id_parent, label, description "
            + " FROM unittree_unit WHERE id_unit NOT IN(SELECT id_parent FROM unittree_unit) ";

    // Table unittree_unit_user
    private static final String SQL_QUERY_ADD_USER_TO_UNIT = " INSERT INTO unittree_unit_user ( id_unit, id_user ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECT_IDS_USER = " SELECT id_user FROM unittree_unit_user WHERE id_unit = ? ";
    private static final String SQL_QUERY_SELECT_ALL_IDS_USER = " SELECT id_user FROM unittree_unit_user ";
    private static final String SQL_QUERY_REMOVE_USER_FROM_UNIT = " DELETE FROM unittree_unit_user WHERE id_user = ? AND id_unit = ? ";
    private static final String SQL_QUERY_REMOVE_USERS_FROM_UNIT = " DELETE FROM unittree_unit_user WHERE id_unit = ? ";
    private static final String SQL_QUERY_CHECK_USER = " SELECT id_unit FROM unittree_unit_user WHERE id_user = ? AND id_unit = ? ";
    private static final String SQL_QUERY_UPDATE_UNIT_PARENT = " UPDATE unittree_unit SET id_parent = ? WHERE id_unit = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int insert( Unit unit, Plugin plugin )
    {
        int nIndex = 1;
        int nIdUnit = newPrimaryKey( plugin );
        unit.setIdUnit( nIdUnit );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( nIndex++, unit.getIdUnit( ) );
        daoUtil.setInt( nIndex++, unit.getIdParent( ) );
        daoUtil.setString( nIndex++, unit.getLabel( ) );
        daoUtil.setString( nIndex, unit.getDescription( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return nIdUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Unit load( int nIdUnit, Plugin plugin )
    {
        Unit unit = null;
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( nIndex, nIdUnit );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;

            unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex ) );
        }

        daoUtil.free( );

        return unit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> selectByIdUser( int nIdUser, Plugin plugin )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_USER, plugin );
        daoUtil.setInt( nIndex, nIdUser );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex ) );
            listUnits.add( unit );
        }

        daoUtil.free( );

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> selectAll( Plugin plugin )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL + SQL_ORDER_BY_LABEL_ASC, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex ) );

            listUnits.add( unit );
        }

        daoUtil.free( );

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSubUnits( int nIdUnit, Plugin plugin )
    {
        boolean bHasSubUnits = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HAS_SUB_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            bHasSubUnits = true;
        }

        daoUtil.free( );

        return bHasSubUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUserFromUnit( int nIdUser, int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_USER_FROM_UNIT, plugin );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setInt( 2, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUsersFromUnit( int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_USERS_FROM_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( Unit unit, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, unit.getLabel( ) );
        daoUtil.setString( nIndex++, unit.getDescription( ) );

        daoUtil.setInt( nIndex, unit.getIdUnit( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> selectByFilter( UnitFilter cmFilter, Plugin plugin )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        StringBuilder sbSQL = new StringBuilder( buildSQLQuery( cmFilter ) );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        setFilterValues( cmFilter, daoUtil );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex ) );

            listUnits.add( unit );
        }

        daoUtil.free( );

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectAllIdsUser( Plugin plugin )
    {
        List<Integer> listIdUsers = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_IDS_USER, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIdUsers.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listIdUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectIdsUser( int nIdUnit, Plugin plugin )
    {
        List<Integer> listIdUsers = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_IDS_USER, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIdUsers.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listIdUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addUserToUnit( int nIdUnit, int nIdUser, Plugin plugin )
    {
        int nIndex = 1;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_USER_TO_UNIT, plugin );
        daoUtil.setInt( nIndex++, nIdUnit );
        daoUtil.setInt( nIndex, nIdUser );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserInUnit( int nIdUser, int nIdUnit, Plugin plugin )
    {
        boolean bIsUserInAnUnit = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_USER, plugin );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setInt( 2, nIdUnit );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            bIsUserInAnUnit = true;
        }

        daoUtil.free( );

        return bIsUserInAnUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> findBySectorId( int nIdSector, Plugin plugin )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SECTOR + SQL_ORDER_BY_LABEL_ASC, plugin );
        daoUtil.setInt( 1, nIdSector );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            listUnits.add( unit );
        }

        daoUtil.free( );

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getUnitWithNoChildren( Plugin plugin )
    {
        List<Unit> listUnits = new ArrayList<Unit>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NO_CHILDREN + SQL_ORDER_BY_LABEL_ASC, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            listUnits.add( unit );
        }

        daoUtil.free( );

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateParent( int nIdUnitToMove, int nIdNewParent, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_UNIT_PARENT, plugin );
        daoUtil.setInt( 1, nIdNewParent );
        daoUtil.setInt( 2, nIdUnitToMove );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    // PRIVATE METHODS

    /**
     * Build the SQL query with filter
     * @param uFilter the filter
     * @return a SQL query
     */
    private String buildSQLQuery( UnitFilter uFilter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ALL );
        int nIndex = 1;

        if ( uFilter.containsIdParent( ) )
        {
            nIndex = addSQLWhereOr( uFilter.isWideSearch( ), sbSQL, nIndex );
            sbSQL.append( SQL_FILTER_ID_PARENT );
        }

        if ( uFilter.containsLabel( ) )
        {
            nIndex = addSQLWhereOr( uFilter.isWideSearch( ), sbSQL, nIndex );
            sbSQL.append( SQL_FILTER_LABEL );
        }

        if ( uFilter.containsDescription( ) )
        {
            nIndex = addSQLWhereOr( uFilter.isWideSearch( ), sbSQL, nIndex );
            sbSQL.append( SQL_FILTER_DESCRIPTION );
        }

        sbSQL.append( SQL_ORDER_BY_LABEL_ASC );

        return sbSQL.toString( );
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index. <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b>
     * depending of the wide search characteristic</li>
     * </ul>
     * @param bIsWideSearch true if it is a wide search, false otherwise
     * @param sbSQL the SQL query
     * @param nIndex the index
     * @return the new index
     */
    private int addSQLWhereOr( boolean bIsWideSearch, StringBuilder sbSQL, int nIndex )
    {
        if ( nIndex == 1 )
        {
            sbSQL.append( SQL_WHERE );
        }
        else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }

    /**
     * Set the filter values on the DAOUtil
     * @param uFilter the filter
     * @param daoUtil the DAOUtil
     */
    private void setFilterValues( UnitFilter uFilter, DAOUtil daoUtil )
    {
        int nIndex = 1;

        if ( uFilter.containsIdParent( ) )
        {
            daoUtil.setInt( nIndex++, uFilter.getIdParent( ) );
        }

        if ( uFilter.containsLabel( ) )
        {
            daoUtil.setString( nIndex++, uFilter.getLabel( ) );
        }

        if ( uFilter.containsDescription( ) )
        {
            daoUtil.setString( nIndex, uFilter.getDescription( ) );
        }
    }
}

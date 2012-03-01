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
package fr.paris.lutece.plugins.unittree.business.sector;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * SectorDAO
 *
 */
public class SectorDAO implements ISectorDAO
{
    private static final String SQL_QUERY_SELECT = " SELECT id_sector, name, number_sector FROM unittree_sector WHERE id_sector = ? ORDER BY name ASC ";
    private static final String SQL_QUERY_SELECT_BY_ID_UNIT = " SELECT s.id_sector, s.name, s.number_sector " +
        " FROM unittree_sector s INNER JOIN unittree_unit_sector u ON s.id_sector = u.id_sector " +
        " WHERE u.id_unit = ? ORDER BY name ASC ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_sector, name, number_sector FROM unittree_sector ORDER BY name ASC ";
    private static final String SQL_QUERY_ADD_SECTOR_TO_UNIT = " INSERT INTO unittree_unit_sector ( id_unit, id_sector ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_HAS_SECTOR = " SELECT id_unit, id_sector FROM unittree_unit_sector WHERE id_unit = ? AND id_sector = ? ";
    private static final String SQL_QUERY_HAS_SECTORS = " SELECT id_unit, id_sector FROM unittree_unit_sector WHERE id_unit = ? ";
    private static final String SQL_QUERY_REMOVE_SECTORS_FROM_UNIT = " DELETE FROM unittree_unit_sector WHERE id_unit = ? ";
    private static final String SQL_QUERY_REMOVE = " DELETE FROM unittree_unit_sector WHERE id_unit = ? AND id_sector = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public Sector load( int nIdSector, Plugin plugin )
    {
        Sector sector = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdSector );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;

            sector = new Sector(  );
            sector.setIdSector( daoUtil.getInt( nIndex++ ) );
            sector.setName( daoUtil.getString( nIndex++ ) );
            sector.setNumberSector( daoUtil.getString( nIndex ) );
        }

        daoUtil.free(  );

        return sector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sector> loadAll( Plugin plugin )
    {
        List<Sector> listSectors = new ArrayList<Sector>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;

            Sector sector = new Sector(  );
            sector.setIdSector( daoUtil.getInt( nIndex++ ) );
            sector.setName( daoUtil.getString( nIndex++ ) );
            sector.setNumberSector( daoUtil.getString( nIndex ) );

            listSectors.add( sector );
        }

        daoUtil.free(  );

        return listSectors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sector> loadByIdUnit( int nIdUnit, Plugin plugin )
    {
        List<Sector> listSectors = new ArrayList<Sector>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;

            Sector sector = new Sector(  );
            sector.setIdSector( daoUtil.getInt( nIndex++ ) );
            sector.setName( daoUtil.getString( nIndex++ ) );
            sector.setNumberSector( daoUtil.getString( nIndex ) );
            listSectors.add( sector );
        }

        daoUtil.free(  );

        return listSectors;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public synchronized void addSectorToUnit( int nIdUnit, int nIdSector, Plugin plugin )
    {
        int nIndex = 1;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_SECTOR_TO_UNIT, plugin );
        daoUtil.setInt( nIndex++, nIdUnit );
        daoUtil.setInt( nIndex, nIdSector );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSector( int nIdUnit, int nIdSector, Plugin plugin )
    {
        boolean bHasSector = false;
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HAS_SECTOR, plugin );
        daoUtil.setInt( nIndex++, nIdUnit );
        daoUtil.setInt( nIndex, nIdSector );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasSector = true;
        }

        daoUtil.free(  );

        return bHasSector;
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public boolean hasSectors( int nIdUnit, Plugin plugin )
    {
        boolean bHasSector = false;
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HAS_SECTORS, plugin );
        daoUtil.setInt( nIndex, nIdUnit );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasSector = true;
        }

        daoUtil.free(  );

        return bHasSector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSectorsFromUnit( int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_SECTORS_FROM_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSector( int nIdUnit, int nIdSector, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE, plugin );
        daoUtil.setInt( nIndex++, nIdUnit );
        daoUtil.setInt( nIndex, nIdSector );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}

/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.unittree.service.sector;

import fr.paris.lutece.plugins.unittree.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.business.sector.SectorHome;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * SectorService
 *
 */
public class SectorService implements ISectorService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Sector findByPrimaryKey( int nIdSector )
    {
        return SectorHome.findByPrimaryKey( nIdSector );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sector> findAll(  )
    {
        return SectorHome.findAll(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sector> findByIdUnit( int nIdUnit )
    {
        return SectorHome.findByIdUnit( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdsSectorByIdUnit( int nIdUnit )
    {
        List<Integer> listIdsSector = new ArrayList<Integer>(  );

        for ( Sector sector : findByIdUnit( nIdUnit ) )
        {
            if ( sector != null )
            {
                listIdsSector.add( sector.getIdSector(  ) );
            }
        }

        return listIdsSector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void addSectorsToUnit( Unit unit )
    {
        if ( ( unit != null ) && ( unit.getListIdsSector(  ) != null ) && !unit.getListIdsSector(  ).isEmpty(  ) )
        {
            for ( int nIdSector : unit.getListIdsSector(  ) )
            {
                SectorHome.addSectorToUnit( unit.getIdUnit(  ), nIdSector );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSector( int nIdUnit, int nIdSector )
    {
        return SectorHome.hasSector( nIdUnit, nIdSector );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void removeSectorsFromUnit( int nIdUnit )
    {
        SectorHome.removeSectorsFromUnit( nIdUnit );
    }
}

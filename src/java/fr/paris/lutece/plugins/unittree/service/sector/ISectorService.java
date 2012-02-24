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
import fr.paris.lutece.plugins.unittree.business.unit.Unit;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * ISectorService
 *
 */
public interface ISectorService
{
    /**
     * Find a sector from its primary key
     * @param nIdSector the id sector
     * @return an instance of {@link Sector}
     */
    Sector findByPrimaryKey( int nIdSector );

    /**
     * find a list of sectors from a given id unit
     * @param nIdUnit the id unit
     * @return a list of {@link Sector}
     */
    List<Sector> findByIdUnit( int nIdUnit );

    /**
     * Find all sectors
     * @return a list of {@link Sector}
     */
    List<Sector> findAll(  );

    /**
     * Get the ids sector from a given id unit
     * @param nIdUnit the id unit
     * @return a list of ids sector
     */
    List<Integer> getIdsSectorByIdUnit( int nIdUnit );

    /**
     * Check if the unit has the sector
     * @param nIdUnit the id unit
     * @param nIdSector the id sector
     * @return true if the unit has the sector, false otherwise
     */
    boolean hasSector( int nIdUnit, int nIdSector );

    /**
     * Add sectors to the unit
     * @param unit the unit
     */
    @Transactional( "unittree.transactionManager" )
    void addSectorsToUnit( Unit unit );

    /**
     * Remove the sectors from an unit
     * @param nIdUnit the id unit
     */
    @Transactional( "unittree.transactionManager" )
    void removeSectorsFromUnit( int nIdUnit );
}

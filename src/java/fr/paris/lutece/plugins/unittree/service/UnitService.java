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
package fr.paris.lutece.plugins.unittree.service;

import fr.paris.lutece.plugins.unittree.business.Unit;
import fr.paris.lutece.plugins.unittree.business.UnitFilter;
import fr.paris.lutece.plugins.unittree.business.UnitHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.xml.XmlUtil;

import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


/**
 *
 *
 */
public class UnitService implements IUnitService
{
    // XML TAGS
    private static final String TAG_UNITS = "units";
    private static final String TAG_UNIT = "unit";
    private static final String TAG_ID_UNIT = "id-unit";
    private static final String TAG_LABEL = "label";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_UNIT_CHILDREN = "unit-children";

    // XSL
    private static final String PATH_XSL = "/WEB-INF/plugins/unittree/xsl/";
    private static final String FILE_TREE_XSL = "units_tree.xsl";

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public Unit getUnit( int nIdUnit )
    {
        return UnitHome.findByPrimaryKey( nIdUnit );
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public Unit getRootUnit(  )
    {
        return getUnit( Unit.ID_ROOT );
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public List<Unit> getAllUnits(  )
    {
        return UnitHome.findAll(  );
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public List<Unit> getUnitsFirstLevel(  )
    {
        return getSubUnits( Unit.ID_ROOT );
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public List<Unit> getSubUnits( int nIdUnit )
    {
        UnitFilter uFilter = new UnitFilter(  );
        uFilter.setIdParent( nIdUnit );

        return UnitHome.findByFilter( uFilter );
    }

    public String getXMLUnits(  )
    {
        StringBuffer sbXML = new StringBuffer(  );
        XmlUtil.beginElement( sbXML, TAG_UNITS );

        getXMLUnit( sbXML, getRootUnit(  ) );

        XmlUtil.endElement( sbXML, TAG_UNITS );

        return sbXML.toString(  );
    }

    public void getXMLUnit( StringBuffer sbXML, Unit unit )
    {
        XmlUtil.beginElement( sbXML, TAG_UNIT );
        XmlUtil.addElement( sbXML, TAG_ID_UNIT, unit.getIdUnit(  ) );
        XmlUtil.addElement( sbXML, TAG_LABEL, unit.getLabel(  ) );
        XmlUtil.addElement( sbXML, TAG_DESCRIPTION, unit.getDescription(  ) );

        List<Unit> listChildren = getSubUnits( unit.getIdUnit(  ) );

        if ( ( listChildren != null ) && !listChildren.isEmpty(  ) )
        {
            XmlUtil.beginElement( sbXML, TAG_UNIT_CHILDREN );

            for ( Unit child : listChildren )
            {
                getXMLUnit( sbXML, child );
            }

            XmlUtil.endElement( sbXML, TAG_UNIT_CHILDREN );
        }

        XmlUtil.endElement( sbXML, TAG_UNIT );
    }

    /**
    * Gets the XSL to display user spaces tree
    * @return The XSL to display user spaces tree
    */
    public Source getTreeXsl(  )
    {
        FileInputStream fis = AppPathService.getResourceAsStream( PATH_XSL, FILE_TREE_XSL );

        return new StreamSource( fis );
    }

    // CRUD OPERATIONS

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public int createUnit( Unit unit )
    {
        if ( unit != null )
        {
            return UnitHome.create( unit );
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void removeUnit( int nIdUnit )
    {
        UnitHome.remove( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void updateUnit( Unit unit )
    {
        if ( unit != null )
        {
            UnitHome.update( unit );
        }
    }
}

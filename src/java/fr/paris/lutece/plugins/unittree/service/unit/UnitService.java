/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.unittree.business.action.IAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitFilter;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.action.IActionService;
import fr.paris.lutece.plugins.unittree.service.rbac.UnittreeRBACService;
import fr.paris.lutece.plugins.unittree.service.rbac.UnittreeRBACRecursiveType;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * UnitService
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
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";

    // PROPERTIES
    private static final String PROPERTY_LABEL_PARENT_UNIT = "unittree.moveUser.labelParentUnit";

    // CONSTANTS
    private static final String ATTRIBUTE_ID_UNIT = "idUnit";
    private static final String ATTRIBUTE_LABEL = "label";

    // XSL
    private static final String PATH_XSL = "/WEB-INF/plugins/unittree/xsl/";
    private static final String FILE_TREE_XSL = "units_tree.xsl";
    @Inject
    private IUnitUserService _unitUserService;
    @Inject
    private IActionService _actionService;

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public Unit getUnit( int nIdUnit, boolean bGetAdditionalInfos )
    {
        Unit unit = UnitHome.findByPrimaryKey( nIdUnit );

        if ( ( unit != null ) && bGetAdditionalInfos )
        {
            UnitAttributeManager.populate( unit );
        }

        return unit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Unit getRootUnit( boolean bGetAdditionalInfos )
    {
        return getUnit( Unit.ID_ROOT, bGetAdditionalInfos );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getUnitsByIdUser( int nIdUser, boolean bGetAdditionalInfos )
    {
        List<Unit> listUnit = UnitHome.findByIdUser( nIdUser );

        for ( Unit unit : listUnit )
        {
            if ( bGetAdditionalInfos && ( unit != null ) )
            {
                UnitAttributeManager.populate( unit );
            }
        }

        return listUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getAllUnits( boolean bGetAdditionalInfos )
    {
        List<Unit> listUnits = UnitHome.findAll( );

        if ( bGetAdditionalInfos )
        {
            for ( Unit unit : listUnits )
            {
                if ( unit != null )
                {
                    UnitAttributeManager.populate( unit );
                }
            }
        }

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getUnitsFirstLevel( boolean bGetAdditionalInfos )
    {
        return getSubUnits( Unit.ID_ROOT, bGetAdditionalInfos );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getSubUnits( int nIdUnit, boolean bGetAdditionalInfos )
    {
        // If the ID unit == -1, then only return the root unit
        if ( nIdUnit == Unit.ID_NULL )
        {
            List<Unit> listUnits = new ArrayList<Unit>( );
            listUnits.add( getRootUnit( bGetAdditionalInfos ) );

            return listUnits;
        }

        UnitFilter uFilter = new UnitFilter( );
        uFilter.setIdParent( nIdUnit );

        List<Unit> listUnits = UnitHome.findByFilter( uFilter );

        if ( bGetAdditionalInfos )
        {
            for ( Unit unit : listUnits )
            {
                if ( unit != null )
                {
                    UnitAttributeManager.populate( unit );
                }
            }
        }

        return listUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getAllSubUnits( Unit unit, boolean bGetAdditionalInfos )
    {
        List<Unit> listSubUnits = getSubUnits( unit.getIdUnit( ), bGetAdditionalInfos );

        return getAllSubUnitsRecursive( listSubUnits, bGetAdditionalInfos );
    }

    /**
     * Recursive method to get all the sub units from a list of units
     * 
     * @param listUnits
     *            the list of units the method has to search the sub units
     * @param bGetAdditionalInfos
     *            {@code true} if it must get the ids sector, {@code false} otherwise
     * @return all the sub units
     */
    private List<Unit> getAllSubUnitsRecursive( List<Unit> listUnits, boolean bGetAdditionalInfos )
    {
        List<Unit> listCurrentUnitsAndSubUnits = new ArrayList<>( listUnits );

        for ( Unit unit : listUnits )
        {
            List<Unit> listSubUnits = getSubUnits( unit.getIdUnit( ), bGetAdditionalInfos );
            listCurrentUnitsAndSubUnits.addAll( getAllSubUnitsRecursive( listSubUnits, bGetAdditionalInfos ) );
        }

        return listCurrentUnitsAndSubUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IAction> getListActions( String strActionType, Locale locale, Unit unit, AdminUser user, UnittreeRBACRecursiveType recursiveType )
    {
        List<IAction> listActions = _actionService.getListActions( strActionType, locale, unit, user, strActionType, recursiveType );

        if ( unit != null )
        {
            // If the unit has sectors and does not have sub units, then remove 'CREATE' action
            if ( !canCreateSubUnit( unit.getIdUnit( ) ) )
            {
                Integer nIndexToRemove = null;
                for ( int i = 0; i < listActions.size( ); i++ )
                {
                    if ( listActions.get( i ).getPermission( ).equals( UnitResourceIdService.PERMISSION_CREATE ) )
                    {
                        nIndexToRemove = i;
                    }
                }
                if ( nIndexToRemove != null )
                {
                    listActions.remove( nIndexToRemove.intValue( ) );
                }
            }
        }

        return listActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getSubUnitsAsReferenceList( int nIdUnit, Locale locale )
    {
        // If parent id == -1, then its sub units is the root unit
        if ( nIdUnit == Unit.ID_NULL )
        {
            ReferenceList listSubUnits = new ReferenceList( );
            listSubUnits.addItem( Unit.ID_ROOT, getRootUnit( false ).getLabel( ) );

            return listSubUnits;
        }

        // Otherwise, build the reference list
        Unit unit = getUnit( nIdUnit, false );

        if ( unit != null )
        {
            String strLabelParentUnit = I18nService.getLocalizedString( PROPERTY_LABEL_PARENT_UNIT, locale );
            ReferenceList listSubUnits = new ReferenceList( );
            listSubUnits.addItem( unit.getIdParent( ), strLabelParentUnit );
            listSubUnits.addAll( ReferenceList.convert( getSubUnits( nIdUnit, false ), ATTRIBUTE_ID_UNIT, ATTRIBUTE_LABEL, true ) );

            return listSubUnits;
        }

        return new ReferenceList( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXMLUnits( AdminUser user )
    {
        List<Unit> listAuthorizedUnit = UnittreeRBACService.getTreeableAuthorizedUnitCollection( UnitResourceIdService.PERMISSION_SEE_UNIT, user );

        StringBuffer sbXML = new StringBuffer( );
        XmlUtil.beginElement( sbXML, TAG_UNITS );

        Unit rootUnit = getRootUnit( false );

        if ( isUnitInList( rootUnit, listAuthorizedUnit ) )
        {
            getXMLUnit( sbXML, listAuthorizedUnit, rootUnit, user );
        }

        XmlUtil.endElement( sbXML, TAG_UNITS );

        return sbXML.toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Source getTreeXsl( )
    {
        FileInputStream fis = AppPathService.getResourceAsStream( PATH_XSL, FILE_TREE_XSL );

        return new StreamSource( fis );
    }

    /**
     * Find by sector id
     * 
     * @param nIdSector
     *            id sector
     * @return list of Unit found
     */
    public List<Unit> findBySectorId( int nIdSector )
    {
        return UnitHome.findBySectorId( nIdSector );
    }

    /**
     * Return all the Unit with no children (level 0)
     * 
     * @return all the Unit with no children (level 0)
     */
    public List<Unit> getUnitWithNoChildren( )
    {
        return UnitHome.getUnitWithNoChildren( );
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSubUnits( int nIdUnit )
    {
        return UnitHome.hasSubUnits( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isParent( Unit unitParent, Unit unitRef )
    {
        if ( ( unitParent != null ) && ( unitRef != null ) )
        {
            if ( unitParent.getIdUnit( ) == unitRef.getIdParent( ) )
            {
                return true;
            }

            Unit nextUnitParent = getUnit( unitRef.getIdParent( ), false );

            while ( ( nextUnitParent != null ) && ( nextUnitParent.getIdUnit( ) != Unit.ID_NULL ) )
            {
                if ( unitParent.getIdUnit( ) == nextUnitParent.getIdUnit( ) )
                {
                    return true;
                }

                nextUnitParent = getUnit( nextUnitParent.getIdParent( ), false );
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canCreateSubUnit( int nIdUnit )
    {
        return hasSubUnits( nIdUnit ) || UnitAttributeManager.canCreateSubUnit( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthorized( Unit unit, String strPermission, AdminUser user, UnittreeRBACRecursiveType recursiveType )
    {
        // 1 The user in an admin user
        if ( user.isAdmin( ) )
        {
            return true;
        }

        // 2 The user is authorized (optionnal : recursively over the unit and unit parents)
        return UnittreeRBACService.isAuthorized( unit, strPermission, user, recursiveType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthorized( String strIdUnit, String strPermission, AdminUser user, UnittreeRBACRecursiveType recursiveType )
    {
        if ( StringUtils.isNotBlank( strIdUnit ) && StringUtils.isNumeric( strIdUnit ) )
        {
            int nIdUnit = Integer.parseInt( strIdUnit );
            Unit unit = getUnit( nIdUnit, false );

            return isAuthorized( unit, strPermission, user, recursiveType );
        }

        return false;
    }

    // CRUD OPERATIONS

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public int createUnit( Unit unit, HttpServletRequest request ) throws UnitErrorException
    {
        int nIdUnit = Unit.ID_NULL;

        if ( unit != null )
        {
            nIdUnit = UnitHome.create( unit );
            UnitAttributeManager.doCreateUnit( unit, request );

            return nIdUnit;
        }

        return Unit.ID_NULL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void removeUnit( int nIdUnit, HttpServletRequest request )
    {
        if ( ( nIdUnit != Unit.ID_ROOT ) && !hasSubUnits( nIdUnit ) )
        {
            UnitAttributeManager.doRemoveUnit( nIdUnit, request );

            // Remove users
            _unitUserService.removeUsersFromUnit( nIdUnit );

            UnitHome.remove( nIdUnit );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public void updateUnit( Unit unit, HttpServletRequest request ) throws UnitErrorException
    {
        if ( unit != null )
        {
            UnitAttributeManager.doModifyUnit( unit, request );

            // Update unit information
            UnitHome.update( unit );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "unittree.transactionManager" )
    public boolean moveSubTree( Unit unitToMove, Unit newUnitParent )
    {
        if ( ( unitToMove != null ) && ( newUnitParent != null ) && ( unitToMove.getIdUnit( ) != newUnitParent.getIdUnit( ) )
                && !isParent( unitToMove, newUnitParent ) )
        {
            UnitAttributeManager.moveSubTree( unitToMove, newUnitParent );
            unitToMove.setIdParent( newUnitParent.getIdUnit( ) );
            UnitHome.updateParent( unitToMove.getIdUnit( ), newUnitParent.getIdUnit( ) );

            return true;
        }

        return false;
    }

    // PRIVATE METHODS

    /**
     * Get the XML for an unit
     * 
     * @param sbXML
     *            the XML
     * @param unit
     *            the unit
     */
    private void getXMLUnit( StringBuffer sbXML, List<Unit> listAuthorizedUnits, Unit unit, AdminUser user )
    {
        XmlUtil.beginElement( sbXML, TAG_UNIT );
        XmlUtil.addElement( sbXML, TAG_ID_UNIT, unit.getIdUnit( ) );
        XmlUtil.addElement( sbXML, TAG_LABEL, CDATA_START + unit.getLabel( ) + CDATA_END );
        XmlUtil.addElement( sbXML, TAG_DESCRIPTION, CDATA_START + unit.getDescription( ) + CDATA_END );

        List<Unit> listChildren = getSubUnits( unit.getIdUnit( ), false );

        if ( ( listChildren != null ) && !listChildren.isEmpty( ) )
        {
            XmlUtil.beginElement( sbXML, TAG_UNIT_CHILDREN );

            for ( Unit child : listChildren )
            {
                if ( isUnitInList( child, listAuthorizedUnits ) )
                {
                    getXMLUnit( sbXML, listAuthorizedUnits, child, user );
                }
            }

            XmlUtil.endElement( sbXML, TAG_UNIT_CHILDREN );
        }

        XmlUtil.endElement( sbXML, TAG_UNIT );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Unit> getListParentUnits( Unit givenUnit )
    {
        if ( givenUnit == null )
        {
            return null;
        }

        List<Unit> listAllUnits = UnitHome.findAll( );
        List<Unit> listParentUnits = new ArrayList<>( );

        // Build a map of id / unit :
        Map<Integer, Unit> _mapIdUnit = new HashMap<>( );
        for ( Unit unit : listAllUnits )
        {
            _mapIdUnit.put( unit.getIdUnit( ), unit );
        }

        Unit recursiveUnit = givenUnit;
        while ( recursiveUnit != null )
        {
            listParentUnits.add( recursiveUnit );
            if ( recursiveUnit.getIdParent( ) == -1 )
            {
                break;
            }
            recursiveUnit = _mapIdUnit.get( recursiveUnit.getIdParent( ) );

        }
        return listParentUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUnitInList( Unit unitToCheck, Collection<Unit> listUnits )
    {
        for ( Unit unit : listUnits )
        {
            if ( unit.getIdUnit( ) == unitToCheck.getIdUnit( ) )
            {
                return true;
            }
        }
        return false;
    }
}

/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.unittree.web.action;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * DefaultUnitSearchFields
 *
 */
public abstract class DefaultUnitSearchFields implements IUnitSearchFields, Serializable
{
    private static final long serialVersionUID = 7400229591290783994L;

    // PROPERTIES
    private static final String PROPERTY_ITEM_PER_PAGE = "unittree.itemsPerPage";

    // PARAMETERS
    private static final String PARAMETER_SESSION = "session";
    private static final String PARAMETER_IS_IN_DEPTH_SEARCH = "isInDepthSearch";

    // VARIABLES
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_ITEM_PER_PAGE, 50 );
    private String _strCurrentPageIndex;
    private boolean _bIsInDepthSearch;
    private String _strSortedAttributeName;
    private boolean _bIsAscSort;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPageIndex(  )
    {
        return _strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefaultItemsPerPage(  )
    {
        return _nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPageIndex( String strCurrentPageIndex )
    {
        _strCurrentPageIndex = strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultItemsPerPage( int nDefaultItemsPerPage )
    {
        _nDefaultItemsPerPage = nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsPerPage(  )
    {
        return _nItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemsPerPage( int nItemsPerPage )
    {
        _nItemsPerPage = nItemsPerPage;
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public void setInDepthSearch( HttpServletRequest request )
    {
        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SESSION ) ) )
        {
            _bIsInDepthSearch = StringUtils.isNotBlank( request.getParameter( PARAMETER_IS_IN_DEPTH_SEARCH ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInDepthSearch(  )
    {
        return _bIsInDepthSearch;
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public String getSortedAttributeName(  )
    {
        return _strSortedAttributeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSortedAttributeName( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME ) ) )
        {
            _strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAscSort(  )
    {
        return _bIsAscSort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAscSort( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( Parameters.SORTED_ASC ) ) )
        {
            _bIsAscSort = Boolean.parseBoolean( request.getParameter( Parameters.SORTED_ASC ) );
        }
    }
}

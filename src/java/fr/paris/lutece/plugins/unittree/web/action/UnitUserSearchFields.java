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

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserFilter;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * UnitUserSearchFields
 *
 */
public class UnitUserSearchFields extends DefaultUnitSearchFields
{
    private static final long serialVersionUID = -4692457181851661527L;

    // PARAMETERS
    private static final String PARAMETER_ID_UNIT = "idUnit";
    private static final String PARAMETER_SESSION = "session";

    // MARKS
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_LIST_USERS = "listUsers";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_SEARCH_ADMIN_USER_FILTER = "search_admin_user_filter";
    private static final String MARK_IS_IN_DEPTH_SEARCH = "isInDepthSearch";

    // MESSAGES
    private static final String MESSAGE_ERROR_USER_NOT_LOGGED = "unittree.message.error.userNotLogged";

    // SERVICES
    private AdminUser _user;
    private AdminUserFilter _auFilter = new AdminUserFilter(  );

    /**
     * Constructor
     */
    public UnitUserSearchFields(  )
    {
        super(  );
    }

    /**
     * Constructor
     * @param request the HTTP request
     */
    public UnitUserSearchFields( HttpServletRequest request )
    {
        super(  );
        _user = AdminUserService.getAdminUser( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModelForUserSearchForm( List<AdminUser> listUsers, String strBaseUrl, HttpServletRequest request,
        Map<String, Object> model, Unit unit ) throws AccessDeniedException
    {
        if ( _user == null )
        {
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ERROR_USER_NOT_LOGGED,
                    request.getLocale(  ) );
            throw new AccessDeniedException( strErrorMessage );
        }

        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SESSION ) ) )
        {
            _auFilter = new AdminUserFilter(  );
            _auFilter.setAdminUserFilter( request );
        }

        UrlItem url = new UrlItem( strBaseUrl );

        List<AdminUser> listFilteredUsers = new ArrayList<AdminUser>(  );

        for ( AdminUser filteredUser : AdminUserHome.findUserByFilter( _auFilter ) )
        {
            boolean bIsFiltered = false;

            for ( AdminUser user : listUsers )
            {
                if ( user.getUserId(  ) == filteredUser.getUserId(  ) )
                {
                    bIsFiltered = true;

                    break;
                }
            }

            if ( bIsFiltered && ( _user.isParent( filteredUser ) || ( _user.isAdmin(  ) ) ) )
            {
                listFilteredUsers.add( filteredUser );
            }
        }

        // SORT
        this.setSortedAttributeName( request );

        if ( getSortedAttributeName(  ) != null )
        {
            this.setAscSort( request );

            Collections.sort( listFilteredUsers,
                new AttributeComparator( getSortedAttributeName(  ), this.isAscSort(  ) ) );
        }

        this.setCurrentPageIndex( Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX,
                this.getCurrentPageIndex(  ) ) );
        this.setItemsPerPage( Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE,
                this.getItemsPerPage(  ), this.getDefaultItemsPerPage(  ) ) );

        if ( getSortedAttributeName(  ) != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, getSortedAttributeName(  ) );
            url.addParameter( Parameters.SORTED_ASC, Boolean.toString( this.isAscSort(  ) ) );
        }

        url.addParameter( PARAMETER_ID_UNIT, unit.getIdUnit(  ) );
        url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        LocalizedPaginator<AdminUser> paginator = new LocalizedPaginator<AdminUser>( listFilteredUsers,
                getItemsPerPage(  ), url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, getCurrentPageIndex(  ),
                request.getLocale(  ) );

        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<Level>(  );

        for ( Level level : LevelHome.getLevelsList(  ) )
        {
            if ( _user.isAdmin(  ) || _user.hasRights( level.getId(  ) ) )
            {
                filteredLevels.add( level );
            }
        }

        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_LIST_USERS, paginator.getPageItems(  ) );
        model.put( MARK_SEARCH_ADMIN_USER_FILTER, _auFilter );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage(  ) ) );
        model.put( MARK_IS_IN_DEPTH_SEARCH, this.isInDepthSearch(  ) );
    }
}

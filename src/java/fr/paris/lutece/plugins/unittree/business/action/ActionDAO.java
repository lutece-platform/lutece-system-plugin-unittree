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
package fr.paris.lutece.plugins.unittree.business.action;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 *
 * ActionDAO
 *
 */
public class ActionDAO implements IActionDAO
{
    private static final String SQL_QUERY_SELECT_ACTIONS = "SELECT id_action, name_key, description_key, action_url, icon_url, action_permission, action_type " +
        " FROM unittree_action WHERE action_type = ? ";
    private static final String SQL_QUERY_SELECT_FILTER_BY_PERMISSION = "SELECT id_action, name_key, description_key, action_url, icon_url, action_permission, action_type " +
        " FROM unittree_action WHERE action_type = ? AND action_permission != ? ";
    @Inject
    private ActionFactory _actionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IAction> selectActions( String strActionType, Plugin plugin )
    {
        List<IAction> listActions = new ArrayList<IAction>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTIONS, plugin );
        daoUtil.setString( 1, strActionType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            IAction action = _actionFactory.newAction( strActionType );
            action.setIdAction( daoUtil.getInt( nIndex++ ) );
            action.setNameKey( daoUtil.getString( nIndex++ ) );
            action.setDescriptionKey( daoUtil.getString( nIndex++ ) );
            action.setUrl( daoUtil.getString( nIndex++ ) );
            action.setIcon( daoUtil.getString( nIndex++ ) );
            action.setPermission( daoUtil.getString( nIndex++ ) );
            action.setActionType( daoUtil.getString( nIndex ) );

            listActions.add( action );
        }

        daoUtil.free(  );

        return listActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IAction> selectFilterByPermission( String strActionType, String strPermission, Plugin plugin )
    {
        int nIndex = 1;
        List<IAction> listActions = new ArrayList<IAction>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FILTER_BY_PERMISSION, plugin );
        daoUtil.setString( nIndex++, strActionType );
        daoUtil.setString( nIndex, strPermission );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIndex = 1;

            IAction action = _actionFactory.newAction( strActionType );
            action.setIdAction( daoUtil.getInt( nIndex++ ) );
            action.setNameKey( daoUtil.getString( nIndex++ ) );
            action.setDescriptionKey( daoUtil.getString( nIndex++ ) );
            action.setUrl( daoUtil.getString( nIndex++ ) );
            action.setIcon( daoUtil.getString( nIndex++ ) );
            action.setPermission( daoUtil.getString( nIndex++ ) );
            action.setActionType( daoUtil.getString( nIndex ) );

            listActions.add( action );
        }

        daoUtil.free(  );

        return listActions;
    }
}

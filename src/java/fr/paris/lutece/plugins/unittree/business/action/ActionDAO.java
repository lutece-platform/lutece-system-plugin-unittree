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
package fr.paris.lutece.plugins.unittree.business.action;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * ActionDAO
 *
 */
public class ActionDAO implements IActionDAO
{
    private static final String SQL_QUERY_SELECT_ACTIONS = "SELECT id_action, name_key, description_key, action_url, icon_url, action_permission, resource_type " +
        " FROM unittree_action WHERE resource_type = ? ";

    /**
     * Load the list of actions for a document
     * @return The Collection of actions
     */
    public List<IAction> selectActions( String strResourceType, Plugin plugin )
    {
        List<IAction> listActions = new ArrayList<IAction>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTIONS, plugin );
        daoUtil.setString( 1, strResourceType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            IAction action = new Action(  );
            action.setIdAction( daoUtil.getInt( nIndex++ ) );
            action.setNameKey( daoUtil.getString( nIndex++ ) );
            action.setDescriptionKey( daoUtil.getString( nIndex++ ) );
            action.setUrl( daoUtil.getString( nIndex++ ) );
            action.setIconUrl( daoUtil.getString( nIndex++ ) );
            action.setPermission( daoUtil.getString( nIndex++ ) );
            action.setResourceType( daoUtil.getString( nIndex++ ) );

            listActions.add( action );
        }

        daoUtil.free(  );

        return listActions;
    }
}

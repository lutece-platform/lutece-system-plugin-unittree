/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 *
 * This factory is used for :
 * <ul>
 * <li>creating new instance of {@link IAction} depending of the action type</li>
 * </ul>
 *
 */
public class ActionFactory implements IActionFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public IAction newAction( String strActionType )
    {
        IAction action = null;

        try
        {
            action = SpringContextService.getBean( strActionType );
        }
        catch( BeansException e )
        {
            logBeanException( e );
        }

        // If no action is defined for strActionType, then create a DefaultAction
        if ( action == null )
        {
            action = new DefaultAction( );
        }

        return action;
    }

    private void logBeanException( BeansException e )
    {
        String beanName = null;
        String message = e.getMessage( );

        if ( e instanceof BeanDefinitionStoreException )
        {
            beanName = ( (BeanDefinitionStoreException) e ).getBeanName( );
        }
        else
            if ( e instanceof NoSuchBeanDefinitionException )
            {
                beanName = ( (NoSuchBeanDefinitionException) e ).getBeanName( );
            }
            else
                if ( e instanceof CannotLoadBeanClassException )
                {
                    beanName = ( (CannotLoadBeanClassException) e ).getBeanName( );
                }

        if ( beanName != null )
        {
            AppLogService.debug( "ActionFactory ERROR : could not load bean '" + beanName + "' - CAUSE : " + message );
        }
    }
}

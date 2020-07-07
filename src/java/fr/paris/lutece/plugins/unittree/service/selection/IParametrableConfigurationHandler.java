package fr.paris.lutece.plugins.unittree.service.selection;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

public interface IParametrableConfigurationHandler
{
    /**
     * Gives the HTML representing the configuration form to display in the workflow task
     * 
     * @param locale
     *            the locale
     * @param task
     *            the task associated to the configuration
     * @return the HTML in a String
     */
    String getCustomConfigForm( Locale locale, ITask task );

    /**
     * Saves the configuration
     * 
     * @param request
     *            the request containing the configuration
     * @param task
     *            the task associated to the configuration
     * @return the error or {@code null} if there is no error
     */
    String saveConfiguration( HttpServletRequest request, ITask task );

    /**
     * Removes the configuration
     * 
     * @param task
     *            the task associated to the configuration
     */
    void removeConfiguration( ITask task );
    
    /**
     * Get the {@link IParametrableUnitSelection}.
     * @return
     */
    IParametrableUnitSelection getParametrableUnitSelection( );
    
    String getBeanName( );
    
    String getTitle( Locale locale );
}

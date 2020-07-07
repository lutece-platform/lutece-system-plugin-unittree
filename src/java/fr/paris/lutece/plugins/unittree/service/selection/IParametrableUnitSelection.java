package fr.paris.lutece.plugins.unittree.service.selection;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

/**
 * Parametrable config for unit selection
 */
public interface IParametrableUnitSelection
{
    /**
     * <p>
     * Selects the unit the resource will be assigned to.
     * </p>
     * <p>
     * This method is not responsible of storing the assignment. The store is done by the workflow task itself.
     * </p>
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request containing information to select the unit. WARNING : if the unit selection is automatic, the request is {@code null}.
     * @param task
     *            the task associated to the unit selection
     * @return the id of the target unit
     * @throws AssignmentNotPossibleException
     *             if the assignment is not possible
     */
    int select( int nIdResource, String strResourceType, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException;
}

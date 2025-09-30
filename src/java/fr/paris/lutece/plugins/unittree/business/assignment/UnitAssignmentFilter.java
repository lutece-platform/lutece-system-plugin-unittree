/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.plugins.unittree.business.assignment;

import java.util.List;

/**
 * Filter UnitAssignment used to load filter from the plugin XML file
 */
public class UnitAssignmentFilter
{
    private String _strResourceType;
    private List<Integer> _listIdAssignedUnit;
    private List<Integer> _listIdResource;
    private int _nActive;

    /**
     * Constructor
     */
    public UnitAssignmentFilter( )
    {
        _strResourceType = null;
        _listIdAssignedUnit = null;
        _listIdResource = null;
        _nActive = -1;
    }

    /**
     * Get the active
     *
     * @return the active
     */
    public int getActive() {
        return _nActive;
    }

    /**
     * Set the active
     *
     * @param nActive
     *            the active
     */
    public void setActive(int nActive) {
        this._nActive = nActive;
    }

    /**
     * Get the idAssignedUnit
     *
     * @return the idAssignedUnit
     */
    public List<Integer> getIdAssignedUnit() {
        return _listIdAssignedUnit;
    }

    /**
     * Set the idAssignedUnit
     *
     * @param listIdAssignedUnit
     *            the idAssignedUnit
     */
    public void setIdAssignedUnit(List<Integer> listIdAssignedUnit) {
        this._listIdAssignedUnit = listIdAssignedUnit;
    }

    /**
     * Get the idResource
     *
     * @return the idResource
     */
    public List<Integer> getIdResource() {
        return _listIdResource;
    }

    /**
     * Set the idResource
     *
     * @param listIdResource
     *            the idResource
     */
    public void setIdResource(List<Integer> listIdResource) {
        this._listIdResource = listIdResource;
    }

    /**
     * Get the resourceType
     *
     * @return the resourceType
     */
    public String getResourceType() {
        return _strResourceType;
    }

    /**
     * Set the resourceType
     *
     * @param strResourceType
     *            the resourceType
     */
    public void setResourceType(String strResourceType) {
        this._strResourceType = strResourceType;
    }
}

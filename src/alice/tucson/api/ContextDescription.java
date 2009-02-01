/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.api;

import alice.tucson.service.LanguageInfo;

import java.util.Properties;

/**
 * This class represents an Agent Coordination Context
 * Description
 *
 *
 */
@SuppressWarnings("serial")
public class ContextDescription implements java.io.Serializable {

    private java.util.Properties properties;
    private String theory;
    private LanguageInfo lan;

    /**
     * Describes a context with a Prolog theory
     * @param theory is the source of the prolog theory
     */
    public ContextDescription(String theory){
        this.theory=theory;
    }

    /**
     * Describes a context with a set of properties
     * @param p is the set of properties
     */
    public ContextDescription(java.util.Properties p){
        properties = p;
    }

    /**
     * Creates an empty context description
     */
    public ContextDescription(){
        properties = new Properties();
    }

    /**
     * Gets the value of a property
     * @param name is the name of the property
     * @return the value of the property
     */
    public String getProperty(String name){
        return properties.getProperty(name);
    }

    /**
     * Sets the value of a property
     * @param name is the name of the property
     * @param value is the value to be set
     */
    public void setProperty(String name,String value){
        properties.setProperty(name,value);
    }
    
    public void setLanguageInfo(LanguageInfo lan){
    	this.lan = lan;
    }
    
    public LanguageInfo getLanguageInfo(){
    	return this.lan;
    }
    
    
    
}
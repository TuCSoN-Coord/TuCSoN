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

import alice.tucson.api.*;

import java.util.Properties;

/**
 * This class represents a default Agent Coordination Context
 * Description
 *
 *
 */
public class DefaultContextDescription extends ContextDescription {

    private Properties properties;

    /**
     * Creates an empty context description
     */
    public DefaultContextDescription(){
        properties = new Properties();
        properties.setProperty("context-name","default");
        properties.setProperty("agent-identity","anonymous");
    }

    /**
     * Creates a default context description
     *
     * @param aid is the identity of the agent
     */
    public DefaultContextDescription(AgentId aid){
        properties = new Properties();
        properties.setProperty("context-name","default");
        properties.setProperty("agent-identity",aid.toString());
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

}
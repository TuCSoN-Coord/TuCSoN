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
import alice.logictuple.*;
import alice.tucson.api.contexts.DefaultContextStub;

import java.io.*;

/**
 *  Tucson Meta Agent Coordiation Context, which can be used
 *  to get an agent coordination context to access TuCSoN services
 */
public class Tucson {

    private static ContextDescription defaultContext = new DefaultContextDescription();

    protected Tucson(){}

    /**
     * Gets the default agent coordination context
     * @return the agent coordination context
     * @throws s ContextNotAvailableException if the default context cannot be released
     */
    public static TucsonContext enterDefaultContext()
            throws ContextNotAvailableException {
        try {
            return new DefaultContextStub(defaultContext);
        } catch (Exception ex){
            throw new ContextNotAvailableException();
        }
    }

    /**
     * Gets an agent coordination context
     * @param context is the context description in which the agent aims to enter
     * @return the agent coordination context
     * @throws s ContextNotAvailableException  if the context cannot be released
     * @throws s InvalidContextDescriptionException if the context description is not valid
     */
    public static TucsonContext enterContext(ContextDescription context)
            throws ContextNotAvailableException, InvalidContextDescriptionException {

        String name=context.getProperty("context-name");
        if (name==null){
            throw new ContextNotAvailableException();
        }
        if (name.equals("default")){
            return new DefaultContextStub(context);
        } else {
            throw new ContextNotAvailableException();
        }
    }

    /**
     *  Gets the TuCSoN version number
     * @return the version number
     */
    public static String getVersion(){
        return "1.3.0";
    }
}
/*
 * Copyright 1999-2019 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of MoK <http://mok.apice.unibo.it>.
 *
 *    MoK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    MoK is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with MoK.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package uniform.swarms.utils;

import java.util.Properties;

/**
 * @author Stefano Mariani (mailto: s [dot] mariani [at] unibo [dot] it)
 *
 */
public final class Topology {

    /**
     * 
     */
    public static void bootTopology() {
        Properties props = new Properties();
        props.put("-netid", "localhost");
        props.put("-portno", "20504");
        props.put("-tcname", "anthill");
        Boot.boot(props);

        props = new Properties();
        props.put("-netid", "localhost");
        props.put("-portno", "20505");
        props.put("-tcname", "short");
        Boot.boot(props);

        props = new Properties();
        props.put("-netid", "localhost");
        props.put("-portno", "20506");
        props.put("-tcname", "long1");
        Boot.boot(props);

        props = new Properties();
        props.put("-netid", "localhost");
        props.put("-portno", "20507");
        props.put("-tcname", "long2");
        Boot.boot(props);

        props = new Properties();
        props.put("-netid", "localhost");
        props.put("-portno", "20508");
        props.put("-tcname", "food");
        Boot.boot(props);
    }

}

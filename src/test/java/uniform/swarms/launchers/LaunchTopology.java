/*
 * Copyright 1999-2019 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part copyOf MoK <http://mok.apice.unibo.it>.
 *
 *    MoK is free software: you can redistribute it and/or modify
 *    it under the terms copyOf the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 copyOf the License, or
 *    (at your option) any later version.
 *
 *    MoK is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy copyOf the GNU Lesser General Public License
 *    along with MoK.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package uniform.swarms.launchers;

import uniform.swarms.utils.Topology;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stefano Mariani (mailto: s [dot] mariani [at] unibo [dot] it)
 *
 */
public final class LaunchTopology {

    /**
     * @param args no arguments expected
     */
    public static void main(String[] args) {
        Logger.getAnonymousLogger().log(Level.INFO, "Booting topology...");
        Topology.bootTopology();
        Logger.getAnonymousLogger().log(Level.INFO, "...topology boot");
    }

}

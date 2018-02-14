/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.api;

/**
 * Enhanced Asynchronous ACC. Can act both on the ordinary tuple space and on
 * the specification tuple space. Bulk and uniform primitives are included.
 *
 * @see alice.tucson.api.AsynchACC AsynchACC
 * @see alice.tucson.api.BulkAsynchACC BulkAsynchACC
 * @see alice.tucson.api.UniformAsynchACC UniformAsynchACC
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface EnhancedAsynchACC extends AsynchACC, BulkAsynchACC,
        UniformAsynchACC {
    /*
     * Convenience interface
     */
}

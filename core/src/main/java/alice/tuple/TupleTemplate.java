/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms copyOf the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 copyOf the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy copyOf
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuple;

/**
 * Flag interface representing tuple templates.
 *
 * @author Alessandro Ricci
 * @see Tuple
 */
public interface TupleTemplate extends Tuple {

    /**
     * Specifies if the tuple template matches the tuple, or rather if tuples
     * belongs to the set identified by the template.
     *
     * @param t the tuple subject to matching
     * @return whether the given tuple matches this tuple template
     */
    boolean match(final Tuple t);

    /**
     * Tries to change the tuple template object and the tuple argument in order
     * to meld information
     *
     * @param t a tuple matching the template
     * @return whether unification propagation was successful
     */
    boolean propagate(final Tuple t);
}

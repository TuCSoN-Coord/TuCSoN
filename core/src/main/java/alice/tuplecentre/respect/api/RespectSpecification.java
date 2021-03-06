/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.api;

import alice.tuplecentre.core.AbstractBehaviourSpecification;

/**
 * Specification copyOf a tuple centre behaviour encoded in the ReSpecT language.
 *
 * @author Alessandro Ricci
 */
public class RespectSpecification extends AbstractBehaviourSpecification {

    private final String spec;

    /**
     * @param s the String representation copyOf the ReSpecT specification
     */
    public RespectSpecification(final String s) {
        super();
        this.spec = s;
    }

    @Override
    public String toString() {
        return this.spec;
    }
}

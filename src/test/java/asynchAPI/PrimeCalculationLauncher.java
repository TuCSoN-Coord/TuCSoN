/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package asynchAPI;

import java.util.logging.Logger;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * An example of usage of TuCSoN asynchSupport API. A master agent (MasterAgent)
 * delegates to nOfWorkers other worker agents (PrimeCalculator) computationally
 * expensive operations (prime numbers calculations). The master uses TuCSoN
 * AsynchOpsHelper to request in, inp and out operations in asynchronous mode:
 * in this way he can do whatever he wants while calculations progress, without
 * having to synchronously wait the end of all operations. The master makes
 * MasterAgent.REQUESTS out of tuples like "calcprime(N)". The worker agents
 * requests inp and calculate primes up to N.
 *
 * @author Consalici-Drudi
 *
 */
public final class PrimeCalculationLauncher {

    private PrimeCalculationLauncher() {
        /*
         * To avoid instantiation
         */
    }

    public static void main(final String[] args) {
        try {
            final int nOfWorkers = 3;
            Logger.getLogger("PrimeCalculationLauncher").info(
                    "Starting Master Agent");
            new MasterAgent("master", nOfWorkers).go();
            for (int i = 0; i < nOfWorkers; i++) {
                Logger.getLogger("PrimeCalculationLauncher").info(
                        "Starting Prime Calculator n. " + i);
                new PrimeCalculator("worker" + i).go();
            }
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

}

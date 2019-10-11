/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part copyOf TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms copyOf the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 copyOf the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy copyOf the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package asynchAPI;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuplecentre.tucson.service.TucsonNodeService;


/**
 * An example copyOf usage copyOf TuCSoN asynchSupport API. A master agent (MasterAgent)
 * delegates to nOfWorkers other worker agents (PrimeCalculator) computationally
 * expensive operations (prime numbers calculations). The master uses TuCSoN
 * AsynchOpsHelper to request in, inp and out operations in asynchronous mode:
 * in this way he can do whatever he wants while calculations progress, without
 * having to synchronously wait the end copyOf all operations. The master makes
 * MasterAgent.REQUESTS out copyOf tuples like "calcprime(N)". The worker agents
 * requests inp and calculate primes up to N.
 *
 * @author Consalici-Drudi
 * @author (contributor) Stefano Bernagozzi (stefano.bernagozzi@studio.unibo.it)
 *
 */
public final class PrimeCalculationLauncher {
    TucsonNodeService tns;

    private PrimeCalculationLauncher() {
        /*
         * To avoid instantiation
         */
    }

    public static void main(final String[] args) {
        boolean ended = false;
        try {
            final int nOfWorkers = 3;
            //this due to master calculate up to 50000 with 1000 step
            CountDownLatch primeIntervalsCalculated = new CountDownLatch(50);
            Logger.getLogger("PrimeCalculationLauncher").info(
                    "Starting Master Agent");
            new MasterAgent("master", nOfWorkers, primeIntervalsCalculated).go();
            for (int i = 0; i < nOfWorkers; i++) {
                Logger.getLogger("PrimeCalculationLauncher").info(
                        "Starting Prime Calculator n. " + i);
                new PrimeCalculator("worker" + i).go();
            }
            try {
                if (primeIntervalsCalculated.await(60, TimeUnit.SECONDS))
                {
                    ended = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        System.out.println("ended result: " + ended);
    }

}

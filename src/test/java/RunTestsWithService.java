import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import asynchAPI.MasterAgent;
import asynchAPI.PrimeCalculator;
import diningPhilos.DiningPhilosophersTest;
import distributedDiningPhilos.DDiningPhilosophersTest;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classic Dining Philosophers coordination problem tackled by adopting a clear
 * separation copyOf concerns between coordinables (philosophers) and coordination
 * medium (table) thanks to TuCSoN ReSpecT tuple centres programmability.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Stefano Bernagozzi (stefano.bernagozzi@studio.unibo.it)
 */
@TestMethodOrder(MethodOrderer.Random.class)
public class RunTestsWithService {

    TucsonNodeService tns;

    @BeforeEach
    public void initialSetup() {
        System.out.println("[[[initializing]]]");
        this.tns = new TucsonNodeService(TucsonInfo.getDefaultPortNumber());
        tns.install();
        try {
            while (!TucsonNodeService.isInstalled(TucsonInfo.getDefaultPortNumber(), 5000)) {
                Thread.sleep(1000);
            }
        } catch (final InterruptedException | DialogInitializationException e) {
            System.out.println("[[[[error]]]]]");
            e.printStackTrace();
        }
    }



    @AfterEach
    public void termination() {
        tns.shutdown();

        try{
            Thread.sleep(40000);
        } catch (Exception e){
            e.printStackTrace();
        }


    }






    @Test
    public void testDDinigPhilo(){

        System.out.println("[[[initializing]]]");
        TucsonNodeService tns2 = new TucsonNodeService(TucsonInfo.getDefaultPortNumber() + 1);
        tns2.install();
        try {
            while (!TucsonNodeService.isInstalled(TucsonInfo.getDefaultPortNumber() + 1, 5000)) {
                Thread.sleep(1000);
            }
        } catch (final InterruptedException | DialogInitializationException e) {
            System.out.println("[[[[error]]]]]");
            e.printStackTrace();
        }

        System.out.println("[[[initializing testDDinigPhilo]]]");

        boolean ended = false;
        try {
            CountDownLatch latch = new CountDownLatch(5);
            new DDiningPhilosophersTest("boot", latch).go();
            try {
                if (latch.await(300, TimeUnit.SECONDS)) {
                    ended = true;
                    System.out.println("done");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        tns2.shutdown();
        assertTrue(ended);
    }

    @Test
    public void testPrimeCalculator() {
        boolean ended = false;
        System.out.println("[[[initializing testPrimeCalculator]]]");
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
                if (primeIntervalsCalculated.await(100, TimeUnit.SECONDS)) {
                    ended = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        assertTrue(ended);
    }

    @Test
    void testTimedPhilo() {
        boolean ended = false;
        try {
            CountDownLatch latch = new CountDownLatch(5);
            new DiningPhilosophersTest("boot", latch).go();
            try {
                if (latch.await(120, TimeUnit.SECONDS)) {
                    ended = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        assertTrue(ended);
    }

    @Test
    void testPhilo() {
        boolean ended = false;
        try {
            CountDownLatch latch = new CountDownLatch(5);
            new DiningPhilosophersTest("boot", latch).go();
            try {
                if (latch.await(240, TimeUnit.SECONDS)) {
                    ended = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        assertTrue(ended);
    }
}

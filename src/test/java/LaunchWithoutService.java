import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import asynchAPI.MasterAgent;
import asynchAPI.PrimeCalculator;
import diningPhilos.DiningPhilosophersTest;
import distributedDiningPhilos.DDiningPhilosophersTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaunchWithoutService {
    TucsonNodeService tns1;
    TucsonNodeService tns2;

    public void initialSetup(int tns, int portNumber) {
        System.out.println("[[[initializing]]]");
        if (tns ==1) {
            this.tns1 = new TucsonNodeService(portNumber);
            this.tns1.install();
        }
        else{
            this.tns2 = new TucsonNodeService(portNumber);
            this.tns2.install();
        }
        try {
            while (!TucsonNodeService.isInstalled(portNumber, 5000)) {
                Thread.sleep(1000);
            }
        } catch (final InterruptedException | DialogInitializationException e) {
            System.out.println("[[[[error]]]]]");
            e.printStackTrace();
        }
    }



    public void termination() {
        this.tns1.shutdown();
    }

    public void termination2(){
        this.tns2.shutdown();
    }

    boolean testDDiningPhilo(){

        System.out.println("[[[initializing testDDinigPhilo]]]");

        boolean ended = false;
        try {
            CountDownLatch latch = new CountDownLatch(5);
            new DDiningPhilosophersTest("boot", latch).go();
            try {
                if (latch.await(100, TimeUnit.SECONDS)) {
                    ended = true;
                    System.out.println("done");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        return ended;

    }


    boolean testTimedPhilo() {
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
        return ended;
    }

    boolean testPhilo() {
        boolean ended = false;
        try {
            CountDownLatch latch = new CountDownLatch(5);
            new DiningPhilosophersTest("boot", latch).go();
            try {
                if (latch.await(30, TimeUnit.SECONDS)) {
                    ended = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        return ended;
    }

    public boolean testPrimeCalculator() {
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
        return ended;
    }
    

    public void executeWithoutService(){
        Integer[] array = new Integer[]{1, 2, 3, 4};
        List<Integer> l = Arrays.asList(array);
        Collections.shuffle(l);
        for (int i =0; i<4;i++){
            switch (l.get(i)){
                case 1:
                    this.testPrimeCalculator();
                    break;
                case 2:
                    this.testPhilo();
                    break;
                case 3:
                    this.testTimedPhilo();
                    break;
                case 4:
                    this.testDDiningPhilo();
                    break;
            }
        }
    }


    public static void main(String[] args) {
        new LaunchWithoutService().executeWithoutService();
        System.out.println("terminata");
    }
}

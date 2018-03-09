/**
 * Thermostat.java
 */
package situatedness;

import java.io.IOException;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 * TuCSoN situatedness feature example.
 *
 * In this toy scenario, a situated, 'intelligent' thermostat is in charge of
 * keeping a room temperature between 18 and 22. In order to do so, it is
 * equipped with a sensor (ActualSensor class) and an actuator (ActualActuator
 * class). As obvious, the former is requested by the thermostat to perceiving
 * the temperature, whereas the latter is prompted to change the temperature
 * upon need.
 *
 * Whereas the thermostat entity can be programmed as pleased, hence as an agent
 * or a simple Java process (still a TuCSoN agent, as in this case), the sensor
 * and the actuator should be modelled as "probes" (aka environmental
 * resources), interfacing with the MAS (in this simple case, only the
 * thermostat TuCSoN agent) through one transducer each.
 *
 * Furthermore, to leverage a possible ditributed scenario for this toy
 * thermostat example, transducers and the thermostat each have their own tuple
 * centre to interact with, suitably programmed through situated ReSpecT
 * reactions (sensorSpec.rsp and actuatorSpec.rsp).
 *
 * @author ste (mailto: s.mariani@unibo.it) on 05/nov/2013
 *
 */
public final class Thermostat {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "20504";
    private static final int HIGH = 22;
    private static final int ITERS = 10;
    private static final int LOW = 18;

    /**
     * @param args
     *            no args expected
     */
    public static void main(final String[] args) {
        try {
            final TucsonAgentId aid = new TucsonAgentId("thermostat");
            final NegotiationACC negACC = TucsonMetaACC.getNegotiationContext(
                    aid, Thermostat.DEFAULT_HOST,
                    Integer.valueOf(Thermostat.DEFAULT_PORT));
            final EnhancedSyncACC acc = negACC.playDefaultRole();
            /*
             * final EnhancedSyncACC acc = TucsonMetaACC.getACC(aid,
             * Thermostat.DEFAULT_HOST,
             * Integer.valueOf(Thermostat.DEFAULT_PORT));
             */
            final TucsonTupleCentreId configTc = new TucsonTupleCentreId(
                    "'$ENV'", Thermostat.DEFAULT_HOST, Thermostat.DEFAULT_PORT);
            /* Set up temperature */
            final TucsonTupleCentreId tempTc = new TucsonTupleCentreId(
                    "tempTc", Thermostat.DEFAULT_HOST, Thermostat.DEFAULT_PORT);
            int bootT;
            do {
                // 10 < bootT < LOW || HIGH < bootT < 30
                bootT = Math.round((float) (Math.random() * 20)) + 10;
            } while (bootT >= Thermostat.LOW && bootT <= Thermostat.HIGH);
            final LogicTuple bootTemp = new LogicTuple("temp", new Value(bootT));
            acc.out(tempTc, bootTemp, null);
            /* Set up sensor */
            Thermostat.log(aid.toString(), "Set up sensor...");
            final TucsonTupleCentreId sensorTc = new TucsonTupleCentreId(
                    "sensorTc", Thermostat.DEFAULT_HOST,
                    Thermostat.DEFAULT_PORT);
            try {
                acc.setS(
                        sensorTc,
                        Utils.fileToString("alice/tucson/examples/situatedness/sensorSpec.rsp"),
                        null);
            } catch (final IOException e) {
                e.printStackTrace();
            }
            final LogicTuple sensorTuple = new LogicTuple(
                    "createTransducerSensor",
                    new TupleArgument(sensorTc.toTerm()),
                    new Value(
                            "alice.tuplecentre.tucson.examples.situatedness.SensorTransducer"),
                            new Value("sensorTransducer"), new Value(
                    "alice.tuplecentre.tucson.examples.situatedness.ActualSensor"),
                                    new Value("sensor"));
            acc.out(configTc, sensorTuple, null);
            /* Set up actuator */
            Thermostat.log(aid.toString(), "Set up actuator...");
            final TucsonTupleCentreId actuatorTc = new TucsonTupleCentreId(
                    "actuatorTc", Thermostat.DEFAULT_HOST,
                    Thermostat.DEFAULT_PORT);
            try {
                acc.setS(
                        actuatorTc,
                        Utils.fileToString("alice/tucson/examples/situatedness/actuatorSpec.rsp"),
                        null);
            } catch (final IOException e) {
                e.printStackTrace();
            }
            final LogicTuple actuatorTuple = new LogicTuple(
                    "createTransducerActuator",
                    new TupleArgument(actuatorTc.toTerm()),
                    new Value(
                            "alice.tuplecentre.tucson.examples.situatedness.ActuatorTransducer"),
                            new Value("actuatorTransducer"),
                            new Value(
                                    "alice.tuplecentre.tucson.examples.situatedness.ActualActuator"),
                                    new Value("actuator"));
            acc.out(configTc, actuatorTuple, null);
            /* Start perception-reason-action loop */
            Thermostat.log(aid.toString(),
                    "Start perception-reason-action loop...");
            LogicTuple template;
            TucsonOperation op;
            int temp;
            LogicTuple action = null;
            for (int i = 0; i < Thermostat.ITERS; i++) {
                Thread.sleep(3000);
                /* Perception */
                template = LogicTuple.parse("sense(temp(_))");
                op = acc.in(sensorTc, template, null);
                if (op.isResultSuccess()) {
                    temp = op.getLogicTupleResult().getArg(0).getArg(0)
                            .intValue();
                    Thermostat.log(aid.toString(), "temp is " + temp
                            + " hence...");
                    /* Reason */
                    if (temp >= Thermostat.LOW && temp <= Thermostat.HIGH) {
                        Thermostat.log(aid.toString(), "...nothing to do");
                        continue;
                    } else if (temp < Thermostat.LOW) {
                        Thermostat.log(aid.toString(), "...heating up");
                        action = LogicTuple.parse("act(temp(" + ++temp + "))");
                    } else if (temp > Thermostat.HIGH) {
                        Thermostat.log(aid.toString(), "...cooling down");
                        action = LogicTuple.parse("act(temp(" + --temp + "))");
                    }
                    /* Action */
                    acc.out(actuatorTc, action, null);
                }
            }
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        }
    }

    private static void log(final String who, final String msg) {
        System.out.println("[" + who + "]: " + msg);
    }

    private Thermostat() {
        /*
         *
         */
    }
}

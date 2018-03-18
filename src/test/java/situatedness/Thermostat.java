/**
 * Thermostat.java
 */
package situatedness;

import java.io.IOException;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;
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
    private static final int HIGH = 22;
    private static final int ITERS = 10;
    private static final int LOW = 18;

    /**
     * @param args
     *            no args expected
     */
    public static void main(final String[] args) {
        try {
            final TucsonAgentId aid = new TucsonAgentIdDefault("thermostat");
            final NegotiationACC negACC = TucsonMetaACC.getNegotiationContext(
                    aid, Thermostat.DEFAULT_HOST,
                    TucsonInfo.getDefaultPortNumber());
            final EnhancedSyncACC acc = negACC.playDefaultRole();
            /*
             * final EnhancedSyncACC acc = TucsonMetaACC.getACC(aid,
             * Thermostat.DEFAULT_HOST,
             * Integer.valueOf(Thermostat.DEFAULT_PORT));
             */
            final TucsonTupleCentreId configTc = new TucsonTupleCentreIdDefault(
                    "'$ENV'", Thermostat.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
            /* Set up temperature */
            final TucsonTupleCentreId tempTc = new TucsonTupleCentreIdDefault(
                    "tempTc", Thermostat.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
            int bootT;
            do {
                // 10 < bootT < LOW || HIGH < bootT < 30
                bootT = Math.round((float) (Math.random() * 20)) + 10;
            } while (bootT >= Thermostat.LOW && bootT <= Thermostat.HIGH);
            final LogicTuple bootTemp = LogicTuples.newInstance("temp", TupleArguments.newValueArgument(bootT));
            acc.out(tempTc, bootTemp, null);
            /* Set up sensor */
            Thermostat.log(aid.toString(), "Set up sensor...");
            final TucsonTupleCentreId sensorTc = new TucsonTupleCentreIdDefault(
                    "sensorTc", Thermostat.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
            try {
                acc.setS(
                        sensorTc,
                        Utils.fileToString("situatedness/sensorSpec.rsp"),
                        null);
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            final LogicTuple sensorTuple = LogicTuples.newInstance(
                    "createTransducerSensor",
                    TupleArguments.newInstance(sensorTc.toTerm()),
                    TupleArguments.newValueArgument(
                            "situatedness.SensorTransducer"),
                    TupleArguments.newValueArgument("sensorTransducer"), TupleArguments.newValueArgument(
                            "situatedness.ActualSensor"),
                    TupleArguments.newValueArgument("sensor"));
            acc.out(configTc, sensorTuple, null);
            /* Set up actuator */
            Thermostat.log(aid.toString(), "Set up actuator...");
            final TucsonTupleCentreId actuatorTc = new TucsonTupleCentreIdDefault(
                    "actuatorTc", Thermostat.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
            try {
                acc.setS(
                        actuatorTc,
                        Utils.fileToString("situatedness/actuatorSpec.rsp"),
                        null);
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            final LogicTuple actuatorTuple = LogicTuples.newInstance(
                    "createTransducerActuator",
                    TupleArguments.newInstance(actuatorTc.toTerm()),
                    TupleArguments.newValueArgument(
                            "situatedness.ActuatorTransducer"),
                    TupleArguments.newValueArgument("actuatorTransducer"),
                    TupleArguments.newValueArgument(
                            "situatedness.ActualActuator"),
                    TupleArguments.newValueArgument("actuator"));
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
                template = LogicTuples.parse("sense(temp(_))");
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
                        action = LogicTuples.parse("act(temp(" + ++temp + "))");
                    } else {
                        Thermostat.log(aid.toString(), "...cooling down");
                        action = LogicTuples.parse("act(temp(" + --temp + "))");
                    }
                    /* Action */
                    acc.out(actuatorTc, action, null);
                }
            }
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidOperationException e) {
            LOGGER.error(e.getMessage(), e);
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

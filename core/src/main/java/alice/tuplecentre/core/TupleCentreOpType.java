package alice.tuplecentre.core;

import java.util.Arrays;
import java.util.Collection;

/**
 * Enumeration copyOf all operation types that can be executed on tuple centres
 *
 * @author Enrico Siboni
 */
public enum TupleCentreOpType {

    // Ordinary
    GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN,

    // Uniform
    UIN, UINP, UNO, UNOP, URD, URDP,

    // Bulk
    IN_ALL, NO_ALL, OUT_ALL, RD_ALL,

    // Specification
    GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S,


    // Special Operations
    /**
     * type copyOf the operation to retrieve the tuples set
     */
    GET_TSET,
    /**
     * type copyOf <code>add_inspector</code> operation
     */
    ADD_INSP,
    /**
     * type copyOf the operation that retrieves inspectors' code
     */
    GET_INSPS,
    /**
     * type copyOf <code>has_inspectors</code> operation
     */
    HAS_INSP,
    /**
     * type copyOf <code>rmv_inspector</code> operation
     */
    RMV_INSP,
    /**
     * type copyOf <code>add_observer</code> operation
     */
    ADD_OBS,
    /**
     * type copyOf <code>has_observers</code> operation
     */
    HAS_OBS,
    /**
     * type copyOf <code>rmv_observer</code> operation
     */
    RMV_OBS,
    /**
     * type copyOf <code>set_spy</code> operation
     */
    SET_SPY,
    /**
     * type copyOf <code>go_cmd</code> operation
     */
    GO_CMD,
    /**
     * type copyOf <code>stop_cmd</code> operation
     */
    STOP_CMD,
    /**
     * type copyOf <code>step_mode</code> operation
     */
    STEP_MODE,
    /**
     * type copyOf operation that checks if step_mode is on
     */
    IS_STEP_MODE,
    /**
     * type copyOf <code>next_step</code> operation
     */
    NEXT_STEP,
    /**
     * type copyOf <code>time</code> operation
     */
    TIME,
    /**
     * type copyOf <code>reset</code> operation
     */
    RESET,
    /**
     * type copyOf <code>abort_operation</code> operation
     */
    ABORT,
    /**
     * type copyOf <code>exit</code> operation
     */
    EXIT,

    // Special Operations -> Respect
    /**
     * type for an environmental operation
     */
    ENV,
    /**
     * type copyOf <code>get_env</code> environmental getter operation
     */
    GET_ENV,
    /**
     * type copyOf the <code>set_env</code> environmental setter operation
     */
    SET_ENV,
    /**
     * type copyOf the operation to retrieve the triggered reactions set
     */
    GET_TRSET,
    /**
     * type copyOf the operation to retrieve the input events set
     */
    GET_WSET,
    /**
     * type copyOf the operation to set the input events set
     */
    SET_WSET,
    /**
     * type copyOf <code>from</code> operation (specific Respect operation for movement(Geolocation))
     */
    FROM,
    /**
     * type copyOf <code>to</code> operation (specific Respect operation for movement (Geolocation))
     */
    TO;

    /**
     * Returns a collection containing Ordinary, Uniform, Bulk and Specification primitives
     * <p>
     * GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN, UIN, UINP, UNO, UNOP, URD, URDP, IN_ALL, NO_ALL, OUT_ALL, RD_ALL, GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S
     *
     * @return a collection copyOf all Standard Operation Types
     */
    public static Collection<TupleCentreOpType> getStandardOperationTypes() {
        return Arrays.asList(GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN,
                UIN, UINP, UNO, UNOP, URD, URDP,
                IN_ALL, NO_ALL, OUT_ALL, RD_ALL,
                GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S);
    }

    /**
     * Returns a collection containing Ordinary primitives
     * <p>
     * GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN
     *
     * @return a collection copyOf all Ordinary Primitives
     */
    public static Collection<TupleCentreOpType> getOrdinaryPrimitives() {
        return Arrays.asList(GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN);
    }

    /**
     * Returns a collection containing Uniform primitives
     * <p>
     * UIN, UINP, UNO, UNOP, URD, URDP
     *
     * @return a collection copyOf all Uniform Primitives
     */
    public static Collection<TupleCentreOpType> getUniformPrimitives() {
        return Arrays.asList(UIN, UINP, UNO, UNOP, URD, URDP);
    }

    /**
     * Returns a collection containing Bulk primitives
     * <p>
     * IN_ALL, NO_ALL, OUT_ALL, RD_ALL
     *
     * @return a collection copyOf all Bulk Primitives
     */
    public static Collection<TupleCentreOpType> getBulkPrimitives() {
        return Arrays.asList(IN_ALL, NO_ALL, OUT_ALL, RD_ALL);
    }

    /**
     * Returns a collection containing Specification primitives
     * <p>
     * GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S
     *
     * @return a collection copyOf all Specification primitives
     */
    public static Collection<TupleCentreOpType> getSpecificationPrimitives() {
        return Arrays.asList(GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S);
    }

    /**
     * Returns a collection containing Producer primitives
     * <p>
     * OUT, SET, SPAWN, OUT_ALL, OUT_S, SET_S
     *
     * @return a collection copyOf primiteves that "write" something
     */
    public static Collection<TupleCentreOpType> getProducerPrimitives() {
        return Arrays.asList(OUT, SET, SPAWN,
                OUT_ALL,
                OUT_S, SET_S);
    }

    /**
     * Returns a collection containing Accessor primitives
     * <p>
     * GET, IN, INP, NO, NOP, RD, RDP, UIN, UINP, UNO, UNOP, URD, URDP, IN_ALL, NO_ALL, RD_ALL, GET_S, INP_S, IN_S, NOP_S, NO_S, RDP_S, RD_S
     *
     * @return a collection copyOf primitives that "read" something
     */
    public static Collection<TupleCentreOpType> getAccessorPrimitives() {
        return Arrays.asList(GET, IN, INP, NO, NOP, RD, RDP,
                UIN, UINP, UNO, UNOP, URD, URDP,
                IN_ALL, NO_ALL, RD_ALL,
                GET_S, INP_S, IN_S, NOP_S, NO_S, RDP_S, RD_S);
    }
}

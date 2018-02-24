package alice.tuplecentre.core;

import java.util.Arrays;
import java.util.Collection;

/**
 * Enumeration of all operation types that can be executed on tuple centres
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
     * type of the operation to retrieve the tuples set
     */
    GET_TSET,
    /**
     * type of <code>add_inspector</code> operation
     */
    ADD_INSP,
    /**
     * type of the operation that retrieves inspectors' code
     */
    GET_INSPS,
    /**
     * type of <code>has_inspectors</code> operation
     */
    HAS_INSP,
    /**
     * type of <code>rmv_inspector</code> operation
     */
    RMV_INSP,
    /**
     * type of <code>add_observer</code> operation
     */
    ADD_OBS,
    /**
     * type of <code>has_observers</code> operation
     */
    HAS_OBS,
    /**
     * type of <code>rmv_observer</code> operation
     */
    RMV_OBS,
    /**
     * type of <code>set_spy</code> operation
     */
    SET_SPY,
    /**
     * type of <code>go_cmd</code> operation
     */
    GO_CMD,
    /**
     * type of <code>stop_cmd</code> operation
     */
    STOP_CMD,
    /**
     * type of <code>step_mode</code> operation
     */
    STEP_MODE,
    /**
     * type of operation that checks if step_mode is on
     */
    IS_STEP_MODE,
    /**
     * type of <code>next_step</code> operation
     */
    NEXT_STEP,
    /**
     * type of <code>time</code> operation
     */
    TIME,
    /**
     * type of <code>reset</code> operation
     */
    RESET,
    /**
     * type of <code>abort_operation</code> operation
     */
    ABORT,
    /**
     * type of <code>exit</code> operation
     */
    EXIT,

    // Special Operations -> Respect
    /**
     * type for an environmental operation
     */
    ENV,
    /**
     * type of <code>get_env</code> environmental getter operation
     */
    GET_ENV,
    /**
     * type of the <code>set_env</code> environmental setter operation
     */
    SET_ENV,
    /**
     * type of the operation to retrieve the triggered reactions set
     */
    GET_TRSET,
    /**
     * type of the operation to retrieve the input events set
     */
    GET_WSET,
    /**
     * type of the operation to set the input events set
     */
    SET_WSET,
    /**
     * type of <code>from</code> operation (specific Respect operation for movement(Geolocation))
     */
    FROM,
    /**
     * type of <code>to</code> operation (specific Respect operation for movement (Geolocation))
     */
    TO;

    /**
     * Returns a collection containing Ordinary, Uniform, Bulk and Specification primitives
     * <p>
     * GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN, UIN, UINP, UNO, UNOP, URD, URDP, IN_ALL, NO_ALL, OUT_ALL, RD_ALL, GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S
     *
     * @return a collection of all Standard Operation Types
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
     * @return a collection of all Ordinary Primitives
     */
    public static Collection<TupleCentreOpType> getOrdinaryPrimitives() {
        return Arrays.asList(GET, IN, INP, NO, NOP, OUT, RD, RDP, SET, SPAWN);
    }

    /**
     * Returns a collection containing Uniform primitives
     * <p>
     * UIN, UINP, UNO, UNOP, URD, URDP
     *
     * @return a collection of all Uniform Primitives
     */
    public static Collection<TupleCentreOpType> getUniformPrimitives() {
        return Arrays.asList(UIN, UINP, UNO, UNOP, URD, URDP);
    }

    /**
     * Returns a collection containing Bulk primitives
     * <p>
     * IN_ALL, NO_ALL, OUT_ALL, RD_ALL
     *
     * @return a collection of all Bulk Primitives
     */
    public static Collection<TupleCentreOpType> getBulkPrimitives() {
        return Arrays.asList(IN_ALL, NO_ALL, OUT_ALL, RD_ALL);
    }

    /**
     * Returns a collection containing Specification primitives
     * <p>
     * GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S
     *
     * @return a collection of all Specification primitives
     */
    public static Collection<TupleCentreOpType> getSpecificationPrimitives() {
        return Arrays.asList(GET_S, INP_S, IN_S, NOP_S, NO_S, OUT_S, RDP_S, RD_S, SET_S);
    }
}

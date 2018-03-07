package alice.tuplecentre.respect.core.tupleset;

import java.util.Iterator;

import alice.logictuple.LogicTuple;

/**
 * This interface representing a Tuple Set.
 *
 * @author Saverio Cicora
 */
public interface ITupleSet {

    /**
     * Add a logic tuple to tuple set.
     *
     * @param t
     *            a {@code LogicTuple} to add.
     */
    void add(LogicTuple t);

    /**
     * Begins a transaction section
     *
     * Every operation on multiset can be undone
     */
    void beginTransaction();

    /**
     * Removes all element from Tuple Set.
     */
    void empty();

    /**
     * Ends a transaction section specifying if operations must be committed or
     * undone
     *
     * @param commit
     *            if {@code true} the operations are committed, else they are
     *            undone and the multiset is rolled back to the state before the
     *            <code>beginTransaction</code> invocation
     */
    void endTransaction(boolean commit);

    /**
     *
     * @return the Iterator through this set
     */
    Iterator<LogicTuple> getIterator();

    /**
     *
     * @param templ
     *            the tuple template to use for unification
     * @return the tuple selected for matching
     */
    LogicTuple getMatchingTuple(LogicTuple templ);

    /**
     * Returns {@code true} if the tuple set contains no elements.
     *
     * @return {@code true} if the tuple set contains no elements.
     */
    boolean isEmpty();

    /**
     * Tells whether there are changes in the tuple multi-set during a
     * transaction
     *
     * @return true if the ongoing transaction made any changes to the tuple
     *         multi-set
     */
    boolean operationsPending();

    /**
     *
     * @param templ
     *            the tuple template to use for unification
     * @return the tuple selected for matching
     */
    LogicTuple readMatchingTuple(LogicTuple templ);

    /**
     * Remove a logic tuple from tuple set.
     *
     * @param t
     *            a {@code LogicTuple} to remove.
     */
    void remove(LogicTuple t);

    /**
     * Returns the number of elements in this tuple set.
     *
     * @return the number of elements in this tuple set
     */
    int size();

    /**
     *
     * @return the Array representation of this tuple Set
     */
    LogicTuple[] toArray();

    /**
     * Provides a representation of the tuple multi-set in the form of a String
     * containing a prolog theory.
     *
     * @return a textual representation in the form of a prolog theory.
     */
    @Override
    String toString();
}

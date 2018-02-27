package alice.respect.core;

import java.util.ArrayList;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.respect.api.IManagementContext;
import alice.respect.api.RespectSpecification;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.introspection.WSetEvent;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;

/**
 *
 * @author Alessandro Ricci
 *
 */
public class ManagementContext implements IManagementContext {

    private final RespectVM vm;

    /**
     *
     * @param rvm
     *            the ReSpecT VM this context refers to
     */
    public ManagementContext(final RespectVM rvm) {
        this.vm = rvm;
    }

    @Override
    public boolean abortOperation(final long opId) {
        return this.vm.abortOperation(opId);
    }

    @Override
    public void addInspector(final InspectableEventListener l) {
        this.vm.addInspector(l);
    }

    @Override
    public void addObserver(final ObservableEventListener l) {
        this.vm.addObserver(l);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.respect.api.IManagementContext#disablePersistence(alice.tucson.
     * api.TucsonTupleCentreId)
     */
    @Override
    public void disablePersistency(final String path,
            final TucsonTupleCentreId fileName) {
        this.vm.disablePersistency(path, fileName);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.respect.api.IManagementContext#enablePersistence(alice.tucson.api
     * .TucsonTupleCentreId)
     */
    @Override
    public void enablePersistency(final String path,
            final TucsonTupleCentreId fileName) {
        this.vm.enablePersistency(path, fileName);
    }

    @Override
    public ArrayList<InspectableEventListener> getInspectors() {
        return this.vm.getInspectors();
    }

    @Override
    public RespectSpecification getSpec() {
        return (RespectSpecification) this.vm.getReactionSpec();
    }

    @Override
    public LogicTuple[] getTRSet(final LogicTuple t) {
        return this.vm.getTRSet();
    }

    @Override
    public LogicTuple[] getTSet(final LogicTuple t) {
        return this.vm.getTSet(t);
    }

    @Override
    public WSetEvent[] getWSet(final LogicTuple t) {
        return this.vm.getWSet(t);
    }

    @Override
    public void goCommand() throws OperationNotPossibleException {
        this.vm.goCommand();
    }

    @Override
    public boolean hasInspectors() {
        return this.vm.hasInspectors();
    }

    @Override
    public boolean hasObservers() {
        return this.vm.hasObservers();
    }

    @Override
    public boolean isStepModeCommand() {
        return this.vm.isStepModeCommand();
    }

    @Override
    public void nextStepCommand() throws OperationNotPossibleException {
        this.vm.nextStepCommand();
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.respect.api.IManagementContext#recoveryPersistent(alice.tucson.
     * api.TucsonTupleCentreId)
     */
    @Override
    public void recoveryPersistent(final String path, final String file,
            final TucsonTupleCentreId tcName) {
        this.vm.recoveryPersistent(path, file, tcName);
    }

    @Override
    public void removeInspector(final InspectableEventListener l) {
        this.vm.removeInspector(l);
    }

    @Override
    public void removeObserver(final ObservableEventListener l) {
        this.vm.removeObserver(l);
    }

    @Override
    public void reset() {
        this.vm.reset();
    }

    @Override
    public void setManagementMode(final boolean activate) {
        this.vm.setManagementMode(activate);
    }

    @Override
    public void setSpec(final RespectSpecification spec)
            throws InvalidSpecificationException {
        final boolean accepted = this.vm.setReactionSpec(spec);
        if (!accepted) {
            throw new InvalidSpecificationException(
                    "RespectSpecification value :" + spec.toString());
        }
    }

    @Override
    public void setWSet(final List<LogicTuple> wSet) {
        this.vm.setWSet(wSet);
    }

    @Override
    public void stepModeCommand() {
        this.vm.stepModeCommand();
    }

    @Override
    public void stopCommand() throws OperationNotPossibleException {
        this.vm.stopCommand();
    }
}

package alice.tuplecentre.respect.core;

import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.ISpatialContext;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.respect.api.place.IPlace;

/**
 * A Spatial Context wraps the access to a tuple centre virtual machine for a
 * specific thread of control, providing a spatial interface.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class SpatialContext implements ISpatialContext {
    /**
     * The ReSpecT VM
     */
    private final RespectVMContext vm;

    /**
     * Constructs a new spatial context
     *
     * @param rvmc the ReSpecT VM context
     */
    public SpatialContext(final RespectVMContext rvmc) {
        this.vm = rvmc;
    }

    /**
     * Gets the current ReSpecT VM local time
     *
     * @return the vm local time
     */
    @Override
    public long getCurrentTime() {
        return this.vm.getCurrentTime();
    }

    /**
     * Gets the current ReSpecT VM position
     *
     * @return the vm position
     */
    @Override
    public Position getPosition() {
        return this.vm.getPosition();
    }

    /**
     * Notifies a new input environment (spatial) events
     *
     * @param ev the events to handle
     */
    @Override
    public void notifyInputEnvEvent(final InputEvent ev) {
        this.vm.notifyInputEnvEvent(ev);
    }

    /**
     * Sets a specified place in the ReSpecT vm position
     *
     * @param place the new place
     */
    @Override
    public void setPosition(final IPlace place) {
        this.vm.getPosition().setPlace(place);
    }
}

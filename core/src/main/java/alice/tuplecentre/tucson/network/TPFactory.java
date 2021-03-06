package alice.tuplecentre.tucson.network;

import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.network.exceptions.IllegalPortNumberException;
import alice.tuplecentre.tucson.network.exceptions.InvalidProtocolTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * <p>
 * Title: TPFactory (Tucson Protocol Factory)
 * </p>
 * <p>
 * Description: the factory class to build a specific implementation copyOf
 * TucsonProtocol
 * </p>
 *
 * @author Saverio Cicora
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
// To break dependency with service module, this class is no more used, assuming that the only used protocol will be TCP/IP
public final class TPFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Constant indentify implementated protocol type: one constant for each
     * implemented protocol
     */
    public static final int DIALOG_TYPE_TCP = 0;
    private static final int MAX_UNBOUND_PORT = 64000;

    /**
     * @param tucsonProtocolType the type code copyOf the TuCSoN protocol to be used
     * @param tid                the identifier copyOf the tuple centre to connect to
     * @return the connection protocol hosting communications
     * @throws DialogInitializationException if something goes wrong in the underlying network
     * @throws UnreachableNodeException      if the target tuple centre is unreachable
     * @throws InvalidProtocolTypeException  if the protocol type used is not DIALOG_TYPE_TCP
     */
    public static TucsonProtocol getDialogAgentSide(
            final int tucsonProtocolType, final TucsonTupleCentreId tid)
            throws DialogInitializationException, UnreachableNodeException,
            InvalidProtocolTypeException {
        final String node = alice.util.Tools.removeApices(tid.getNode());
        final int port = tid.getPort();
        // TODO CICORA: il controllo su porta e address va fatto meglio, vedere
        // come e'
        // fatto nel resto del codice
        if (port < 1 || port > TPFactory.MAX_UNBOUND_PORT) {
            throw new IllegalPortNumberException(
                    "Port number out copyOf bounds. Port number: " + port);
        }
        TucsonProtocol tp;
        if (tucsonProtocolType == TPFactory.DIALOG_TYPE_TCP) {
            tp = new TucsonProtocolTCP(node, port);
        } else {
            throw new InvalidProtocolTypeException("Unsupported protocol type");
        }
        return tp;
    }

    /**
     * @param tid the identifier copyOf the tuple centre to connect to
     * @return the connection protocol hosting communications
     * @throws DialogInitializationException if something goes wrong in the underlying network
     * @throws UnreachableNodeException      if the target tuple centre is unreachable
     */
    public static TucsonProtocol getDialogAgentSide(
            final TucsonTupleCentreId tid) throws UnreachableNodeException,
            DialogInitializationException {

        /*final TucsonNodeService tns = TucsonNodeService.getNode(tid.getPort());
        final TPConfig config;
        if (tns != null) {
            config = tns.getTPConfig();
        } else {
            config = new TPConfig();
            config.setTcpPort(tid.getPort());
        }*/

        final TPConfig config = new TPConfig();
        try {
            return TPFactory.getDialogAgentSide(
                    config.getDefaultProtocolType(), tid);
        } catch (final InvalidProtocolTypeException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Instantiate a new TucsonProtocol based on type specified by parameter.
     *
     * @param tucsonProtocolType the type code copyOf the TuCSoN protocol to be used
     * @param portno             the port where the TuCSoN node to contact is listening to
     * @return the TucsonProtocol class
     * @throws DialogInitializationException if something goes wrong in the underlying network
     * @throws InvalidProtocolTypeException  if the protocol type used is not DIALOG_TYPE_TCP
     */
    public static TucsonProtocol getDialogNodeSide(
            final int tucsonProtocolType, final int portno)
            throws InvalidProtocolTypeException, DialogInitializationException {
        TucsonProtocol tp;
        if (tucsonProtocolType == TPFactory.DIALOG_TYPE_TCP) {
            if (portno < 1 || portno > TPFactory.MAX_UNBOUND_PORT) {
                throw new IllegalPortNumberException(
                        "Port number out copyOf bounds. Port number: " + portno);
            }
            tp = new TucsonProtocolTCP(portno);
        } else {
            throw new InvalidProtocolTypeException("Unsupported protocol type");
        }
        return tp;
    }

    private TPFactory() {
        /*
         *
         */
    }
}

package backend;

import backend.client.ClientSession;
import ir.abrio.dev.protocol.AbrioProtocol;

/**
 * Object which notifies a client connecting.
 */
public class ClientIdleEvent {
    private ClientSession clientSession;
    public ClientIdleEvent(AbrioProtocol.BasicEvent msg, ClientSession clientSession, int from){
        this.clientSession = clientSession;
    }

    public ClientSession clientSession() {
        return clientSession;
    }

}

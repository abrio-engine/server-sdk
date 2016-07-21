package backend;

import backend.client.ClientSession;
import ir.abrio.dev.protocol.AbrioProtocol;

/**
 * Object which notifies a client disconnecting
 */
public class ClientDownEvent {
    private ClientSession clientSession;
    public ClientDownEvent(AbrioProtocol.BasicEvent msg, ClientSession clientSession, int from){
        this.clientSession = clientSession;
    }

    public ClientSession clientSession() {
        return clientSession;
    }

}

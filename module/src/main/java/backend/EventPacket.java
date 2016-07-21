package backend;

import backend.client.ClientSession;
import ir.abrio.dev.protocol.AbrioProtocol;

/**
 * EventPacket class carries the wrapped message
 * and the corresponding ClientSession
 */
public class EventPacket {
    private AbrioProtocol.EventWrapper msg;
    private ClientSession session;
    private int from;
    public EventPacket(AbrioProtocol.EventWrapper msg,ClientSession session,int from){
        this.msg = msg;
        this.session = session;
        this.from = from;
    }

    public AbrioProtocol.EventWrapper msg() {
        return msg;
    }

    public ClientSession session() {
        return session;
    }

    public int from() {
        return from;
    }
}

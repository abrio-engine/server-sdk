package testbench;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import backend.EventPacket;
import backend.client.ClientSession;
import ir.abrio.dev.protocol.AbrioProtocol;
import ir.abrio.dev.sdk.AbrioComponent;

import java.rmi.NoSuchObjectException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is emulator of Project in server side.
 */
public class TesterActor<ComponentType extends AbrioComponent> extends UntypedActor {
    private ClientSessionC sessionCreator;
    private ActorRef componentRef;
    private Queue<EventPacket> outgoingMessagesBuffer;

    TesterActor(Class<ComponentType> componentType){
        outgoingMessagesBuffer = new LinkedList<EventPacket>();
        componentRef = context().actorOf(Props.create(new AbrioComponentC<ComponentType>(componentType)),
                "comp");
        sessionCreator = new ClientSessionC();
        context().actorOf(Props.create(sessionCreator));
    }

    public void onReceive(Object message) throws Exception {
        System.out.println("message came");
        if(message instanceof TestCase) {
            System.out.println(sessionCreator.getClientSession());
            componentRef.tell(
                    new EventPacket(
                            ((TestCase) message).getEventWrapper(),
                            sessionCreator.getClientSession(),0),
                    self()
            );
        }
        else if(message instanceof EventPacket){
            outgoingMessagesBuffer.add((EventPacket) message);
        }
    }

    /**
     * Get message synchronously from component
     * @return Event packet of component which contain message and session
     * @throws NoSuchObjectException no message from component in time
     */
    public EventPacket pullMessage() throws InterruptedException, NoSuchObjectException {return pullMessage(1);}

    /**
     * Get message synchronously from component
     * @param timeout times that retry to get message from component
     * @return Event packet of component which contain message and session
     * @throws NoSuchObjectException no message from component in time
     */
    public EventPacket pullMessage(int timeout) throws InterruptedException, NoSuchObjectException {
        for(int i = 0; i<=timeout;i++) {
            if (outgoingMessagesBuffer.size() > 0)
                return outgoingMessagesBuffer.poll();
            Thread.sleep(100);
        }
        throw new NoSuchObjectException("Component return no result in "+(100*timeout)+" mili-seconds");
    }

    /**
     * Send message to component
     * @param eventWrapper Message object
     */
    public void pushMessage(AbrioProtocol.EventWrapper eventWrapper){
        self().tell(new TestCase(eventWrapper), null);
    }

    private static class AbrioComponentC<ComponentType extends AbrioComponent> implements Creator<AbrioComponent> {
        Class<ComponentType> componentType;
        AbrioComponentC(Class<ComponentType> componentType){
            this.componentType = componentType;
        }
        public ComponentType create() {
            try {
                return componentType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ClientSessionC implements Creator<ClientSession> {
        private ClientSession clientSession = null;
        public ClientSession create() {
            System.out.println("Client Session is created ");
            clientSession = new ClientSession();
            return clientSession;
        }

        ClientSession getClientSession() {
            return clientSession;
        }
    }

    private static class TestCase {
        private final AbrioProtocol.EventWrapper eventWrapper;
        public TestCase(AbrioProtocol.EventWrapper eventWrapper){
            this.eventWrapper = eventWrapper;
        }

        AbrioProtocol.EventWrapper getEventWrapper() {
            return eventWrapper;
        }
    }
}

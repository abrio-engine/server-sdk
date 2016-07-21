package ir.abrio.dev.sdk;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import backend.*;
import backend.client.ClientSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.abrio.dev.protocol.AbrioProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * AbrioLogic is main abstract class.
 * Implement this class
 */
public abstract class AbrioComponent extends UntypedActor {

    private final Integer id;
    private AbrioMemory dataMemory;
    private AbrioStorage dataStorage;

    public AbrioComponent(String projectId) throws IOException {
        System.out.println("Hi I am abrio component for project: " + projectId + ". I am created at " + getSelf().path().toString());
        ComponentConfigures componentConfigures = loadConfig();
        componentConfigures.projectId = projectId;

        id = componentConfigures.componentId;

        dataStorage = new AbrioStorage(componentConfigures);
        dataMemory = new AbrioMemory(this);
    }

    private ComponentConfigures loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Converting JSON string from file to Object
        String configFileName = "config.json";
        InputStream in = getClass().getResourceAsStream(configFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ComponentConfigures componentConfigures = mapper.readValue(reader, ComponentConfigures.class);
        String prettyDbSettings = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(componentConfigures);
        System.out.println(prettyDbSettings);

        return componentConfigures;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof EventPacket) {
            receive(((EventPacket) message).msg(),
                    new Context(getSender(), ((EventPacket) message).session()));
        } else if (message instanceof ClientUpEvent) {
            connectionEstablished(
                    new Context(getSender(), ((ClientUpEvent) message).clientSession()));
        } else if (message instanceof ClientDownEvent) {
            connectionClosed(
                    new Context(getSender(), ((ClientDownEvent) message).clientSession()));
        } else if (message instanceof ClientIdleEvent) {
            connectionIdle(
                    new Context(getSender(), ((ClientIdleEvent) message).clientSession()));
        } else if (message instanceof ClientWakeUpEvent) {
            connectionWakeUp(
                    new Context(getSender(), ((ClientWakeUpEvent) message).clientSession()));
        } else {
            // todo throw exception
            System.out.println("Invalid message received in AbrioComponent: " + message.getClass().getName());
        }
    }

    protected void finalize() throws Throwable {

        super.finalize();
    }

    /**
     * This function will be called each time a message receive
     *
     * @param message received message
     * @param context Context object contain state of incoming event
     */
    abstract public void receive(AbrioProtocol.EventWrapper message, Context context);

    /**
     * This function will be called each time a client join logic
     *
     * @param context Context object contain state of incoming event
     */
    abstract public void connectionEstablished(Context context);

    /**
     * This function will be called each time a client disconnect from logic
     * and client session removed
     *
     * @param context Context object contain state of incoming event
     */
    abstract public void connectionClosed(Context context);

    /**
     * This function will be called each time a client lost its connection
     *
     * @param context Context object contain state of incoming event
     */
    abstract public void connectionIdle(Context context);

    /**
     * This function will be called each time a client come back after connection lost
     *
     * @param context Context object contain state of incoming event
     */
    abstract public void connectionWakeUp(Context context);


    public Integer id() {
        return id;
    }

    public AbrioMemory dataMemory() {
        return dataMemory;
    }

    public AbrioStorage dataStorage() {
        return dataStorage;
    }

    protected class Context {
        private ActorRef sender;
        private ClientSession session;

        Context(ActorRef sender, ClientSession session) {
            this.sender = sender;
            this.session = session;
        }

        /**
         * DO NOT override this function unless you know exactly what are you doing
         *
         * @param message this message will be send to client
         */
        public void send(AbrioProtocol.EventWrapper message) {
            sender.tell(new EventPacket(message, session, id()), getSelf());
        }

        public ClientSession getSession() {
            return session;
        }
    }
}

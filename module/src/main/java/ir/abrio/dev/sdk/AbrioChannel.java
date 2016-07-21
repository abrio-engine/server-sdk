package ir.abrio.dev.sdk;

import akka.actor.ActorRef;
import akka.actor.Kill;
import akka.actor.Props;
import akka.japi.Creator;
import backend.EventPacket;
import backend.SubscribeSession;
import backend.UnsubscribeSession;
import backend.client.ClientSession;
import backend.util.MessageChannel;
import ir.abrio.dev.protocol.AbrioProtocol;

/**
 * Abrio Channel let you broadcast message to clientSessions that subscribe to
 */
public class AbrioChannel {
    private ActorRef messageChannelActorRef;
    private AbrioComponent component;

    public AbrioChannel(AbrioComponent component) {
        this.component = component;
        messageChannelActorRef = component.context().actorOf(Props.create(new MessageChannelCreator()));
    }

    public void subscribe(ClientSession client) {
        client.setData(component.id(), "channel", this);
        messageChannelActorRef.tell(new SubscribeSession(client.self()), component.self());
    }

    public void unSubscribe(ClientSession client) {
        messageChannelActorRef.tell(new UnsubscribeSession(client.self()), component.self());
    }

    public void send(AbrioProtocol.EventWrapper message, AbrioComponent.Context context) {
        messageChannelActorRef.tell(new EventPacket(message, context.getSession(), component.id()), component.getSelf());
    }

    public void close() {
        System.out.println(messageChannelActorRef);
        messageChannelActorRef.tell(Kill.getInstance(), component.self());
    }

    static public AbrioChannel getChannel(AbrioComponent component, AbrioComponent.Context context) {
        return context.getSession().getData(component.id(), "channel", AbrioChannel.class);
    }

    private static class MessageChannelCreator implements Creator<MessageChannel> {
        public MessageChannel create() {
            return new MessageChannel();
        }
    }
}

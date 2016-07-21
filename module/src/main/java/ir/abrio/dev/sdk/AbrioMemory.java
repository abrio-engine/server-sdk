package ir.abrio.dev.sdk;

import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import backend.Delete;
import backend.Flush;
import backend.Get;
import backend.Update;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * Component Data Handler Class
 * provides some wrappers around LogicPersistentData actor
 */
class AbrioMemory {

    private ActorSelection logicPDataActorRef;
    private AbrioComponent component;

    AbrioMemory(AbrioComponent component) {
        logicPDataActorRef = component.getContext()
                .actorSelection(component.getSelf().path().parent().toString() + "/AbrioPersistentData");
        this.component = component;
    }


    public String get(String key) {
        // Default time out indicates how blocking could span .
        // 2s is probably more than enough if all components are on a single machine.
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Future<Object> future = Patterns.ask(logicPDataActorRef, new Get(key), timeout);
        String result = null;
        try {
            result = (String) Await.result(future, timeout.duration());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void update(String key, String payload) {
        logicPDataActorRef.tell(new Update(key, payload), component.getSelf());
    }

    public void delete(String key) {
        logicPDataActorRef.tell(new Delete(key), component.getSelf());
    }

    public void flush() {
        logicPDataActorRef.tell(new Flush(), component.getSelf());
    }
}
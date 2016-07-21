package testbench;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;
import ir.abrio.dev.sdk.AbrioComponent;

/**
 * Helper class to write a synchronous unit test for an AbrioComponent
 * @param <ComponentType> The class of Component that want to be tested.
 */
public class TestBench<ComponentType extends AbrioComponent> {
    final private Class<ComponentType> componentType;
    private final ActorSystem system;
    public TestBench(Class<ComponentType> componentType){
        system = ActorSystem.create("MySystem");
        this.componentType = componentType;
    }

    /**
     * Spawn component
     * @return TesterActor
     */
    public TesterActor<ComponentType> createTester() throws InterruptedException {
        TesterActorC<ComponentType> creator = new TesterActorC<ComponentType>(componentType);
        system.actorOf(Props.create(creator),"tester");
        Thread.sleep(1000);
        return creator.getTesterActor();
    }


    /**
     * Finish emulation
     */
    public void done() {
        system.terminate();
    }


    private static class TesterActorC<ComponentType extends AbrioComponent> implements Creator<TesterActor> {
        private Class<ComponentType> componentType;
        private TesterActor<ComponentType> instance = null;
        TesterActorC(Class<ComponentType> componentType){
            this.componentType = componentType;
        }
        public TesterActor<ComponentType> create() {
            System.out.println("testerActorCreated");
            instance = new TesterActor<ComponentType>(componentType);
            return instance;
        }

        TesterActor<ComponentType> getTesterActor() {
            return instance;
        }
    }
}

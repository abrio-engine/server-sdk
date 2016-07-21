package backend.client;

import akka.actor.UntypedActor;


public class ClientSession extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {

    }

    public void setData(Integer componentId, String key, Object value){

    }

    public <T>T getData(Integer componentId, String key, Class<T> _type){
        return null;
    }

    public String deviceId(){
        return "";
    }
}

package ir.abrio.dev.protocol;

/**
 * Util class for simplify abrioProtocol messages
 */
public class AbrioMessages {
    static public AbrioProtocol.EventWrapper basicEvent(String title, String body){
        AbrioProtocol.BasicEvent.Builder basicEventBuilder = AbrioProtocol.BasicEvent.newBuilder();
        basicEventBuilder.setTitle(title);
        basicEventBuilder.setBody(body);
        return  AbrioProtocol.EventWrapper.newBuilder()
                .setEventType(AbrioProtocol.EventWrapper.EventType.BasicEvent)
                .setBasicEvent(basicEventBuilder)
                .build();
    }

    static public AbrioProtocol.EventWrapper response(int code){
        AbrioProtocol.Response.Builder responseEvent = AbrioProtocol.Response.newBuilder()
                .setType(Integer.toString(code));
        return AbrioProtocol.EventWrapper.newBuilder()
                .setEventType(AbrioProtocol.EventWrapper.EventType.Response)
                .setResponse(responseEvent)
                .build();
    }
}

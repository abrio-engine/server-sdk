package backend;

/**
 * Object used to Update via persistent data manager
 */
public class Update {
    private String key;
    private String payload;
    public Update(String Key, String Payload){
        this.key = Key;
        this.payload = Payload;
    }

    public String key(){ return key ; }
    public String payload() { return  payload ;}
}

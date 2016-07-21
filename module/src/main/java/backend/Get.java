package backend;

/**
 * Object used to Get via persistent data manager
 */
public class Get {
    private String key ;

    public Get(String Key) {
        this.key = Key;
    }

    public String key(){ return key ; }
}

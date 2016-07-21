package backend;

/**
 * Object used to Delete via persistent data manager
 */
public class Delete {
    private String key ;

    public Delete(String Key) {
        this.key = Key;
    }

    public String key(){ return key ; }
}

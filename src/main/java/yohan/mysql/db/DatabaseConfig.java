package yohan.mysql.db;

/**
 *
 * @author yohan
 */
public class DatabaseConfig {

    String url = "";
    String user = "";
    String pass = "";
    String root = "admin";
    String db = "";

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getRoot() {
        return root;
    }

    public String getDb() {
        return db;
    }

}

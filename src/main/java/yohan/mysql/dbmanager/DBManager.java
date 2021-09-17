//package yohan.mysql.dbmanager;
//
//import com.yohan.mysql.db.DatabaseConfig;
//import com.yohan.mysql.db.IDbFactory;
//import com.yohan.exceptions.CustomException;
//import yohan.db.mysql.MySqlDbFactory;
//
///**
// *
// * @author yohanr
// */
//public class DBManager {
//
//    static DBManager instance = new DBManager();
//    IDbFactory factory;
//
//    public void configure(DatabaseConfig config) throws CustomException {
//        //dbConfig = config;
//        factory = new MySqlDbFactory();
//        factory.setDbConfig(config);
//        factory.init();
//    }
//
//    public static DBManager getInstance() {
//
//        return instance;
//    }
//
//    public IDbFactory getFactory() {
//        return factory;
//    }
//}

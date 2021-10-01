///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yohan.db.mysql;
//
//import com.yohan.mysql.db.DatabaseConfig;
//import com.google.gson.Gson;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//import com.yohan.mysql.db.IDbFactory;
//import com.yohan.mysql.db.IDbSession;
//import yohan.exceptions.CustomException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import javax.naming.InitialContext;
//
///**
// *
// * @author yohanr
// */
//public class MySqlDbFactory implements IDbFactory {
//
//    InitialContext ctx;
//    MysqlDataSource pool;
//
//    String url = "";
//    String user = "";
//    String pass = "";
////    String root = "";
////    String rootPass = "";
//    String db = "";
//    Gson gson = new Gson();
//    MySqlDbSession currentSession;
//
//    public MySqlDbFactory() {
//    }
//
//    DatabaseConfig dbConfig;
//    boolean ignorePool = false;
//
//    public boolean isIgnorePool() {
//        return ignorePool;
//    }
//
//    public void setIgnorePool(boolean ignorePool) {
//        this.ignorePool = ignorePool;
//    }
//
//    @Override
//    public void setDbConfig(DatabaseConfig dbConfig) {
//        this.dbConfig = dbConfig;
//    }
//
//    @Override
//    public void init() throws CustomException {
//        try {
//            if (dbConfig == null) {
//                throw new Exception("Invalid database config");
//            }
//
//            url = dbConfig.getUrl();
//            // root = dbConfig.getRoot();
//            //rootPass = dbConfig.getRootPass();
//            user = dbConfig.getUser();
//            pass = dbConfig.getPass();
//            db = dbConfig.getDb();
//
//            ctx = new InitialContext();
//
//            if (!ignorePool) {
//                pool = new MysqlDataSource();
//                pool.setURL(url);
//                pool.setUser(user);
//                pool.setPassword(pass);
//                pool.setDatabaseName(db);
//            }
//
//        } catch (Exception ex) {
//
//            System.out.println("E001: @Database(ERROR):" + ex.getMessage());
//
//        }
//    }
//
//    public IDbSession getCurrentSession() throws CustomException {
//        return currentSession;
//    }
//
//    public void setCurrentSecstion(IDbSession dbSession) throws CustomException {
//        currentSession = (MySqlDbSession) dbSession;
//    }
//
//    public IDbSession getNewDbSession() throws CustomException {
//        try {
//            MySqlDbSession dbSession = new MySqlDbSession();
//
//            //System.out.println("A003: @Database: acquireing new db session..");
//            if (!ignorePool) {
//
//                if (pool == null) {
//                    System.out.println("A004: @Database: ------pool null------- ");
//                }
//                Connection dbx = pool.getConnection();
//                dbSession.set(dbx);
//
//                currentSession = dbSession;
//
//                return dbSession;
//            } else {
//                if (ctx == null) {
//                    System.out.println("A007: @Database: ------db null------- ");
//                }
//                Connection dbx = DriverManager.getConnection(db, user, pass);
//                dbSession.set(dbx);
//
//                currentSession = dbSession;
//
//                return dbSession;
//            }
//
//        } catch (Exception ex) {
//
//            System.out.println("E002: @Database(ERROR):" + ex.getMessage());
//
//        }
//        return null;
//    }
//
//    public void close() {
//        destroy();
//    }
//
//    private void destroy() {
//        try {
//
//            if (!ignorePool) {
//                //pool.close();
//            }
//            ctx.close();
//
//        } catch (Exception ex) {
//
//            System.out.println("E003:@Database(ERROR):" + ex.getMessage());
//
//        }
//    }
//
//}

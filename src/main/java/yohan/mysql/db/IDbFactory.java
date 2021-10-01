package yohan.mysql.db;

import yohan.exceptions.CustomException;



/**
 *
 * @author yohanr
 */
public interface IDbFactory {

    public void setDbConfig(DatabaseConfig dbConfig);

    public void init() throws CustomException;

    public IDbSession getCurrentSession() throws CustomException;

    public void setCurrentSecstion(IDbSession dbSession) throws CustomException;

    public IDbSession getNewDbSession() throws CustomException;

    public void close();

}

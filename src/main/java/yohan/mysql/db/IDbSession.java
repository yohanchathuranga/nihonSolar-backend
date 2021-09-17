package com.yohan.mysql.db;

import com.yohan.exceptions.CustomException;

/**
 *
 * @author yohanr
 */
public interface IDbSession {

    public void startTrananaction() throws CustomException;

    public void commit() throws CustomException;

    public void rollbck() throws CustomException;

    public void close() throws CustomException;

    public Object get();

    public void set(Object dbSession);

}

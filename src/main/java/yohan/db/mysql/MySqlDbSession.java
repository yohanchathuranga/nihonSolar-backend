/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yohan.db.mysql;


import java.sql.Connection;
import java.sql.SQLException;
import yohan.exceptions.CustomException;
import yohan.exceptions.DatabaseException;
import yohan.mysql.db.IDbSession;

/**
 *
 * @author yohanr
 */
public class MySqlDbSession implements IDbSession {

    Connection dbSession;

    public void startTrananaction() throws CustomException {
        try {
            dbSession.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public void commit() throws CustomException {
        try {
            dbSession.commit();
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public void rollbck() throws CustomException {

        try {
            dbSession.rollback();
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }

    }

    public void close() throws CustomException {
        try {
            dbSession.close();
        } catch (SQLException ex) {
            //throw new DatabaseException(ex.getMessage());
        }
    }

    public Object get() {
        return dbSession;
    }

    public void set(Object dbSession) {
        this.dbSession = (Connection) dbSession;
    }

}

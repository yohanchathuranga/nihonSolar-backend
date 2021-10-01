/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dataaccess;

import yohan.commons.filters.DOPropertyFilter;
import yohan.commons.filters.FilterUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import yohan.exceptions.CustomException;
import yohan.exceptions.DatabaseException;

/**
 *
 * @author yohan
 */
@Service
public class DAODataUtil {

    public String listFilterData(List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit, String tableName) throws CustomException {
        try {

            String sql = "select * from " + tableName + " where deleted = 0 ";

            String whereClause = "";
            if (filterData != null && filterData.size() > 0) {
                whereClause = FilterUtil.generateWhereClause((ArrayList<DOPropertyFilter>) filterData);
            }

            if (!whereClause.isEmpty()) {
                sql = sql + " and " + whereClause;
            }

            if (orderFields != null && orderFields.size() > 0) {
                String orderStr = String.join(",", orderFields);
                sql = sql + "order by " + orderStr;
                if (isDescending) {
                    sql += " desc";
                }
            } else {
                sql = sql + "order by id";
                if (isDescending) {
                    sql += " desc";
                }
            }

            sql = sql + " limit " + limit + " offset " + ((page - 1) * limit);

            return sql;

        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public String countFilterdata(List<DOPropertyFilter> filterData, String tableName) throws CustomException {
        try {
            String sql = "select count(*) as count from " + tableName + " where deleted = 0 ";
            String whereClause = "";
            if (filterData != null && filterData.size() > 0) {
                whereClause = FilterUtil.generateWhereClause((ArrayList<DOPropertyFilter>) filterData);
            }

            if (!whereClause.isEmpty()) {
               sql = sql + " and " + whereClause;
            }
            return sql;
        } catch (CustomException ex) {
            throw ex;
        }

    }
}

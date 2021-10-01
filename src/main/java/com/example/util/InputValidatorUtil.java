package com.example.util;

import yohan.exceptions.CustomException;
import yohan.exceptions.InvalidInputException;





/**
 *
 * @author yohan
 */
public class InputValidatorUtil {

    public static String validateStringProperty(String propertyName, String propertyValue) throws InvalidInputException {

        if (propertyValue == null) {
            throw new InvalidInputException("Invalid " + propertyName);
        }
        propertyValue = propertyValue.trim();
        if (propertyValue.isEmpty()) {
            throw new InvalidInputException("Invalid " + propertyName);
        }
        return propertyValue;
    }

    public static String fillDefaulePropertyValues(String propertyValue) throws CustomException {
        if (propertyValue == null) {
            propertyValue = "";
        } else {
            propertyValue = propertyValue.trim();
        }
        return propertyValue;
    }

    public static int fillPageDefauleValues(String pageValue) throws CustomException {
        int page = 0;
        if (pageValue == null) {
            page = 1;
        } else {
            pageValue = pageValue.trim();
            if (pageValue.isEmpty()) {
                page = 1;
            } else {
                try {
                    page = Integer.parseInt(pageValue);
                } catch (Exception e) {
                    InvalidInputException ex = new InvalidInputException("Invalid Input page");
                    throw ex;
                }
            }
        }
        if (page <= 0) {
            page = 1;
        }
        return page;
    }

    public static int fillLimitDefauleValues(String limitValue) throws CustomException {
        int limit = 0;
        if (limitValue == null) {
            limit = 15;
        } else {
            limitValue = limitValue.trim();
            if (limitValue.isEmpty()) {
                limit = 15;
            } else {
                try {
                    limit = Integer.parseInt(limitValue);
                } catch (Exception e) {
                    InvalidInputException ex = new InvalidInputException("Invalid Input limit");
                    throw ex;
                }
            }
        }
        if (limit <= 0) {
            limit = 15;
        }
        return limit;
    }

}

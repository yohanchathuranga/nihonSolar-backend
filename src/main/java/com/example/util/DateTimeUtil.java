/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.util;

import com.yohan.exceptions.DoesNotExistException;
import com.yohan.exceptions.InvalidInputException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Suranga
 */
public class DateTimeUtil {

    public static long getCurrentTime() {
        long currentTime = Instant.now().getEpochSecond();
        return currentTime;
    }

    public static LocalDate getLocalDate(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDate localDate = Instant.ofEpochSecond(epochTime).atZone(timeZoneId).toLocalDate();

        return localDate;

    }

    public static long getDayStartEpochTime(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            // timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }

        LocalDate localDate = Instant.ofEpochSecond(epochTime).atZone(timeZoneId).toLocalDate();
        Instant instant = localDate.atStartOfDay().atZone(timeZoneId).toInstant();
        long timeInMillis = instant.toEpochMilli();
        long startEpochTime = timeInMillis / 1000L;
        return startEpochTime;

    }

    public static long getDayEndEpochTime(LocalDate localDate) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        Instant instant = localDate.atStartOfDay().atZone(timeZoneId).toInstant();
        long timeInMillis = instant.toEpochMilli();
        long endEpochTime = timeInMillis / 1000L;
        endEpochTime = endEpochTime + (24 * 3600);
        return endEpochTime;

    }

    public static long getDayStartEpochTime(LocalDate localDate) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        Instant instant = localDate.atStartOfDay().atZone(timeZoneId).toInstant();
        long timeInMillis = instant.toEpochMilli();
        long startEpochTime = timeInMillis / 1000L;
        return startEpochTime;

    }

    public static long getDayEndEpochTime(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDate localDate = Instant.ofEpochSecond(epochTime).atZone(timeZoneId).toLocalDate();
        Instant instant = localDate.atStartOfDay().atZone(timeZoneId).toInstant();
        long timeInMillis = instant.toEpochMilli();
        long endEpochTime = timeInMillis / 1000L;
        endEpochTime = endEpochTime + (24 * 3600);
        return endEpochTime;

    }

    public static String getFormatDateTime(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime * 1000).atZone(timeZoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh.mm a");

        String formatDateTime = dateTime.format(formatter);
        return formatDateTime;
    }

    public static String getFormatDate(LocalDate dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatDateTime = dateTime.format(formatter);
        return formatDateTime;
    }

    public static LocalDate getLocalDate(String date) throws InvalidInputException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate;
        } catch (Exception ex) {
            throw new InvalidInputException("Invalid date or date pattern");
        }
    }

    public static String getStringDate(LocalDate localDate) throws InvalidInputException {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formatDateTime = localDate.format(formatter);
            return formatDateTime;
        } catch (Exception ex) {
            throw new InvalidInputException("Invalid date or date pattern");
        }

    }

    public static String getFormatTime(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            //TimeZone timeZone = TimeZone.getTimeZone(DataUtil.TIME_ZONE);
            //timeZoneId = timeZone.toZoneId();
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime * 1000).atZone(timeZoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh.mm a");

        String formatDateTime = dateTime.format(formatter);
        return formatDateTime;
    }

    public static String getStringFormatDateTime(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime * 1000).atZone(timeZoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, hh.mm a");

        String formatDateTime = dateTime.format(formatter);
        return formatDateTime;
    }

    public static String getStringMonth(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime * 1000).atZone(timeZoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");

        String month = dateTime.format(formatter);
        return month;
    }

    public static String getStringYear(long epochTime) throws DoesNotExistException {

        ZoneId timeZoneId = null;
        try {
            timeZoneId = ZoneId.of(DataUtil.TIME_ZONE);
        } catch (Exception e) {
            throw new DoesNotExistException("Time zone does not exist. Time zone : " + DataUtil.TIME_ZONE);
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(epochTime * 1000).atZone(timeZoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

        String year = dateTime.format(formatter);
        return year;
    }

}

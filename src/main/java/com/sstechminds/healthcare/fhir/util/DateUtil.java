package com.sstechminds.healthcare.fhir.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public static LocalDate parseToLocalDate(String sInputDate) {
       return LocalDate.parse(sInputDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String formatToDDMMYYYY(Date inputDate) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatToLocalDate(inputDate).format(formatters);
    }

    public static LocalDate formatToLocalDate(Date inputDate) {
        return Instant.ofEpochMilli(inputDate.getTime())
            .atZone(ZoneOffset.UTC) //TODO???
            .toLocalDate();
    }

    public static Date dateFromUTC(Date date){
        return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    public static Date dateToUTC(Date date){
//        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(date));
        return gmt;
    }
}

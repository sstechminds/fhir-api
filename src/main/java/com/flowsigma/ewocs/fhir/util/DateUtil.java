package com.flowsigma.ewocs.fhir.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public static Date parse(String sInputDate) {
        try {
            return format.parse(sInputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(Date inputDate) {
        return format.format(inputDate);
    }

    public static Date formattedDate(Date inputDate) {
        try {
            return format.parse(format.format(inputDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

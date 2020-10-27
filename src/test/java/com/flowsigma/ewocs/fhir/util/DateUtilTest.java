package com.flowsigma.ewocs.fhir.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.jupiter.api.Test;

// https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
public class DateUtilTest {

  @Test
  public void testStringToDate() {
    String sDate = "2013-05-08T09:33:27+00:00";

    LocalDate date = DateUtil.parseToLocalDate(sDate);

    assertEquals(date, LocalDate.of(2013, Month.MAY, 8));
  }

  @Test
  public void testFormatToDDMMYYYY() {
    String sDate = "2013-05-08T09:33:27+01:00";

    OffsetDateTime zonedDateTime = OffsetDateTime.parse(sDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    //This creates time in CST - which is Local time zone!
    Date date = new Date(zonedDateTime.toInstant().toEpochMilli());

    //Local date time in UTC without using offset!
    LocalDateTime ldt = zonedDateTime.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    System.out.println(ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    String sLocalDate = DateUtil.formatToDDMMYYYY(date);

    assertEquals(sLocalDate, "08-05-2013");
  }

  @Test
  public void testDateToUTC() {
    //https://gist.github.com/salomvary/79977bbdb3a9cccadac2
    //https://gist.github.com/nickrussler/7527851
    DateUtil.dateToUTC(new Date());
    OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
    Date date = Date.from(utc.toInstant());
    System.out.println(date);
  }
}

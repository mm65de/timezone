package de.mm65.github.timezone;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

class VeryBasicTest {

    final static long MILLIS_PER_SECOND = 1000;
    final static long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    final static long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    final static long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    @Test
    void veryBasicAndPartlyInteractiveTest() {
        final long millisNow = System.currentTimeMillis();
        long remainingMillis = millisNow;
        final long days = remainingMillis / MILLIS_PER_DAY;
        remainingMillis -=  days * MILLIS_PER_DAY;
        final long hours = remainingMillis / MILLIS_PER_HOUR;
        remainingMillis -=  hours * MILLIS_PER_HOUR;
        final long minutes = remainingMillis / MILLIS_PER_MINUTE;
        remainingMillis -=  minutes * MILLIS_PER_MINUTE;
        final long seconds = remainingMillis / MILLIS_PER_SECOND;
        remainingMillis -=  seconds * MILLIS_PER_SECOND;

        // How to show timezones:
        // X -> "Z", z -> "GMT", Z -> "+0000"
        final DateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        System.out.println("veryBasicAndPartlyInteractiveTest():");
        System.out.println("------------------------------------");
        System.out.println("                   millis = " + millisNow);
        System.out.println("         new Date(millis) = " + new Date(millisNow));
        System.out.println("        DateFormat as GMT = " + gmtDateFormat.format(new Date(millisNow)));
        System.out.println("    days | h:m:s | millis = "
                + days + " | " + hours + ":" + minutes + ":" + seconds + " | " + remainingMillis);
        System.out.println();

        // Check the GMT hour against the hour calculated from the milliseconds:
        final DateFormat gmtHour = new SimpleDateFormat("HH");
        gmtHour.setTimeZone(TimeZone.getTimeZone("GMT"));
        String hourString = gmtHour.format(new Date(millisNow));
        assertEquals(hours, Integer.parseInt(hourString));
    }

    @Test
    void aNewDateInitializesWithGmtMilliseconds() {
        final long millis = System.currentTimeMillis();
        final Date date = new Date();
        System.out.println("aNewDateInitializesWithGmtMilliseconds():");
        System.out.println("-----------------------------------------");
        System.out.println("            System.currentTimeMillis() = " + millis);
        System.out.println("                  new Date().getTime() = " + date.getTime());
        System.out.println("    currentTimeMillis - date.getTime() = " +  (millis - date.getTime()));
        System.out.println();
        assertEquals(millis, date.getTime(), 1);
    }

    @Test
    void timezonesDoNotInfluenceSystemCurrentMillis() {
        final double MAX_DELTA = 50;
        // How to find valid TimeZone IDs:
        //Arrays.asList(TimeZone.getAvailableIDs()).stream().forEach(tz -> System.out.println(tz));

        System.out.println("timezonesDoNotInfluenceSystemCurrentMillis():");
        System.out.println("---------------------------------------------");

        final TimeZone tzDefaultToBeRestored = TimeZone.getDefault();
        final long millis1 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("WET"));
        System.out.println(new Date());
        final long millis2 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        System.out.println(new Date());
        final long millis3 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("EET"));
        System.out.println(new Date());
        final long millis4 = System.currentTimeMillis();

        TimeZone.setDefault(tzDefaultToBeRestored);
        System.out.println(new Date());
        final long millis5 = System.currentTimeMillis();

        assertEquals(millis1, millis2, MAX_DELTA);
        assertEquals(millis2, millis3, MAX_DELTA);
        assertEquals(millis3, millis4, MAX_DELTA);
        assertEquals(millis4, millis5, MAX_DELTA);

        System.out.println("Deltas have been: "
                + (millis2 - millis1) + ", "
                + (millis3 - millis2) + ", "
                + (millis4 - millis3) + ", "
                + (millis5 - millis4) + "." );
        System.out.println();
    }

}
package de.mm65.github.timezone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


class VeryBasicTest {

    private static final Logger LOGGER = LogManager.getLogger();


    static final long MILLIS_PER_SECOND = 1000;
    static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    @Test
    void veryBasicAndPartlyInteractiveTest() {
        final long millisNow = System.currentTimeMillis();
        long remainingMillis = millisNow;
        final long days = remainingMillis / MILLIS_PER_DAY;
        remainingMillis -= days * MILLIS_PER_DAY;
        final long hours = remainingMillis / MILLIS_PER_HOUR;
        remainingMillis -= hours * MILLIS_PER_HOUR;
        final long minutes = remainingMillis / MILLIS_PER_MINUTE;
        remainingMillis -= minutes * MILLIS_PER_MINUTE;
        final long seconds = remainingMillis / MILLIS_PER_SECOND;
        remainingMillis -= seconds * MILLIS_PER_SECOND;

        // How to show timezones:
        // X -> "Z", z -> "GMT", Z -> "+0000"
        final DateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        LOGGER.info("veryBasicAndPartlyInteractiveTest():");
        LOGGER.info("------------------------------------");
        LOGGER.info("                   millis = {}", millisNow);
        LOGGER.info("         new Date(millis) = {}", new Date(millisNow));
        LOGGER.info("        DateFormat as GMT = {}", gmtDateFormat.format(new Date(millisNow)));
        LOGGER.info("    days | h:m:s | millis = {} | {}:{}:{} | {}",
                days, hours, minutes, seconds, remainingMillis);
        LOGGER.info("");

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
        LOGGER.info("aNewDateInitializesWithGmtMilliseconds():");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("            System.currentTimeMillis() = {}", millis);
        LOGGER.info("                  new Date().getTime() = {}", date.getTime());
        LOGGER.info("    currentTimeMillis - date.getTime() = {}", millis - date.getTime());
        LOGGER.info("");
        assertEquals(millis, date.getTime(), 1);
    }

    /**
     * <b>INFO:</b>
     * <p>How to find valid TimeZone IDs:</p>
     * <code>Arrays.asList(TimeZone.getAvailableIDs()).stream().forEach(tz -> LOGGER.info(tz));</code>
     */
    @Test
    void timezonesDoNotInfluenceSystemCurrentMillis() {
        final double MAX_DELTA = 50;

        LOGGER.info("timezonesDoNotInfluenceSystemCurrentMillis():");
        LOGGER.info("---------------------------------------------");

        final TimeZone tzDefaultToBeRestored = TimeZone.getDefault();
        final long millis1 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("WET"));
        LOGGER.info(new Date());
        final long millis2 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        LOGGER.info(new Date());
        final long millis3 = System.currentTimeMillis();

        TimeZone.setDefault(TimeZone.getTimeZone("EET"));
        LOGGER.info(new Date());
        final long millis4 = System.currentTimeMillis();

        TimeZone.setDefault(tzDefaultToBeRestored);
        LOGGER.info(new Date());
        final long millis5 = System.currentTimeMillis();

        assertEquals(millis1, millis2, MAX_DELTA);
        assertEquals(millis2, millis3, MAX_DELTA);
        assertEquals(millis3, millis4, MAX_DELTA);
        assertEquals(millis4, millis5, MAX_DELTA);

        LOGGER.info("Deltas have been: {}, {}, {}, {}",
                millis2 - millis1, millis3 - millis2, millis4 - millis3, millis5 - millis4);
        LOGGER.info("");
    }

    @Test
    void testCreateDateWithUTCtimeAsLocalTime() {
        LOGGER.info("testCreateDateWithUTCtimeAsLocalTime():");
        LOGGER.info("---------------------------------------");

        final TimeZone tz = TimeZone.getDefault();
        assertNotEquals("Coordinated Universal Time", tz.getDisplayName());

        LOGGER.info("Current default TimeZone (tz): {}", tz.getDisplayName());
        LOGGER.info("               tz.getOffset(): {}", tz.getOffset(new Date().getTime()));
        LOGGER.info("            tz.getRawOffset(): {}", tz.getRawOffset());
        LOGGER.info("   DateWithUTCtimeAsLocalTime: {}", createDateWithUTCtimeAsLocalTime());
        LOGGER.info("");
    }

    private Date createDateWithUTCtimeAsLocalTime() {
        final TimeZone tz = TimeZone.getDefault();
        final long currentMillis = new Date().getTime();
        final int currentOffsetMillis = tz.getOffset(currentMillis);
        return new Date(currentMillis - currentOffsetMillis);
    }

}

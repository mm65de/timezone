package de.mm65.github.timezone;

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DatabaseTest {

    static final DBConfig[] dbConfigs = new DBConfig[] {
            DBConfig.builder().url("jdbc:h2:~/test").usr("sa").pwd("").build(),
            DBConfig.builder().url("jdbc:h2:tcp://localhost/~/test;AUTO_SERVER=TRUE").usr("sa").pwd("").build(),
            DBConfig.builder().url("jdbc:h2:~/.h2/timezone;AUTO_SERVER=TRUE").usr("sa").pwd("").build(),
    };
    static final int selectedConfig = 2;

    private static Connection con;

    @BeforeAll
    static void beforeAll() throws SQLException {
        final DBConfig dbConfig = dbConfigs[selectedConfig];
        con = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsr(), dbConfig.getPwd());
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        final String sql = """
                drop table if exists T cascade;
                create table T (
                    id int primary key,
                    name varchar(255) default '' not null,
                    mydate date,
                    mytime time,
                    mytimestamp timestamp,
                    mytimetz time with time zone,
                    mytimestamptz timestamp with time zone
                );                                                                   
                """;
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        ps.close();
    }

    @AfterEach
    void afterEach() throws SQLException {
    }

    @AfterAll
    static void afterAll() throws SQLException {
        if (con != null) {
            con.close();
            con = null;
        }
    }

    @Test
    void test() throws SQLException {
        final String sql = """
                insert into T (id, name, mydate, mytime, mytimestamp, mytimetz, mytimestamptz)
                values (?, ?, ?, ?, ?, ?, ?);                                                                   
                """;
        final java.sql.Date epochDate = java.sql.Date.valueOf("1970-01-01");
        final java.sql.Time epochTime = java.sql.Time.valueOf("00:00:00");
        final java.sql.Timestamp epochTimestamp = java.sql.Timestamp.valueOf("1970-01-01 00:00:00");
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, 1);
        ps.setString(2, "testOnly");
        ps.setDate(3, epochDate);
        ps.setTime(4, epochTime);
        ps.setTimestamp(5, epochTimestamp);
        ps.setTime(6, epochTime, cal);
        ps.setTimestamp(7, epochTimestamp, cal);
        ps.execute();
        ps.close();

        fail("oh");
    }

}


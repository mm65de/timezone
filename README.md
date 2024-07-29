***** THIS PAGE AND PROJECT IS STILL UNDER CONSTRUCTION *****
# timezone
Working with timezones in a distributed environment

## General considerations
Working with timezones in a distributed environment is difficult. 
If you want to store dates and time you first have to consider the nature of the dates and times that you want to store:
Some of your data should consider a timezone, others must not.

### Examples for Data without timezone information
| Type | Example |
| :---: | :--- |
| date | Your _date of birth_ does not change if you travel around. If a police office asks you for your date of birth, you are not allowed to convert the date written in your id card to the timezone the police officer is currently located in. |
| time | If your doctor tells you to take your pills _in the morning_, then this applies to _any_ place, independently of where you travel to. It is always in _in the morning_ considered in local time. |

### Examples for Data with timezone information
| Type | Example |
| :---: | :--- |
| date and time | Imagine a job is started on some machine in some country far away. In the logfile you probably want to show the time in UTC to be able to collate messages from around the world in one combined log file. In a client application you want to show this UTC timestamp in local time that the user easily sees, that the job was started just two minutes ago. |

### Typical Problems
If you store your _date of birth_  by an instance of `java.util.Date`, you could get in trouble: A `java.util.Date` internally always stores the count of milliseconds since `1970-01-01 00:00:00 UTC`. The first problem is, that your date of birth does not specify hours, minutes and seconds, but `java.util.Date` does. The second problem is, that if you are currently located in NYC, you have to specify 7 p.m. *of the eve* of your date of birth to get midnight of your date of birth in UTC.

## Java means
Instances of type `java.util.Date` always store the milliseconds since `1970-01-01 00:00:00 +00:00` that means GMT or UTC: Please see the description of [java.util.Date](https://docs.oracle.com/javase/8/docs/api/java/util/Date.html). JDBC provides the three types [java.sql.Date](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/Date.html), [java.sql.Time](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/Time.html) and [java.sql.Timestamp](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/Timestamp.html). Constructors of these types, that specify values for year, month, day, hour, minute, second, etc. are deprecated since Java 1.2. The proper way to construct these values is specifying a long value representing the milliseconds since [Epoch](https://en.wikipedia.org/wiki/Unix_time#Definition), i.e. January 1, 1970 00:00:00 GMT.

## Database means
### Standard SQL
According to this [Wikipedia section](https://en.wikipedia.org/wiki/SQL#Predefined_data_types) the SQL Standard provides the predefined data types DATE, TIME and TIMESTAMP.

### PostgreSQL
#### Documentation
* [Date/Time Types](https://www.postgresql.org/docs/8.1/datatype-datetime.html)
* [Date/Time Functions and Operators](https://www.postgresql.org/docs/15/functions-datetime.html)
* [Millis from Timestamp](https://dba.stackexchange.com/questions/266444/postgres-epoch-from-current-timestamp)
### Oracle
#### Documentation
* [Datetime Data Types and Time Zone Support](https://docs.oracle.com/en/database/oracle/oracle-database/21/nlspg/datetime-data-types-and-time-zone-support.html#GUID-7A1BA319-767A-43CC-A579-4DAC7063B243)
* [DBTIMEZONE](https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/DBTIMEZONE.html)
* [Convert Timestamp into Milliseconds](https://stackoverflow.com/questions/64359143/convert-timestamp-into-milliseconds)
* [TO_TIMESTAMP](https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/TO_TIMESTAMP.html)
* [Oracle Built-in Data Types](https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/Data-Types.html#GUID-7B72E154-677A-4342-A1EA-C74C1EA928E6)

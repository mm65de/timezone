# timezone
Working with timezones in a distributed environment

## General considerations
Working with timezones in a distributed environment is difficult. 
If you want to store dates and time you first have to consider the nature of the dates and times that you want to store:
Some of your data should consider a timezone, others must not.

### Data without timezone information
| Type | Example |
| :---: | :--- |
| date | Your _date of birth_ does not change if you travel around. If a police office asks you for your date of birth, you are not allowed to convert the date written in your id card to the timezone the police officer is currently located in. |
| time | If your doctor tells you to take your pills _in the morning_, then this applies to _any_ place, independently of where you travel to. It is always in _in the morning_ considered in local time. |

### Data with timezone information
| Type | Example |
| :---: | :--- |
| date and time | Imagine a job is started on some machine in some country far away. In the logfile you probably want to show the time in UTC to be able to collate messages from around the world in one combined log file. In a client application you want to show this UTC timestamp in local time that the user easily sees, that the event just happend two minutes ago. |

### Problems
If you store your _date of birth_  by an instance of `java.util.Date`, you could get in trouble: A `java.util.Date` internally always stores the count of milliseconds since `1970-01-01 00:00:00 UTC`. The first problem is, that your date of birth does not specify hours, minutes and seconds, but `java.util.Date` does. The second problem is, that if you are currently located in NYC, you have to specify 7 p.m. *of the eve* of your date of birth to get midnight of the in UTC.

## Java means

## Database means
### PostgreSQL
### Oracle

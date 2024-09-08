package fib.br10.core.utility;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Log4j2
public final class DateUtil {


    private static ZoneOffset getZoneOffset() {
        String offsetStr = RequestContext.get(RequestContextEnum.TIME_ZONE);
        if (Objects.isNull(offsetStr)) {
            offsetStr = "+04:00";
        }
        return ZoneOffset.of(offsetStr);
    }

    public static OffsetDateTime getCurrentDateTime() {
        ZoneOffset offset = getZoneOffset();
        return OffsetDateTime.now(offset);
    }
    public static OffsetDateTime getOffsetDateTime(String offsetDateTime) {
        return OffsetDateTime.parse(offsetDateTime);
    }

    public static Date getCurrentDate() {
        return Date.from(getCurrentDateTime().toInstant());
    }

    public static boolean isEquals(OffsetDateTime dateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().equals(getCurrentDateTime().toInstant());
    }

    public static boolean isEquals(OffsetDateTime dateTime, OffsetDateTime currentDateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().equals(currentDateTime.toInstant());
    }

    public static boolean isAfter(OffsetDateTime dateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().isAfter(getCurrentDateTime().toInstant());
    }

    public static boolean isAfter(OffsetDateTime dateTime, OffsetDateTime currentDateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().isAfter(currentDateTime.toInstant());
    }

    public static boolean isBefore(OffsetDateTime dateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().isBefore(getCurrentDateTime().toInstant());
    }

    public static boolean isBefore(OffsetDateTime dateTime, OffsetDateTime currentDateTime) {
        return !Objects.isNull(dateTime) && dateTime.toInstant().isBefore(currentDateTime.toInstant());
    }

    public static OffsetDateTime toOffsetDateTime(Date date) {
        return date.toInstant().atOffset(getZoneOffset());
    }

    public static long getDifferenceAsLong(Date date) {
        Instant instant = date.toInstant();

        Instant now = getCurrentDateTime().toInstant();

        return ChronoUnit.SECONDS.between(now, instant);
    }
}
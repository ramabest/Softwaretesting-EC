package de.hsbremen.atm.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

@Component
public class BusinessClock {

    public static final ZoneId BUSINESS_ZONE = ZoneId.of("Europe/Berlin");
    private static final ThreadLocal<Instant> TEST_NOW = new ThreadLocal<>();

    public Instant now() {
        Instant override = TEST_NOW.get();
        return override != null ? override : Instant.now();
    }

    public LocalDate businessDate() {
        return now().atZone(ZoneOffset.UTC).toLocalDate();
    }

    public static void setTestNow(Instant instant) {
        TEST_NOW.set(instant);
    }

    public static void clearTestNow() {
        TEST_NOW.remove();
    }
}


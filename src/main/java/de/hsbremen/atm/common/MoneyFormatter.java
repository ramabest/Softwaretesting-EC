package de.hsbremen.atm.common;

import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyFormatter {

    private MoneyFormatter() {
    }

    public static String format(long cents) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return format.format(cents / 100.0);
    }
}


package com.diskora.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class FormatUtils {

    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB", "PB"};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
            "0.0",
            DecimalFormatSymbols.getInstance(Locale.US));

    private FormatUtils() {
    }

    public static String bytes(long bytes) {
        if (bytes < 1_024) {
            return bytes + " B";
        }
        double value = bytes;
        int unitIndex = 0;
        while (value >= 1_024 && unitIndex < UNITS.length - 1) {
            value /= 1_024;
            unitIndex++;
        }
        return DECIMAL_FORMAT.format(value) + " " + UNITS[unitIndex];
    }

    public static String percentage(double percentage) {
        return DECIMAL_FORMAT.format(percentage) + "%";
    }
}

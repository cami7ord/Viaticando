package com.cami7ord.viaticando;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Noninstantiable utility class
public final class Utilities {

    // Suppress default constructor for noninstantiability
    private Utilities() {
        throw new AssertionError();
    }

    public static String formatDate(String date) {
        return date;
    }

    // ------------- // FORMAT CURRENCY // ---------------------//
    private static final NumberFormat DEFAULT_FORMAT;
    private static final DecimalFormatSymbols OTHER_SYMBOLS;

    static {

        DEFAULT_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
        DEFAULT_FORMAT.setMaximumFractionDigits(0);

        OTHER_SYMBOLS = new DecimalFormatSymbols(Locale.US);
        OTHER_SYMBOLS.setDecimalSeparator(',');
        OTHER_SYMBOLS.setGroupingSeparator('.');
    }

    public static String formatPrice(double price) {
        String priceBuilder = DEFAULT_FORMAT.format(price);
        DecimalFormat df = new DecimalFormat(priceBuilder, OTHER_SYMBOLS);
        return String.valueOf(df.format(price));
    }
    // ------------- // FORMAT CURRENCY // ---------------------//

    public static String simpleServerDateFormat(String serverDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yy");

        try {
            Date d = sdf.parse(serverDate);
            return output.format(d);
        } catch (ParseException e) {
            return serverDate;
        }
    }
}
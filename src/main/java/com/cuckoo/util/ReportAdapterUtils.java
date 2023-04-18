package com.cuckoo.util;

public class ReportAdapterUtils {
    private static final ThreadLocal<ReportBooster> tl = new ThreadLocal<>();

    public static void set(ReportBooster reportBooster) {
        tl.set(reportBooster);
    }

    public static ReportBooster get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}

package com.cronista.dolarya.helpers;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ramiro E. Rinaldi on 25-Sep-15.
 */
public class Utils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}

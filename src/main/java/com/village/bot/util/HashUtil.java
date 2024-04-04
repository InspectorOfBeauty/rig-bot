package com.village.bot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class HashUtil {
    private static final BigDecimal HASHES_IN_MEGA_HASHES = BigDecimal.valueOf(1000000);

    public static BigDecimal hashToMegaHash(BigDecimal hashPerSecond) {
        return hashPerSecond.divide(HASHES_IN_MEGA_HASHES, RoundingMode.CEILING);
    }

    public static BigDecimal megaHashToHash(BigDecimal megaHashPerSecond) {
        return megaHashPerSecond.multiply(HASHES_IN_MEGA_HASHES);
    }

    private HashUtil() {
        // no-op
    }
}

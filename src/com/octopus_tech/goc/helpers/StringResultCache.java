package com.octopus_tech.goc.helpers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class StringResultCache {
    private String cachedResult = null;
    private Instant cacheExpiration;

    public String getCachedResult() {
        if (cacheExpiration != null && Instant.now().isBefore(cacheExpiration)) {
            return cachedResult;
        }
        return null;
    }

    public void cacheStringResult(String result) {
        cachedResult = result;
        cacheExpiration = Instant.now().plus(30, ChronoUnit.SECONDS);
    }
}


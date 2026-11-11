package com.skylineground.core.cache.debug;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.Builder;

@Builder
public record CacheStatsResponse(
        long hitCount,
        long missCount,
        double hitRate,
        double missRate,
        long evictionCount,
        long loadCount
) {
    public static CacheStatsResponse from(CacheStats stats) {
        return CacheStatsResponse.builder()
                .hitCount(stats.hitCount())
                .missCount(stats.missCount())
                .hitRate(stats.hitRate())
                .missRate(stats.missRate())
                .evictionCount(stats.evictionCount())
                .loadCount(stats.loadCount())
                .build();
    }
}

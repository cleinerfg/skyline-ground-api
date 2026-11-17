package com.skylineground.shared.cache.debug;

import com.skylineground.shared.cache.debug.doc.InspectCacheDoc;
import com.skylineground.shared.cache.debug.doc.ListCachesDoc;
import com.skylineground.shared.cache.debug.doc.StatsCacheDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@Tag(name = "Cache Debug",
        description = "Admin endpoints for inspecting Caffeine cache entries and statistics")
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheDebugController {

    private final CacheManager manager;

    @GetMapping
    @ListCachesDoc
    public Collection<String> listAll() {
        return manager.getCacheNames();
    }

    @GetMapping("/{name}")
    @InspectCacheDoc
    public Map<Object, Object> inspect(@PathVariable String name) {
        if (manager.getCache(name) instanceof CaffeineCache cache) {
            return cache.getNativeCache().asMap();
        }
        return Map.of();
    }

    @GetMapping("/{cacheName}/stats")
    @StatsCacheDoc
    public CacheStatsResponse stats(@PathVariable String cacheName) {
        if (manager.getCache(cacheName) instanceof CaffeineCache cache) {
            return CacheStatsResponse.from(cache.getNativeCache().stats());
        }
        return null;
    }
}

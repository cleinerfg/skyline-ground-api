package com.skylineground.core.cache.debug.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get cache statistics",
        description = "Returns hit rate, miss rate, load times, and eviction counts for the specified Caffeine cache. Development only.",
        parameters = {
                @Parameter(name = "cacheName", description = "Target cache name", required = true, example = "users")
        }
)
@ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
@ApiResponse(responseCode = "404", description = "Cache not found", content = @Content)
public @interface StatsCacheDoc {
}

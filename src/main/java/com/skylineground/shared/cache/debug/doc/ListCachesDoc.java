package com.skylineground.shared.cache.debug.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "List all active cache names",
        description = "Returns a collection of all registered cache names in the CacheManager. Admin only."
)
@ApiResponse(
        responseCode = "200",
        description = "Cache names retrieved successfully",
        content = @Content
)
public @interface ListCachesDoc {
}

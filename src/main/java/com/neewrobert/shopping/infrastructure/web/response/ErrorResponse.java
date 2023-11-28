package com.neewrobert.shopping.infrastructure.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        int status,
        Map<String, String> errors) {
}

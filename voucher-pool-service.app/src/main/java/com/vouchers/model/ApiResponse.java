package com.vouchers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ApiResponse {

    @JsonProperty
    private final Map<String, Object> details;

    @JsonProperty
    private final String status;

    @JsonProperty
    private final String message;

    @JsonCreator
    public ApiResponse(@JsonProperty("details") final Map<String, Object> details,
                       @JsonProperty("status") final String status,
                       @JsonProperty("message") final String message) {
        this.details = details;
        this.status = status;
        this.message = message;
    }

    @JsonCreator
    public ApiResponse(@JsonProperty("status") final String status,
                       @JsonProperty("message") final String message) {
        this(null, status, message);
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

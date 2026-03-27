package com.playground.ecommerce.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ApiProblem {

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private OffsetDateTime timestamp;
    private Map<String, String> errors = new LinkedHashMap<String, String>();
}

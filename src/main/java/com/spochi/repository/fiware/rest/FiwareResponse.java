package com.spochi.repository.fiware.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class FiwareResponse {
    private final String body;
    private final Map<String, List<String>> headers;
}

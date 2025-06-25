package dev.pigmeo.greenhouse.dto;

public record DhtResponse(String id, Double temperature, Double humidity,
        Double heatIndex, String timestamp) {}

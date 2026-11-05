package dev.quiqzy.accsmanager;

public record AccountProfile(
        String name,
        String uuid,
        String refreshToken,
        String authJson
) {}
package com.api.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("user:read"), ADMIN("admin:read");

    @Getter
    private final String permission;
}

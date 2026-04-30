package com.url.shortenerdemo.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;

    // --- NEW FIELDS ADDED ---
    private Long id;
    private String username;
    private String email;
}
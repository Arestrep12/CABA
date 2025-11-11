package com.example.caba.application.dto;

import java.time.Instant;
import java.util.List;

public record AuthResponse(String token, Instant expiresAt, List<String> roles) {}


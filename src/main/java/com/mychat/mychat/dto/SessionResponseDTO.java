package com.mychat.mychat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SessionResponseDTO implements Serializable {
    private UUID id;
    private String title;
    private boolean favorite;
    private Instant createdAt;
    private Instant updatedAt;
}

package com.mychat.mychat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MessageResponseDTO implements Serializable {
    private UUID id;
    private String role;
    private String content;
    private Integer tokenUsage;
    private Instant createdAt;
    private Map<String, Object> metadata;
    private List<ContextItemDTO> context;

}

package com.mychat.mychat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Builder
public class ContextItemDTO implements Serializable {
    @NotBlank
    private String sourceType;

    @NotBlank
    private String sourceUri;

    @NotBlank
    private String snippet;

    private Map<String, Object> metadata;
}

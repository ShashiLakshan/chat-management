package com.mychat.mychat.dto;

import com.mychat.mychat.entity.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class PostMessageRequestDTO implements Serializable {

    @NotNull
    private ChatMessage.Role role;

    @NotBlank
    private String content;

    private Integer tokenUsage;

    private Map<String, Object> metadata;

    private List<ContextItemDTO> context;
}

package com.mychat.mychat.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mychat.mychat.dto.ContextItemDTO;
import com.mychat.mychat.dto.MessageResponseDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import com.mychat.mychat.entity.ChatMessage;
import com.mychat.mychat.entity.ChatSession;
import com.mychat.mychat.entity.MessageContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final ObjectMapper objectMapper;

    public SessionResponseDTO toDTO(ChatSession s) {
        if (s == null) return null;
        return SessionResponseDTO.builder()
                .id(s.getId())
                .title(s.getTitle())
                .favorite(s.isFavorite())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    public MessageResponseDTO toDTO(ChatMessage m, List<MessageContext> ctxs) {
        if (m == null) return null;
        return MessageResponseDTO.builder()
                .id(m.getId())
                .role(m.getRole().name())
                .content(m.getContent())
                .tokenUsage(m.getTokenUsage())
                .createdAt(m.getCreatedAt())
                .metadata(readMap(m.getMetadataJson()))
                .context(ctxs == null
                        ? List.of()
                        : ctxs.stream().map(this::toContextDTO).collect(Collectors.toList()))
                .build();
    }

    public ContextItemDTO toContextDTO(MessageContext c) {
        if (c == null) return null;
        return ContextItemDTO.builder()
                .sourceType(c.getSourceType())
                .sourceUri(c.getSourceUri())
                .snippet(c.getSnippet())
                .metadata(readMap(c.getMetadataJson()))
                .build();
    }

    public String writeJson(Map<String, Object> map) {
        try {
            return map == null ? "{}" : objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            return "{}";
        }
    }

    public Map<String, Object> readMap(String json) {
        try {
            if (json == null || json.isBlank()) return Map.of();
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Map.of();
        }
    }

}



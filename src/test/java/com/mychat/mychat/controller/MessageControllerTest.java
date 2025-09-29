package com.mychat.mychat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mychat.mychat.dto.MessageResponseDTO;
import com.mychat.mychat.dto.PostMessageRequestDTO;
import com.mychat.mychat.entity.ChatMessage;
import com.mychat.mychat.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest {

    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(messageService))
                .build();
    }

    @Test
    void postMessage_created() throws Exception {
        // Given
        UUID sessionId = UUID.randomUUID();
        String userId = "test-user";

        Mockito.when(messageService.add(Mockito.eq(userId), Mockito.eq(sessionId), Mockito.any()))
                .thenReturn(MessageResponseDTO.builder().build());

        PostMessageRequestDTO body = PostMessageRequestDTO.builder()
                .role(ChatMessage.Role.USER)
                .content("test-content")
                .tokenUsage(10)
                .build();

        // When
        var result = mvc.perform(post("/v1/sessions/{sessionId}/messages", sessionId)
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // Then
        result.andExpect(status().isCreated());
    }

    @Test
    void postMessage_missingHeader_returns400() throws Exception {
        // Given
        UUID sessionId = UUID.randomUUID();
        PostMessageRequestDTO body = PostMessageRequestDTO.builder()
                .role(ChatMessage.Role.USER)
                .content("test-content")
                .tokenUsage(10)
                .build();

        // When
        var result = mvc.perform(post("/v1/sessions/{sessionId}/messages", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // Then
        result.andExpect(status().isBadRequest());
    }
}

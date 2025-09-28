package com.mychat.mychat.service;

import com.mychat.mychat.dto.MessageResponseDTO;
import com.mychat.mychat.dto.PostMessageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageService {
    MessageResponseDTO add(String userId, UUID sessionId, PostMessageRequestDTO request);
    Page<MessageResponseDTO> page(String userId, UUID sessionId, Pageable pageable);
    void delete(String userId, UUID sessionId, UUID messageId);
}

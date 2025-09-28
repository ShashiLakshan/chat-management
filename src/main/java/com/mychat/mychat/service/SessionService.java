package com.mychat.mychat.service;

import com.mychat.mychat.dto.CreateSessionRequestDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SessionService {

    SessionResponseDTO create(String userId, CreateSessionRequestDTO request);
    Page<SessionResponseDTO> list(String userId, Boolean favorite, String q, Pageable pageable);
    SessionResponseDTO get(String userId, UUID sessionId);
    void rename(String userId, UUID sessionId, String newTitle);
    void favorite(String userId, UUID sessionId, boolean fav);
    void delete(String userId, UUID sessionId);
}

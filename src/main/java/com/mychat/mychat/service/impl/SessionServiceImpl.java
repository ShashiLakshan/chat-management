package com.mychat.mychat.service.impl;

import com.mychat.mychat.dto.CreateSessionRequestDTO;
import com.mychat.mychat.dto.RenameSessionRequestDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import com.mychat.mychat.entity.ChatSession;
import com.mychat.mychat.exception.NotFoundException;
import com.mychat.mychat.mapper.ChatMapper;
import com.mychat.mychat.repository.ChatSessionRepository;
import com.mychat.mychat.service.SessionService;
import com.mychat.mychat.specification.ChatSessionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;

    @Override
    public SessionResponseDTO create(String userId, CreateSessionRequestDTO createSessionRequestDTO) {

        String title = Optional.ofNullable(createSessionRequestDTO.getTitle()).orElse("New Chat");

        ChatSession chatSession = new ChatSession();
        chatSession.setUserId(userId);
        chatSession.setTitle(title.trim());
        chatSession.setFavorite(Optional.ofNullable(createSessionRequestDTO.getFavourite()).orElse(false));
        chatSession.setCreatedAt(Instant.now());
        chatSession.setUpdatedAt(Instant.now());
        chatSessionRepository.save(chatSession);
        return chatMapper.toDTO(chatSession);
    }

    @Override
    public Page<SessionResponseDTO> list(String userId, Boolean favorite, String q, Pageable pageable) {
        Specification<ChatSession> spec =
                Specification.<ChatSession>unrestricted()
                        .and(ChatSessionSpecification.byUserId(userId))
                        .and(ChatSessionSpecification.notDeleted())
                        .and(ChatSessionSpecification.favorite(favorite))
                        .and(ChatSessionSpecification.titleContains(q));

        return chatSessionRepository.findAll(spec, pageable)
                .map(chatMapper::toDTO);
    }

    @Override
    public SessionResponseDTO get(String userId, UUID sessionId) {
        ChatSession session = chatSessionRepository.findByIdAndUserIdAndDeletedAtIsNull(sessionId, userId)
                .orElseThrow(() -> new NotFoundException("session"));
        return chatMapper.toDTO(session);
    }

    @Override
    public void rename(String userId, UUID sessionId, RenameSessionRequestDTO dto) {
        ChatSession session = chatSessionRepository.findByIdAndUserIdAndDeletedAtIsNull(sessionId, userId)
                .orElseThrow(() -> new NotFoundException("session"));

        String title = dto.getTitle();
        session.setTitle((title == null || title.isBlank()) ? "New Chat" : title.trim());
        session.setUpdatedAt(Instant.now());
        chatSessionRepository.save(session);
    }

    @Override
    public void favorite(String userId, UUID sessionId, boolean fav) {
        ChatSession session = chatSessionRepository.findByIdAndUserIdAndDeletedAtIsNull(sessionId, userId)
                .orElseThrow(() -> new NotFoundException("session"));
        session.setFavorite(fav);
        session.setUpdatedAt(Instant.now());
        chatSessionRepository.save(session);
    }

    @Override
    public void delete(String userId, UUID sessionId) {
        ChatSession session = chatSessionRepository.findByIdAndUserIdAndDeletedAtIsNull(sessionId, userId)
                .orElseThrow(() -> new NotFoundException("session"));
        session.setDeletedAt(Instant.now());
        chatSessionRepository.save(session);
    }
}

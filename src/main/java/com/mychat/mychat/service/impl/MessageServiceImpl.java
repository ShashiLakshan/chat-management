package com.mychat.mychat.service.impl;

import com.mychat.mychat.dto.ContextItemDTO;
import com.mychat.mychat.dto.MessageResponseDTO;
import com.mychat.mychat.dto.PostMessageRequestDTO;
import com.mychat.mychat.entity.ChatMessage;
import com.mychat.mychat.entity.ChatSession;
import com.mychat.mychat.entity.MessageContext;
import com.mychat.mychat.exception.NotFoundException;
import com.mychat.mychat.mapper.ChatMapper;
import com.mychat.mychat.repository.ChatMessageRepository;
import com.mychat.mychat.repository.ChatSessionRepository;
import com.mychat.mychat.repository.MessageContextRepository;
import com.mychat.mychat.service.MessageService;
import com.mychat.mychat.specification.ChatMessageSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final MessageContextRepository contextRepository;
    private final ChatMapper mapper;

    @Transactional
    @Override
    public MessageResponseDTO add(String userId, UUID sessionId, PostMessageRequestDTO request) {

        ChatSession session = sessionRepository.findByIdAndUserIdAndDeletedAtIsNull(sessionId, userId)
                .orElseThrow(() -> new NotFoundException("session"));
        ChatMessage msg = ChatMessage.builder()
                .session(session)
                .role(request.getRole())
                .content(request.getContent())
                .tokenUsage(request.getTokenUsage())
                .metadataJson(mapper.writeJson(request.getMetadata()))
                .createdAt(Instant.now())
                .build();

        List<ContextItemDTO> items =
                Optional.ofNullable(request.getContext()).orElse(List.of());

        List<MessageContext> toSave = items
                .stream()
                .map(dto -> MessageContext.builder()
                        .message(msg)
                        .sourceType(dto.getSourceType())
                        .sourceUri(dto.getSourceUri())
                        .snippet(dto.getSnippet())
                        .metadataJson(mapper.writeJson(dto.getMetadata()))
                        .build())
                .toList();

        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);
        List<MessageContext> msgCtxList = contextRepository.findByMessage_IdIn(List.of(msg.getId()));
        return mapper.toDTO(msg, msgCtxList);
    }

    @Override
    public Page<MessageResponseDTO> page(String userId, UUID sessionId, Pageable pageable) {

        Specification<ChatMessage> spec = ChatMessageSpecification.bySessionAndUser(sessionId, userId);
        Page<ChatMessage> page = messageRepository.findAll(spec, pageable);

        List<UUID> ids = page.getContent().stream().map(ChatMessage::getId).toList();
        Map<UUID, List<MessageContext>> byMsg = new HashMap<>();
        if (!ids.isEmpty()) {
            for (MessageContext messageContext : contextRepository.findByMessage_IdIn(ids)) {
                byMsg.computeIfAbsent(messageContext.getMessage().getId(), uuid -> new ArrayList<>()).add(messageContext);
            }
        }

        return page.map(chatMessage -> mapper.toDTO(chatMessage, byMsg.getOrDefault(chatMessage.getId(), List.of())));
    }

    @Override
    public void delete(String userId, UUID sessionId, UUID messageId) {
        Specification<ChatMessage> spec = ChatMessageSpecification.bySessionAndUser(sessionId, userId)
                .and((root, criteriaQuery, builder) -> builder.equal(root.get("id"), messageId));

        if (messageRepository.count(spec) == 0) {
            throw new NotFoundException("message");
        }
        messageRepository.deleteById(messageId);
    }
}

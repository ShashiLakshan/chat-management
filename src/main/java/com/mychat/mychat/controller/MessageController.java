package com.mychat.mychat.controller;

import com.mychat.mychat.dto.MessageResponseDTO;
import com.mychat.mychat.dto.PostMessageRequestDTO;
import com.mychat.mychat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/sessions/{sessionId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> post(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody PostMessageRequestDTO request
    ) {
        MessageResponseDTO resp = messageService.add(userId, sessionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<MessageResponseDTO>> page(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("sessionId") UUID sessionId,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<MessageResponseDTO> page = messageService.page(userId, sessionId, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("sessionId") UUID sessionId,
            @PathVariable("messageId") UUID messageId
    ) {
        messageService.delete(userId, sessionId, messageId);
        return ResponseEntity.noContent().build();
    }
}

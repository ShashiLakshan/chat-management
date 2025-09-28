package com.mychat.mychat.controller;

import com.mychat.mychat.dto.CreateSessionRequestDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import com.mychat.mychat.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponseDTO> create(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody(required = false) @Valid CreateSessionRequestDTO request) {

        SessionResponseDTO resp = sessionService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping
    public ResponseEntity<Page<SessionResponseDTO>> list(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false, defaultValue = "") String q,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SessionResponseDTO> page = sessionService.list(userId, favorite, q, pageable);
        return ResponseEntity.ok(page);
    }
}

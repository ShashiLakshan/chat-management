package com.mychat.mychat.controller;

import com.mychat.mychat.dto.CreateSessionRequestDTO;
import com.mychat.mychat.dto.FavoriteSessionRequestDTO;
import com.mychat.mychat.dto.RenameSessionRequestDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import com.mychat.mychat.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "List sessions", description = "Paginated list of active sessions for the current user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SessionResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
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

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> get(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("id") UUID sessionId) {

        return ResponseEntity.ok(sessionService.get(userId, sessionId));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<Void> rename(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("id") UUID sessionId,
            @Valid @RequestBody RenameSessionRequestDTO body) {

        sessionService.rename(userId, sessionId, body);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<Void> favorite(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("id") UUID sessionId,
            @Valid @RequestBody FavoriteSessionRequestDTO body
    ) {
        sessionService.favorite(userId, sessionId, body.getFavorite());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("id") UUID sessionId
    ) {
        sessionService.delete(userId, sessionId);
        return ResponseEntity.noContent().build();
    }

}

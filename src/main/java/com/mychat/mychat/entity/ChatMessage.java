package com.mychat.mychat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    public enum Role { USER, ASSISTANT, SYSTEM }

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "token_usage")
    private Integer tokenUsage;

    @Column(name = "metadata_jsonb", columnDefinition = "jsonb")
    private String metadataJson = "{}";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}

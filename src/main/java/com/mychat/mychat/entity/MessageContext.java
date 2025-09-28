package com.mychat.mychat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "message_context")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageContext {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    @Column(name = "source_type", nullable = false, length = 64)
    private String sourceType;

    @Column(name = "source_uri", nullable = false, columnDefinition = "text")
    private String sourceUri;

    @Column(columnDefinition = "text", nullable = false)
    private String snippet;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_jsonb", columnDefinition = "jsonb", nullable = false)
    private String metadataJson = "{}";

}

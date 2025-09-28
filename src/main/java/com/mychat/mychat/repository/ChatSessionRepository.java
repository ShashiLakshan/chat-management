package com.mychat.mychat.repository;

import com.mychat.mychat.entity.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID>, JpaSpecificationExecutor<ChatSession> {
    Page<ChatSession> findByUserIdAndDeletedAtIsNull(String userId, Pageable pageable);
    Page<ChatSession> findByUserIdAndDeletedAtIsNullAndFavoriteTrue(String userId, Pageable pageable);
    Page<ChatSession> findByUserIdAndDeletedAtIsNullAndTitleContainingIgnoreCase(String userId, String q, Pageable pageable);
    Optional<ChatSession> findByIdAndUserIdAndDeletedAtIsNull(UUID id, String userId);

}

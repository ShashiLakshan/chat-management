package com.mychat.mychat.specification;

import com.mychat.mychat.entity.ChatSession;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public final class ChatSessionSpecification {

    private ChatSessionSpecification() {}

    public static Specification<ChatSession> byUserId(String userId) {
        return (root, cq, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<ChatSession> notDeleted() {
        return (root, cq, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<ChatSession> favorite(Boolean fav) {
        if (fav == null) return null;
        return (root, cq, cb) -> cb.equal(root.get("favorite"), fav);
    }

    public static Specification<ChatSession> titleContains(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("title")), like);
    }

    public static Specification<ChatSession> byIdAndUser(UUID id, String userId) {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("id"), id),
                cb.equal(root.get("userId"), userId)
        );
    }

    public static Specification<ChatSession> updatedAfter(Instant ts) {
        if (ts == null) return null;
        return (root, cq, cb) -> cb.greaterThan(root.get("updatedAt"), ts);
    }
}

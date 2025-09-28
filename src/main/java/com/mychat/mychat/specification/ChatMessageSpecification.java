package com.mychat.mychat.specification;

import com.mychat.mychat.entity.ChatMessage;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class ChatMessageSpecification {

    private ChatMessageSpecification() {}

    public static Specification<ChatMessage> bySessionAndUser(UUID sessionId, String userId) {
        return (root, cq, cb) -> {
            var session = root.join("session");
            return cb.and(
                    cb.equal(session.get("id"), sessionId),
                    cb.equal(session.get("userId"), userId),
                    cb.isNull(session.get("deletedAt"))
            );
        };
    }
}

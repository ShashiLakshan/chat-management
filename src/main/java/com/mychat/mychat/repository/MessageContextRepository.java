package com.mychat.mychat.repository;

import com.mychat.mychat.entity.MessageContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageContextRepository extends JpaRepository<MessageContext, UUID> {
    List<MessageContext> findByMessage_IdIn(Collection<UUID> messageIds);
}
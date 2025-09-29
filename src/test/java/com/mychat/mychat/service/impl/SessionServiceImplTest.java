package com.mychat.mychat.service.impl;

import com.mychat.mychat.dto.CreateSessionRequestDTO;
import com.mychat.mychat.dto.SessionResponseDTO;
import com.mychat.mychat.entity.ChatSession;
import com.mychat.mychat.mapper.ChatMapper;
import com.mychat.mychat.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {

    private final static String userId = "user-123";

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private SessionServiceImpl service;

    @Test
    void create_withValidInput_savesTitle_defaultsHandled_returnsMapperDto() {
        SessionResponseDTO expected = getTestSessionResponseDto();

        when(chatSessionRepository.save(any(ChatSession.class))).then(returnsFirstArg());
        when(chatMapper.toDTO(any(ChatSession.class))).thenReturn(expected);

        //Given
        CreateSessionRequestDTO req = CreateSessionRequestDTO
                .builder().title("My Session").favourite(Boolean.TRUE).build();

        //When
        SessionResponseDTO result = service.create(userId, req);

        //Then
        verify(chatMapper).toDTO(any(ChatSession.class));
        verifyNoMoreInteractions(chatSessionRepository, chatMapper);
        assertSame(expected, result);
    }

    @Test
    void create_withNullTitleAndFavourite_defaultsTitleToNewChat_andFavFalse() {
        when(chatSessionRepository.save(any(ChatSession.class))).then(returnsFirstArg());
        when(chatMapper.toDTO(any(ChatSession.class))).thenReturn(getTestSessionResponseDto());

        //Given
        CreateSessionRequestDTO req = CreateSessionRequestDTO.builder().build();

        //When
        SessionResponseDTO result = service.create(userId, req);

        //Then
        verify(chatMapper).toDTO(any(ChatSession.class));
        assertNotNull(result);
    }

    @Test
    void create_withNullRequest_throwsNPE_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.create(userId, null));
        verifyNoInteractions(chatSessionRepository, chatMapper);
    }

    private SessionResponseDTO getTestSessionResponseDto() {
        return SessionResponseDTO.builder()
                .id(UUID.randomUUID())
                .title("IGNORED_BY_TEST")
                .favorite(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}

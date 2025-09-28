package com.mychat.mychat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CreateSessionRequestDTO implements Serializable {

    @NotNull
    @NotBlank
    private String title;
    private Boolean favourite;
}

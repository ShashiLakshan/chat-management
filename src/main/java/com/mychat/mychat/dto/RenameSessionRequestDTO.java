package com.mychat.mychat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RenameSessionRequestDTO implements Serializable {
    @NotNull
    private String title;
}

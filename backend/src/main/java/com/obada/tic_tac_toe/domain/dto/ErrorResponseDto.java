package com.obada.tic_tac_toe.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {

    private String message;
    private int status;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

package com.obada.tic_tac_toe.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMakeMove {


    @NotNull
    private int index;
    @NotNull
    private UUID id;
    @NotNull
    private String token;
}

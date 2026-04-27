package com.obada.tic_tac_toe.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameBoardCreateRequest {

    private Boolean isPlayerOneTurn;
}

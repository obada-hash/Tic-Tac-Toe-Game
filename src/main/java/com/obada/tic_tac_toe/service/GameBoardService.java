package com.obada.tic_tac_toe.service;

import com.obada.tic_tac_toe.domain.GameBoardCreateRequest;
import com.obada.tic_tac_toe.domain.entity.GameBoard;
import com.obada.tic_tac_toe.domain.entity.GameState;
import com.obada.tic_tac_toe.domain.entity.Player;

import java.util.UUID;

public interface GameBoardService {

    GameBoard createGameBoard(GameBoardCreateRequest request);

    GameBoard getGameBoard(UUID id);

    GameBoard makeMove(int index, UUID id, String token);

    GameBoard joinGame(UUID id);

    GameState getGameState(UUID id);


}

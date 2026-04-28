package com.obada.tic_tac_toe.service.impl;


import com.obada.tic_tac_toe.Repository.GameBoardRepo;
import com.obada.tic_tac_toe.domain.GameBoardCreateRequest;
import com.obada.tic_tac_toe.domain.entity.GameBoard;
import com.obada.tic_tac_toe.domain.entity.GameState;
import com.obada.tic_tac_toe.domain.entity.Player;
import com.obada.tic_tac_toe.mapper.GameBoardMapper;
import com.obada.tic_tac_toe.service.GameBoardService;
import com.obada.tic_tac_toe.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameBoardServiceImpl implements GameBoardService {

    private final GameBoardRepo gameBoardRepo;
    private final GameBoardMapper gameBoardMapper;
    private final PlayerService playerService;


    @Override
    public GameBoard createGameBoard(GameBoardCreateRequest request) {
        GameBoard gameBoard = gameBoardMapper.toEntity(request);
        System.out.println(gameBoard.getBoard());
        Player player1 = playerService.createPlayer("player1");
        gameBoard.setPlayer1(player1);
        return gameBoardRepo.save(gameBoard);
    }

    @Override
    public GameBoard getGameBoard(UUID id) {
        return gameBoardRepo.findById(id).orElse(null);
    }

    @Override
    public GameBoard makeMove(int index, UUID id, String token) {

        GameBoard gameBoard = getGameBoard(id);
        if (gameBoard.getGameState() != GameState.IN_PROGRESS) {
            throw new RuntimeException("game has ended");
        }
        if ((gameBoard.getIsPlayerOneTurn() && !token.equals(gameBoard.getPlayer1().getToken())) ||
                (!gameBoard.getIsPlayerOneTurn() && !token.equals(gameBoard.getPlayer2().getToken()))){
            throw new RuntimeException("invalid token or not your turn");
        }
        char[] newBoard = gameBoard.getBoard().toCharArray();
        if(newBoard[index-1] == 'X' || newBoard[index-1] == 'O') {
            throw new RuntimeException("position is already occupied");
        }
        if (gameBoard.getIsXTurn()){
            newBoard[index-1] = 'X';
        } else {
            newBoard[index-1] = 'O';
        }
        gameBoard.setBoard(String.valueOf(newBoard));
        gameBoard.setIsXTurn(!gameBoard.getIsXTurn());
        gameBoard.setIsPlayerOneTurn(!gameBoard.getIsPlayerOneTurn());
        gameBoard.setCount(gameBoard.getCount()+1);
        endGame(gameBoard);
        return gameBoardRepo.save(gameBoard);
    }

    @Override
    public GameBoard joinGame(UUID id) {
        GameBoard gameBoard = getGameBoard(id);
        if(gameBoard.getPlayer2() != null) {
            throw new RuntimeException("game is full");
        }
        Player player2 = playerService.createPlayer("player2");
        gameBoard.setPlayer2(player2);
        return gameBoardRepo.save(gameBoard);
    }

    @Override
    public GameState getGameState(UUID id) {
        GameBoard gameBoard = getGameBoard(id);
        return gameBoard.getGameState();
    }

    private char isThereWin(GameBoard gameBoard) {
        char[] b = gameBoard.getBoard().toCharArray();
        int[][] winConditions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
                {0, 4, 8}, {2, 4, 6}             // diagonals
        };

        for (int[] win : winConditions) {
            if (b[win[0]] != '-' && b[win[0]] == b[win[1]] && b[win[1]] == b[win[2]]) {
                return b[win[0]];
            }
        }
        return '-';
    }

    private Player getWinner(GameBoard gameBoard) {

        if(gameBoard.getIsPlayerOneTurn()) {
            return gameBoard.getPlayer2();
        } else {
            return gameBoard.getPlayer1();
        }
    }

    private void endGame(GameBoard gameBoard) {
        if(isThereWin(gameBoard) != '-'){

            if(getWinner(gameBoard).equals(gameBoard.getPlayer1())){
                gameBoard.setGameState(GameState.PLAYER_ONE_WINS);
            } else {
                gameBoard.setGameState(GameState.PLAYER_TWO_WINS);
            }
        } else if(gameBoard.getCount() == 9){
            gameBoard.setGameState(GameState.DRAW);
        }
    }
}

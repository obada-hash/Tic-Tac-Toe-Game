package com.obada.tic_tac_toe.controller;

import com.obada.tic_tac_toe.domain.GameMakeMove;
import com.obada.tic_tac_toe.domain.dto.GameBoardDto;
import com.obada.tic_tac_toe.domain.entity.GameBoard;
import com.obada.tic_tac_toe.mapper.GameBoardMapper;
import com.obada.tic_tac_toe.service.GameBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GamePlayController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameBoardService gameService;
    private final GameBoardMapper gameBoardMapper;

    @MessageMapping("/gameplay")
    public void playMove(@Payload GameMakeMove gameMakeMove) {

        GameBoardDto updatedBoardDto = gameBoardMapper.toDto(
                gameService.makeMove(
                        gameMakeMove.getIndex(), gameMakeMove.getId(),
                        gameMakeMove.getToken()));

        simpMessagingTemplate.convertAndSend(
                "/radio/game/" + gameMakeMove.getId(),
                updatedBoardDto
        );
    }
}

package com.obada.tic_tac_toe.controller;


import com.obada.tic_tac_toe.domain.GameBoardCreateRequest;
import com.obada.tic_tac_toe.domain.dto.GameBoardCreateRequestDto;
import com.obada.tic_tac_toe.domain.dto.GameBoardDto;
import com.obada.tic_tac_toe.domain.dto.JoinGameDto;
import com.obada.tic_tac_toe.domain.entity.GameBoard;
import com.obada.tic_tac_toe.domain.entity.GameState;
import com.obada.tic_tac_toe.mapper.GameBoardMapper;
import com.obada.tic_tac_toe.service.GameBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameBoardController {

    private final GameBoardService gameBoardService;
    private final GameBoardMapper gameBoardMapper;


    @GetMapping("/{id}")
    public ResponseEntity<GameBoardDto> getGameById(@PathVariable UUID id){
         GameBoardDto dto = gameBoardMapper.toDto(
                 gameBoardService.getGameBoard(id)
         );

         return ResponseEntity.ok(dto);
    }


    @PostMapping("/create")
    public ResponseEntity<GameBoardDto> createGame(@RequestBody GameBoardCreateRequestDto requestDto) {
        GameBoardCreateRequest request = gameBoardMapper.toGameBoardCreateRequest(requestDto);
        GameBoard gameBoard = gameBoardService.createGameBoard(request);
        return new ResponseEntity<>(gameBoardMapper.toDto(gameBoard), HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<GameBoardDto> joinGame(@RequestBody JoinGameDto requestDto
    ) {
         GameBoardDto dto = gameBoardMapper.toDto(
                 gameBoardService.joinGame(requestDto.getId())
         );
         return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameState> getGameState(@PathVariable UUID id){
        return new ResponseEntity<>(gameBoardService.getGameState(id), HttpStatus.OK);
    }


}

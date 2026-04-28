package com.obada.tic_tac_toe.mapper;

import com.obada.tic_tac_toe.domain.GameBoardCreateRequest;
import com.obada.tic_tac_toe.domain.dto.GameBoardCreateRequestDto;
import com.obada.tic_tac_toe.domain.dto.GameBoardDto;
import com.obada.tic_tac_toe.domain.entity.GameBoard;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,
            builder = @Builder(disableBuilder = true))
public interface GameBoardMapper {


    GameBoardDto toDto(GameBoard gameBoard);

    GameBoard toEntity(GameBoardCreateRequest request);

    GameBoardCreateRequest toGameBoardCreateRequest(GameBoardCreateRequestDto requestDto);
}

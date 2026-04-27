package com.obada.tic_tac_toe.domain.dto;


import com.obada.tic_tac_toe.domain.entity.GameState;
import com.obada.tic_tac_toe.domain.entity.Player;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameBoardDto {

    private UUID id;
    private Boolean isPlayerOneTurn;
    private Boolean isXTurn;
    private GameState gameState;
    private Integer count;
    private String board;
    private Instant createdAt;
    private Instant updatedAt;
    private Player player1;
    private Player player2;
}

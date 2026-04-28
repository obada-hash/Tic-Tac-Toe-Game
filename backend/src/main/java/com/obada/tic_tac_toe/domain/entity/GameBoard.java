package com.obada.tic_tac_toe.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "boards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Boolean isPlayerOneTurn = true;

    @Column(nullable = false)
    private Boolean isXTurn = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    GameState gameState = GameState.IN_PROGRESS;

    @Column(length = 9)
    private String board = "---------";

    @Column(name = "count")
    private Integer count = 0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt; // delete every unfinished game that lasts more than 1 hour

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private Player player2;

}

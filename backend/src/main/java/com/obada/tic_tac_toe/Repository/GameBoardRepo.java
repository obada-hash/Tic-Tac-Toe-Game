package com.obada.tic_tac_toe.Repository;


import com.obada.tic_tac_toe.domain.entity.GameBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Repository
public interface GameBoardRepo extends JpaRepository<GameBoard, UUID> {


}

package com.obada.tic_tac_toe.service;

import com.obada.tic_tac_toe.domain.entity.Player;

public interface PlayerService {

    Player findById(int id);

    Player createPlayer(String name);
}

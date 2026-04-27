package com.obada.tic_tac_toe.service.impl;


import com.obada.tic_tac_toe.Repository.PlayerRepo;
import com.obada.tic_tac_toe.domain.entity.Player;
import com.obada.tic_tac_toe.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepo playerRepo;


    @Override
    public Player findById(int id) {
        return playerRepo.findById(id).orElse(null);
    }

    @Override
    public Player createPlayer(String name) {
        String token = UUID.randomUUID().toString();
        Player player = Player.builder()
                .name(name)
                .token(token)
                .build();
        return playerRepo.save(player);
    }
}

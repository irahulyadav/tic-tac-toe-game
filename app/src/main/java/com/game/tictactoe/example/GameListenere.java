package com.game.tictactoe.example;

import android.widget.ImageView;

import com.game.tictactoe.example.module.Player;

/**
 * Created by rahul on 06/06/16.
 */

public interface GameListenere {

    public boolean onGameStart();

    public boolean onMoveTaken(ImageView view, Player player);

    public void onMoveAlreadyTaken(Integer move);

    public boolean nextMove(Player player);

    public void onResultShow(Player winner, Integer[] slot);

    public boolean onGameStop();
}

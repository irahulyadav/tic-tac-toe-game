package com.game.tictactoe.example.module;

import android.widget.ImageView;

import com.game.tictactoe.example.GameListenere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by rahul on 06/06/16.
 */

public class Game extends Thread {
    private static final Integer[][] WINNING_SLOTES = {
            {1, 2, 3},
            {1, 4, 7},
            {1, 5, 9},
            {2, 5, 8},
            {3, 6, 9},
            {3, 5, 7},
            {4, 5, 6},
            {7, 8, 9},
    };
    private static Game game;
    private int timer;
    private int playerTimer;
    private GameListenere gameListenere;
    private boolean running;
    private GameType type;
    private GameLevel level;
    private Player player0;
    private Player playerX;
    private int gameCount;

    private Map<Integer, Player> moves = new HashMap<>();
    private List<Integer> movesLeft = new ArrayList<>();

    private boolean xTurn;

    private Game(GameType type) {
        setType(type);
    }

    public static Game newGame(GameType gameType) {
        game = null;
        return game = new Game(gameType);
    }

    public static Game getGame() {
        return game;
    }

    public void setGameListenere(GameListenere gameListenere) {
        this.gameListenere = gameListenere;
    }

    private void setPlayers() {
        Player.Type sType = getSecondPlayerType();
        if (player0 == null) {
            player0 = new Player(0, "Your", Player.Type.PERSON);
        }
        if (playerX == null) {
            playerX = new Player(1, "X", sType);
        } else if (sType != playerX.getType()) {
            playerX.setType(sType);
        }

        if (getType() == GameType.COMPUTER) {
            if (playerX.getType() != Player.Type.COMPUTER) {
                playerX.setType(Player.Type.COMPUTER);
            }
        } else if (playerX.getType() != Player.Type.COMPUTER) {

        }
    }

    private Player.Type getSecondPlayerType() {
        return getType() == GameType.COMPUTER ? Player.Type.COMPUTER : Player.Type.PERSON;

    }

    public Integer[] isWin(Player player) {
        if (player.reachMinForResult()) {
            for (Integer[] winSlot : WINNING_SLOTES) {
                if (player.isMatchFound(winSlot)) {
                    return winSlot;
                }
            }
        }
        return null;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
        setPlayers();
    }

    public GameLevel getLevel() {
        return level;
    }

    public void setLevel(GameLevel level) {
        this.level = level;
    }

    public Player getPlayer0() {
        return player0;
    }

    public boolean isMultiLevelGame() {
        return getType() == GameType.COMPUTER;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public boolean makeMove(ImageView button, Player player, Integer move) {
        if (moves.containsKey(move)) {
            return false;
        }
        if (player.addMoves(move)) {
            if (gameListenere != null) {
                moves.put(move, player);
                movesLeft.remove(move);
                gameListenere.onMoveTaken(button, player);

                if (player.reachMinForResult()) {
                    Integer[] slot = isWin(player);
                    if (slot != null) {
                        if (gameListenere != null) {
                            gameListenere.onResultShow(player, slot);
                            stopGame();
                            return true;
                        }
                    }
                }
                if (movesLeft.size() <= 0) {
                    if (gameListenere != null) {
                        gameListenere.onResultShow(null, null);
                        return true;
                    }
                }
                xTurn = !xTurn;
                gameListenere.nextMove(getPlayerForNextMove());
            }
        }


        return true;
    }


    public Player getPlayerForNextMove() {
        return !xTurn ? player0 : playerX;
    }


    public void startGame() {
        moves.clear();
        playerX.getMoves().clear();
        player0.getMoves().clear();
        movesLeft.clear();

        running = true;
        for (int i = 1; i <= 9; i++) {
            movesLeft.add(i);
        }
        if (gameListenere != null) {
            gameListenere.onGameStart();
            gameListenere.nextMove(getPlayerForNextMove());
        }

        gameCount++;
    }

    public void stopGame() {
        movesLeft.clear();
        running = false;
        if (gameListenere != null) {
            gameListenere.onGameStop();
        }
    }

    public int generateMove(Player player) {
        int level = 0;
        if (getLevel() == GameLevel.MEDIUM) {
            level++;
        }
        if (getLevel() == GameLevel.HARD) {
            level++;
        }
        return generateMove(level, player);
    }

    public boolean isLastMove() {
        return movesLeft.size() == 1;
    }

    public boolean isRunning() {
        return running;
    }

    private Integer generateRandom() {
        Integer nextMove = isLastMove() ? movesLeft.get(0) :
                movesLeft.get(new Random().nextInt(movesLeft.size() - 1));
        if (moves.containsKey(nextMove)) {
            return generateRandom();
        }
        return nextMove;
    }

    private Integer generateMove(int level, Player player) {
        Integer nextMove = hasWiningMove(player);
        if (nextMove == -1) {
            if (level <= 0) {
                return generateRandom();
            }
            nextMove = hasWiningMove(player0);
        }
        if (nextMove == -1) {
            if (level <= 1) {
                return generateRandom();
            }
            nextMove = checkMultipleWiningSlot(player0);
            if (nextMove == -1) {
                nextMove = checkMultipleWiningSlot(player);
            }
        }

        return nextMove <= 0 ? generateRandom() : nextMove;
    }

    private Integer checkMultipleWiningSlot(Player player) {
        if (player.getMoves().size() <= 0) {
            return -1;
        }
        Iterator<Integer> iterator = movesLeft.iterator();
        while (iterator.hasNext()) {
            Integer winingMove = iterator.next();
            Integer notForMultlySlot = winingMove;
            for (Integer[] slot : WINNING_SLOTES) {
                int i = 0;
                if (isTwoSlotEmptyForPlayer(player, slot, winingMove)) {
                    i++;
                    notForMultlySlot = winingMove;
                }
                if (i >= 2) {
                    return winingMove;
                }
            }
            return notForMultlySlot;
        }

        return -1;
    }

    private boolean isTwoSlotEmptyForPlayer(Player player, Integer[] slot, Integer no) {
        int empty = 0;
        int i = 0;
        for (Integer sl : slot) {
            if (no.equals(sl)) {
                i++;
            }
            if (movesLeft.contains(sl)) {
                i++;
            } else if (!player.getMoves().contains(sl)) {
                i--;
            }
        }
        return i == 3;
    }

    @Override
    public void run() {
        super.run();
    }


    private Integer hasWiningMove(Player player) {
        if (player.getMoves().size() > 1) {
            Iterator<Integer> iterator = movesLeft.iterator();
            Set<Integer> playerMoves = new HashSet<>();
            playerMoves.addAll(player.getMoves());
            while (iterator.hasNext()) {
                Integer winingMove = iterator.next();
                playerMoves.add(winingMove);
                for (Integer[] slot : WINNING_SLOTES) {
                    if (playerMoves.contains(slot[0]) && playerMoves.contains(slot[1]) && playerMoves.contains(slot[2])) {
                        return winingMove;
                    }
                }
                playerMoves.remove(winingMove);
            }
        }
        return -1;
    }
}

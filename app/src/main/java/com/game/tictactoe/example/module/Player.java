package com.game.tictactoe.example.module;

import com.game.tictactoe.example.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rahul on 06/06/16.
 */

public class Player {
    private int id;
    private String name;
    private long winningTime;
    private Type type;
    private Set<Integer> moves = new HashSet<>();

    public Player(int id, String name, Type type) {
        this.id = id;
        this.name = name;
        setType(type);
    }

    public boolean addMoves(int move) {
        if (hasMoreMoves()) {
            moves.add(move);
            return true;
        }
        return false;
    }


    public boolean isLastMoves() {
        return !hasMoreMoves();
    }

    public boolean hasMoreMoves() {
        return !reachMax();
    }

    public boolean reachMax() {
        return moves.size() >= 5;
    }

    public boolean reachMinForResult() {
        return moves.size() >= 3;
    }


    public Set<Integer> getMoves() {
        return moves;
    }

    public Integer[] getMovesInArray() {
        return moves.toArray(new Integer[moves.size()]);
    }

    public boolean isMoveAlreadyTaken(int move) {
        return moves.contains(move);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        if (type == Type.COMPUTER) {
            name = "Computer";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isMatchFound(Integer[] slot) {
        if (slot == null || slot.length < 3) {
            return false;
        }
        return moves.contains(slot[0]) && moves.contains(slot[1]) && moves.contains(slot[2]);
    }

    public long getWinningTime() {
        return winningTime;
    }

    public void markWin() {
        winningTime++;
    }

    public int getIcon() {
        return id == 0 ? R.mipmap.button_0 : R.mipmap.button_x;
    }

    public int getWinningIcon() {
        return id == 0 ? R.mipmap.green_0 : R.mipmap.green_x;
    }

    public enum Type {
        PERSON, COMPUTER
    }
}

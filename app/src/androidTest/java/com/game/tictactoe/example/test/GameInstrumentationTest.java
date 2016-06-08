/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.game.tictactoe.example.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.CheckBox;

import com.game.tictactoe.example.GameActivity;
import com.game.tictactoe.example.R;
import com.game.tictactoe.example.module.Game;
import com.game.tictactoe.example.module.GameLevel;
import com.game.tictactoe.example.module.GameType;
import com.game.tictactoe.example.module.Player;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameInstrumentationTest {
    @Rule
    public ActivityTestRule<GameActivity> mActivityRule = new ActivityTestRule<>(
            GameActivity.class);
    private int[] modeId = new int[]{R.id.normalMode, R.id.mediumMode, R.id.hardMode};

    @Test
    public void checkGameLevel() {
        for (int id : modeId) {
            changeGameLevel(id);
        }
    }


    @Test
    public void playWithPlayer() {
        setTextMode();
        Game game = Game.getGame();
        game.setType(GameType.TWO_PLAYER);
        game.getPlayer0().setName("Player 1");
        game.getPlayerX().setName("Player 2");
        while (game.isRunning()) {
            Player player = game.getPlayerForNextMove();
            placeMove(mActivityRule.getActivity().SLOTES[game.generateMove(player) - 1]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (game.getLevel() == GameLevel.NORMAL) {
            startGame(GameLevel.MEDIUM);
            playWithPlayer();
        } else if (game.getLevel() == GameLevel.MEDIUM) {
            startGame(GameLevel.HARD);
            playWithPlayer();
        }
    }

    @Test
    public void playWithComputer() {
        setTextMode();
        Game game = Game.getGame();
        game.setType(GameType.COMPUTER);
        game.getPlayer0().setName("Player 1");
        while (game.isRunning()) {
            Player player = game.getPlayerForNextMove();
            placeMove(mActivityRule.getActivity().SLOTES[game.generateMove(player) - 1]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (game.getLevel() == GameLevel.NORMAL) {
            startGame(GameLevel.MEDIUM);
            playWithComputer();
        } else if (game.getLevel() == GameLevel.MEDIUM) {
            startGame(GameLevel.HARD);
            playWithComputer();
        }
    }


    private void changeGameLevel(int id) {
        onView(withId(id)).perform(click());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void startGame(GameLevel level) {
        Game game = Game.getGame();
        int id = R.id.normalMode;
        if (level == GameLevel.MEDIUM) {
            id = R.id.mediumMode;
        } else if (level == GameLevel.HARD) {
            id = R.id.hardMode;
        }
        changeGameLevel(id);
        if (game == null || !game.isRunning()) {
            onView(withId(R.id.btnStartGame)).perform(click());
        }
    }


    private void setTextMode() {
        (mActivityRule.getActivity()).setTestMode(true);
        if (((CheckBox) mActivityRule.getActivity().findViewById(R.id.cbAutoStart)).isChecked()) {
            onView(withId(R.id.cbAutoStart)).perform(click());
        }
    }

    private void placeMove(int slotId) {
        onView(withId(slotId)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

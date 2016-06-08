package com.game.tictactoe.example.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageView;

import com.game.tictactoe.example.GameActivity;
import com.game.tictactoe.example.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by iprahul on 6/8/16.
 */

@RunWith(AndroidJUnit4.class)
public class GameActivityEspressoTest extends ActivityInstrumentationTestCase2<GameActivity> {


    private GameActivity gameActivity;

    public GameActivityEspressoTest() {
        super(GameActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        gameActivity = getActivity();
    }

    @Test
    public void testStartStopButton() {
        Button button = (Button) gameActivity.findViewById(R.id.btnStartGame);
        assertNotNull(button);
    }

    @Test
    public void testSlotsView() {
        for (int id : GameActivity.SLOTES) {
            ImageView imageView = (ImageView) gameActivity.findViewById(id);
            assertNotNull(imageView);
        }
    }

    @Test
    public void testSlotsLevelGroup() {
        assertNotNull(gameActivity.findViewById(R.id.groupLevel));
        assertNotNull(gameActivity.findViewById(R.id.normalMode));
        assertNotNull(gameActivity.findViewById(R.id.mediumMode));
        assertNotNull(gameActivity.findViewById(R.id.hardMode));
    }

    @Test
    public void testAutoStartCheckBox() {
        assertNotNull(gameActivity.findViewById(R.id.cbAutoStart));
    }


    @Test
    public void testTextView() {
        assertNotNull(gameActivity.findViewById(R.id.score1));
        assertNotNull(gameActivity.findViewById(R.id.score2));
        assertNotNull(gameActivity.findViewById(R.id.tvPlayerStatus));
        assertNotNull(gameActivity.findViewById(R.id.tvStatus));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

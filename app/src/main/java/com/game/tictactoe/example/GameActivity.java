package com.game.tictactoe.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.game.tictactoe.example.module.Game;
import com.game.tictactoe.example.module.GameLevel;
import com.game.tictactoe.example.module.GameType;
import com.game.tictactoe.example.module.Player;


public class GameActivity extends Activity implements View.OnClickListener, GameListenere, RadioGroup.OnCheckedChangeListener {
    private Game game;
    private Player currentPlayer;
    private RadioGroup radioGroup;
    private TextView score1, score2, status, playerStaus;
    private Button startButton;

    private int[] views = {R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        radioGroup = (RadioGroup) findViewById(R.id.groupLevel);
        radioGroup.setOnCheckedChangeListener(this);
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        status = (TextView) findViewById(R.id.tvStatus);
        playerStaus = (TextView) findViewById(R.id.tvPlayerStatus);
        startButton = (Button) findViewById(R.id.btnStartGame);
        startButton.setOnClickListener(this);

        game = Game.newGame(GameType.COMPUTER);
        game.setLevel(GameLevel.NORMAL);
        game.setGameListenere(this);

        status.setText(" Level : ".concat(game.getLevel().name()));

        startButton.performClick();
    }

    public void makeMove(View v) {
        if (game == null || !game.isRunning()) {
            toast("please start game before make any moves!");
            return;
        }
        Integer move = Integer.valueOf((String) v.getTag());
        game.makeMove((ImageView) v, currentPlayer, move);
    }


    @Override
    public boolean onGameStart() {
        score1.setText(game.getPlayer0().getName().concat(" : ") + game.getPlayer0().getWinningTime());
        score2.setText(game.getPlayerX().getName().concat(" : ") + game.getPlayerX().getWinningTime());
        for (int id : views) {
            ImageView imageView = (ImageView) findViewById(id);
            imageView.setEnabled(true);
            imageView.setImageBitmap(null);
        }
        startButton.setText(R.string.stop_game);
        toast("Game start");
        return false;
    }

    @Override
    public boolean onMoveTaken(ImageView view, Player player) {
        view.setImageResource(player.getIcon());
        view.setEnabled(false);
        return false;
    }

    @Override
    public void onMoveAlreadyTaken(Integer move) {
        moveAlreadyTakenDialog(move);
    }

    @Override
    public boolean nextMove(Player player) {
        currentPlayer = player;
        playerStaus.setText(player.getName().concat(" Move"));
        if (currentPlayer.getType() == Player.Type.COMPUTER || game.isLastMove()) {
            findViewById(views[game.generateMove(player) - 1]).performClick();
        }
        return false;
    }

    @Override
    public void onResultShow(Player winner) {
        if (winner != null) {
            winner.markWin();
            winningDialog(winner);
        } else {
            drawDialog();
        }
    }

    @Override
    public boolean onGameStop() {
        startButton.setText(R.string.start_game);
        score1.setText(game.getPlayer0().getName().concat(" : ") + game.getPlayer0().getWinningTime());
        score2.setText(game.getPlayerX().getName().concat(" : ") + game.getPlayerX().getWinningTime());
        toast("game stopped!");
        playerStaus.setText("Game Stopped");
        return false;
    }

    @Override
    public void onClick(View v) {
        if (game.isRunning()) {
            game.stopGame();
        } else {
            game.startGame();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.normalMode) {
            game.setLevel(GameLevel.NORMAL);
        } else if (checkedId == R.id.midiumMode) {
            game.setLevel(GameLevel.MEDIUM);
        } else if (checkedId == R.id.hardMode) {
            game.setLevel(GameLevel.HARD);
        }
        status.setText(" Level : ".concat(game.getLevel().name()));
    }

    public void drawDialog() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Draw!");
        dlgAlert.setTitle("Draw");
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        autoNewGameStart();
                    }
                });
        dlgAlert.create().show();
    }

    public void moveAlreadyTakenDialog(Integer move) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Move Already Taken!");
        dlgAlert.setTitle(move + " Already taken");
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok", null);
        dlgAlert.create().show();
    }

    public void winningDialog(Player player) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(player.getName().concat(" wins!"));
        dlgAlert.setTitle("congratulations");
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        autoNewGameStart();
                    }
                });
        dlgAlert.create().show();
    }

    private void autoNewGameStart() {
        if (((CheckBox) findViewById(R.id.cbAutoStart)).isChecked()) {
            game.startGame();
        }
    }

    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }
}

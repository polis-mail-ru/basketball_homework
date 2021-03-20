package ru.ok.technopolis.basketball;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MultiGameActivity extends GameActivity {
    private TextView playerText;

    private int countBallsPlayer1;
    private int countBallsPlayer2;
    private int goodCountBallsPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerText = findViewById(R.id.activity_game__player);
        countBallsPlayer1 = countBallsPlayer2 = goodCountBallsPlayer2 = 0;
        playerText.setText(R.string.player1);
    }

    @Override
    protected void changeScore(boolean increase) {
        if (countBalls % 2 == 1) {
            countBallsPlayer1++;
            if (increase) {
                goodCountBallsPlayer1++;
            }

            int progress = (int) ((double) goodCountBallsPlayer1 / countBallsPlayer1 * 100);
            playerText.setText(R.string.player2);
            textViewScore.setText(getString(R.string.score, goodCountBallsPlayer2, countBallsPlayer2));
            rateView.setColor(Color.RED);
            rateView.setProgress(progress);
        } else {
            countBallsPlayer2++;
            if (increase) {
                goodCountBallsPlayer2++;
            }

            int progress = (int) ((double) goodCountBallsPlayer2 / countBallsPlayer2 * 100);
            playerText.setText(R.string.player1);
            textViewScore.setText(getString(R.string.score, goodCountBallsPlayer1, countBallsPlayer1));
            rateView.setColor(Color.YELLOW);
            rateView.setProgress(progress);
        }

        if (countBalls == maxCount * 2) {
            Intent data = new Intent();
            if (goodCountBallsPlayer1 > goodCountBallsPlayer2) {
                data.putExtra(SCORE_KEY, R.string.player1_win);
            } else if (goodCountBallsPlayer1 < goodCountBallsPlayer2) {
                data.putExtra(SCORE_KEY, R.string.player2_win);
            } else {
                data.putExtra(SCORE_KEY, R.string.draw);
            }
            setResult(RESULT_MESSAGE, data);
            finish();
        }
    }
}

package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MultiGameActivity extends GameActivity {
    private TextView playerText;

    private int count1;
    private int count2;
    private int goodCount2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerText = findViewById(R.id.activity_game__player);
        count1 = count2 = goodCount2 = 0;
        playerText.setText(R.string.player1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void changeScore(boolean increase) {
        if (count % 2 == 1) {
            count1++;
            if (increase) {
                goodCount++;
            }

            int progress = (int) ((double) goodCount / count1 * 100);
            playerText.setText(R.string.player2);
            textViewScore.setText(goodCount2 + "/" + count2);
            rateView.setColor(Color.RED);
            rateView.setProgress(progress);
        } else {
            count2++;
            if (increase) {
                goodCount2++;
            }

            int progress = (int) ((double) goodCount2 / count2 * 100);
            playerText.setText(R.string.player1);
            textViewScore.setText(goodCount + "/" + count1);
            rateView.setColor(Color.YELLOW);
            rateView.setProgress(progress);
        }

        if (count == maxCount * 2) {
            Intent data = new Intent();
            if (goodCount > goodCount2) {
                data.putExtra(SCORE_KEY, "Player 1 WIN");
            } else if (goodCount < goodCount2) {
                data.putExtra(SCORE_KEY, "Player 2 WIN");
            } else {
                data.putExtra(SCORE_KEY, "Nobody won");
            }
            setResult(RESULT_MESSAGE, data);
            finish();
        }
    }
}

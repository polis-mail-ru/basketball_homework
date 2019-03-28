package ru.ok.technopolis.basketball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    MediaPlayer mPlayer;
    ImageView panel;

    ImageView logo;
    Button playButton;
    Button optionsButton;
    Button paintButton;

    Button backFromOpt;
    Switch musicView;
    Switch wallsView;
    Switch vibroView;

    Button backFromCus;
    Button left;
    Button right;
    ImageView ballView;

    boolean music;
    boolean walls;
    boolean vibro;

    int chosenBall = 0;
    final int BALL_MAX = 1;
    int[] balls;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        getSupportActionBar().hide();
        initUI();
        initValues();
        initListeners();
    }

    private void initValues() {
        sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        music = sp.getBoolean("music", true);
        musicView.setChecked(music);
        vibro = sp.getBoolean("vibro", false);
        vibroView.setChecked(vibro);
        walls = sp.getBoolean("walls", true);
        wallsView.setChecked(walls);

        balls = new int[BALL_MAX + 1];
        balls[0] = R.drawable.ball2;
        balls[1] = R.drawable.ball;
        chosenBall = sp.getInt("ball", 0);
        Log.d(TAG, "initValues: " + chosenBall);
        ballView.setImageResource(balls[chosenBall]);
    }

    private void initListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGame();
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuInterface(View.GONE);
                setOptionInterface(View.VISIBLE);
            }
        });
        paintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPaintInterface(View.VISIBLE);
                setMenuInterface(View.GONE);
            }
        });
        backFromOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOptionInterface(View.GONE);
                setMenuInterface(View.VISIBLE);
            }
        });
        backFromCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPaintInterface(View.GONE);
                setMenuInterface(View.VISIBLE);
            }
        });
        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music = musicView.isChecked();
                sp.edit().putBoolean("music", music).apply();
                mPlayer.setVolume(music ? 0.2f : 0, music ? 0.2f : 0);
            }
        });
        vibroView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibro = vibroView.isChecked();
                sp.edit().putBoolean("vibro", vibro).apply();
            }
        });
        wallsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walls = wallsView.isChecked();
                sp.edit().putBoolean("walls", walls).apply();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenBall++;
                if (chosenBall > BALL_MAX)
                    chosenBall = 0;
                updateBall();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenBall--;
                if (chosenBall < 0)
                    chosenBall = BALL_MAX;
                updateBall();
            }
        });

    }

    private void updateBall() {
        ballView.setImageResource(balls[chosenBall]);
        sp.edit().putInt("ball", chosenBall).apply();
    }

    private void loadGame() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("ballView", 0);
        intent.putExtra("music", music);
        intent.putExtra("walls", walls);
        intent.putExtra("vibro", vibro);
        intent.putExtra("ball", balls[chosenBall]);
        MenuActivity.this.startActivity(intent);
    }

    private void initUI() {
        panel = findViewById(R.id.backPanel);

        logo = findViewById(R.id.logo);
        playButton = findViewById(R.id.playButton);
        paintButton = findViewById(R.id.paintButton);
        optionsButton = findViewById(R.id.optionsButton);

        musicView = findViewById(R.id.musicButton);
        wallsView = findViewById(R.id.ball_wall);
        vibroView = findViewById(R.id.vibrate);
        backFromOpt = findViewById(R.id.backFromOpt);

        backFromCus = findViewById(R.id.backFromCus);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        ballView = findViewById(R.id.choose_ball);
    }

    private void setMenuInterface(int mode) {
        playButton.setVisibility(mode);
        optionsButton.setVisibility(mode);
        paintButton.setVisibility(mode);
        logo.setVisibility(mode);
    }

    private void setOptionInterface(int mode) {
        musicView.setVisibility(mode);
        wallsView.setVisibility(mode);
        vibroView.setVisibility(mode);
        panel.setVisibility(mode);
        backFromOpt.setVisibility(mode);
    }

    private void setPaintInterface(int mode) {
        left.setVisibility(mode);
        right.setVisibility(mode);
        ballView.setVisibility(mode);
        backFromCus.setVisibility(mode);
        panel.setVisibility(mode);
    }

    private void initPlayer() {
        mPlayer = MediaPlayer.create(this, R.raw.menusong);
        mPlayer.setVolume(music ? 0.2f : 0, music ? 0.2f : 0);
        mPlayer.start();
        mPlayer.setLooping(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPlayer();
    }
}

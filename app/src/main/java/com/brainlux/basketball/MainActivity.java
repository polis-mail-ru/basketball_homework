package com.brainlux.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Stack;

import com.brainlux.basketball.controllers.BallController;
import com.brainlux.basketball.controllers.MusicController;
import com.brainlux.basketball.controllers.ScoreController;
import com.brainlux.basketball.objects.Ball;
import com.brainlux.basketball.objects.Basket;
import com.brainlux.basketball.objects.Coin;
import com.brainlux.basketball.objects.Game;
import com.brainlux.basketball.objects.Record;
import com.brainlux.basketball.objects.Wall;

public class MainActivity extends AppCompatActivity {

    private BackView backView;
    public static final String WALL_KEY = "walls";
    public static final String VIBRO_KEY = "vibro";
    public static final String MUSIC_KEY = "music";
    public static final String BALL_KEY = "ballView";
    private Ball ball;
    private Basket basket;
    private RelativeLayout layout;
    private ImageView ballTemplate;
    private ImageView exitView;
    private ImageView exitPanel;
    private TextView exitText;
    private Button exitYes;
    private Button exitNo;
    private ImageView radio;
    private ImageView song;
    private boolean radioMode;
    private AnimationDrawable radioAnim;
    private Coin coin;
    private TextView coinText;
    private Stack<Wall> wallsAlive;
    private Stack<Wall> wallsDead;
    private Record record;
    private boolean walls;
    private BallController ballController;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        backView = findViewById(R.id.back);
        basket = new Basket(findViewById(R.id.empty));
        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        layout = findViewById(R.id.main_layout);
        initControllers();
        getExtra();
        initExitButton();
        initRadioButton();
        initCoin();
        initWalls();
        initRecord();
        startGame();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void initControllers() {
        Game.setMusicController(new MusicController());
        Game.setScoreController(new ScoreController((StarView) findViewById(R.id.scoreView)));
    }

    private void initRecord() {
        record = new Record((TextView)findViewById(R.id.balls),
                (TextView)findViewById(R.id.money),
                (TextView)findViewById(R.id.high));
    }

    private void initWalls() {
        wallsAlive = new Stack<>();
        wallsDead = new Stack<>();
        ImageView wall = findViewById(R.id.wallPlace1);
        wallsDead.push(new Wall(wall, wallsDead, wallsAlive));
        wall.setVisibility(View.INVISIBLE);
        wall = findViewById(R.id.wallPlace2);
        wallsDead.push(new Wall(wall, wallsDead, wallsAlive));
        wall.setVisibility(View.INVISIBLE);
        wall = findViewById(R.id.wallPlace3);
        wallsDead.push(new Wall(wall, wallsDead, wallsAlive));
        wall.setVisibility(View.INVISIBLE);
        wall = findViewById(R.id.wallPlace4);
        wallsDead.push(new Wall(wall, wallsDead, wallsAlive));
        wall.setVisibility(View.INVISIBLE);
    }

    private void initCoin() {
        coin = new Coin(findViewById(R.id.coin));
        coinText = findViewById(R.id.money);
        coin.hide();
    }

    private void initRadioButton() {
        radio = findViewById(R.id.radio);
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRadio();
            }
        });
        song = findViewById(R.id.song);
        radioAnim = (AnimationDrawable) song.getBackground();
        if (radioMode) {
            radioAnim.start();
        } else {
            song.setVisibility(View.INVISIBLE);
        }
    }

    private void switchRadio() {
        if (!radioMode) {
            radioAnim.start();
            song.setVisibility(View.VISIBLE);

        } else {
            radioAnim.stop();
            song.setVisibility(View.INVISIBLE);
        }
        radioMode = !radioMode;
    }

    private void initExitButton() {
        exitView = findViewById(R.id.exit);
        exitPanel = findViewById(R.id.exitView);
        exitYes = findViewById(R.id.yesButton);
        exitNo = findViewById(R.id.noButton);
        exitText = findViewById(R.id.exitText);
        switchExit(View.GONE);
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchExit(View.VISIBLE);
                exitPanel.bringToFront();
                exitNo.bringToFront();
                exitText.bringToFront();
                exitYes.bringToFront();
            }
        });
        exitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchExit(View.GONE);
            }
        });
    }

    private void switchExit(int visibility) {
        exitView.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        exitPanel.setVisibility(visibility);
        exitYes.setVisibility(visibility);
        exitNo.setVisibility(visibility);
        exitText.setVisibility(visibility);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            resetTemplate();
        }
    }

    private void startGame() {
        Game.setBackView(backView);
        Game.setGameMode(Game.Mode.DEFAULT);
        Game.setBallsCount(16);
        record.updateBallCount();
    }

    private void getExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            walls = extras.getBoolean(WALL_KEY, true);
            if(extras.getBoolean(VIBRO_KEY, false))
                Game.setVibrator((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
            if(extras.getBoolean(MUSIC_KEY, true))
                Game.getMusicController().setMode(true);
            ((ImageView)findViewById(R.id.iv_translate_fling)).setImageResource(extras.getInt(BALL_KEY, R.drawable.ball2));
        }
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Game.getGameMode() == Game.Mode.UNLIMITED || ball == null || (Game.getGameMode() == Game.Mode.DEFAULT && ball.isNotThrown())) {
                if (Game.getGameMode() == Game.Mode.DEFAULT && ball != null && ball.isNotThrown()) {
                    layout.removeView(ball.getObject());
                }
                Game.throwIncrease();
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                ball = new Ball(dX > 0 ? Ball.Direction.RIGHT : Ball.Direction.LEFT, ballTemplate);
                resetTemplate();
                ball.throwBall(dY / 2, dX / 2);
                ballController = new BallController(ball, ballTemplate, coin);
                ballController.setWalls(wallsAlive, wallsDead);
                ballController.setRecord(record);
                ballController.throwBall(MainActivity.this, dY, basket, layout);
            }
            return false;
        }

    };

    private void resetTemplate() {
        ballTemplate = new ImageView(MainActivity.this);
        ImageView ballView = findViewById(R.id.iv_translate_fling);
       // ballView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ballView.getWidth(), ballView.getHeight());
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.back);
        params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.back);
        params.setMargins(0, 0, (int) Game.getScoreController().getView().fromDpToPx(30), (int) Game.getScoreController().getView().fromDpToPx(125));
        ballTemplate.setLayoutParams(params);
        ballTemplate.setImageDrawable(ballView.getDrawable());
        if (ball == null || Game.getGameMode() == Game.Mode.UNLIMITED) {
            ballTemplate.setVisibility(View.VISIBLE);
        } else {
            ballTemplate.setVisibility(View.INVISIBLE);
        }
        layout.addView(ballTemplate);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Game.updateHighScore();
        if (ballController != null && ballController.getAnimator() != null) {
            ballController.getAnimator().cancel();
        }
        if (Game.hasVibrator()) {
            Game.getVibrator().cancel();
        }
        if (Game.getMusicController().canPlay()) {
            Game.getMusicController().release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Game.updateHighScore();
    }
}
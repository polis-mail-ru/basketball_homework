package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Stack;

import ru.ok.technopolis.basketball.controllers.BallController;
import ru.ok.technopolis.basketball.controllers.MusicController;
import ru.ok.technopolis.basketball.controllers.ScoreController;
import ru.ok.technopolis.basketball.objects.Ball;
import ru.ok.technopolis.basketball.objects.Basket;
import ru.ok.technopolis.basketball.objects.Coin;
import ru.ok.technopolis.basketball.objects.Game;
import ru.ok.technopolis.basketball.objects.Wall;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        backView = findViewById(R.id.back);
        // getExtra();
        basket = new Basket(findViewById(R.id.empty));
        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        layout = findViewById(R.id.main_layout);
        initExitButton();
        initRadioButton();
        initCoin();
        initWalls();
        startGame();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
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
        Game.setMusicController(new MusicController());
        Game.setScoreController(new ScoreController((StarView) findViewById(R.id.scoreView), coinText));
        Game.setGameMode(Game.Mode.DEFAULT);
    }

//    private void getExtra() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            walls = extras.getBoolean(WALL_KEY, true);
//            vibro = extras.getBoolean(VIBRO_KEY, false);
//            music = extras.getBoolean(MUSIC_KEY, true);
//            //ballView.setImageResource(extras.getInt(BALL_KEY, R.drawable.ball2));
//        }
//    }

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
                BallController ballController = new BallController(ball, ballTemplate, coin);
                ballController.setWalls(wallsAlive, wallsDead);
                ballController.throwBall(MainActivity.this, dY, basket, layout);
            }
            return false;
        }

    };

    private void resetTemplate() {
        ballTemplate = new ImageView(MainActivity.this);
        ImageView ballView = findViewById(R.id.iv_translate_fling);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ballView.getWidth(), ballView.getHeight());
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.back);
        params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.back);
        params.setMargins(0, 0, (int) Game.getScoreController().getView().fromDpToPx(30), (int) Game.getScoreController().getView().fromDpToPx(125));
        ballTemplate.setLayoutParams(params);
        ballTemplate.setImageDrawable(ballView.getBackground());
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
//        if (animator != null) {
//            animator.cancel();
//        }
//        if (vibrator != null) {
//            vibrator.cancel();
//        }
//        if (soundsPlayer != null) {
//            soundsPlayer.release();
//        }
    }
}
package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.ok.technopolis.basketball.controllers.BallController;
import ru.ok.technopolis.basketball.controllers.MusicController;
import ru.ok.technopolis.basketball.controllers.ScoreController;
import ru.ok.technopolis.basketball.objects.Ball;
import ru.ok.technopolis.basketball.objects.Basket;
import ru.ok.technopolis.basketball.objects.Game;

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
        startGame();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
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
        Game.setScoreController(new ScoreController((StarView) findViewById(R.id.scoreView)));
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
            if (Game.getGameMode() == Game.Mode.UNLIMITED || ball == null || ball.isThrown()) {
                if (Game.getGameMode() == Game.Mode.DEFAULT && ball != null && ball.isThrown()) {
                    layout.removeView(ball.getObject());
                }
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                ball = new Ball(dX > 0 ? Ball.Direction.RIGHT : Ball.Direction.LEFT, ballTemplate);
                resetTemplate();
                ball.throwBall(dY / 2, dX / 2);
                BallController ballController = new BallController(ball);
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
        params.setMargins((int) Game.getScoreController().getView().fromDpToPx(30), 0, 0, (int) Game.getScoreController().getView().fromDpToPx(125));
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
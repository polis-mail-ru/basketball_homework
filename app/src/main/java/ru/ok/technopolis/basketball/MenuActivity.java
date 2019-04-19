package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private static final String SETTINGS_KEY = "settings";
    private AlertDialog exitDialog;
    private AlertDialog toolsDialog;
    private AlertDialog helpDialog;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_menu);
        settings = getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);
        createDialogs();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {
            case GameActivity.RESULT_MESSAGE:
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.AlertDialogTheme);
                String message = data.getStringExtra(GameActivity.SCORE_KEY);
                builder.setMessage(message);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void createDialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.AlertDialogTheme);
        builder.setMessage("Do you really want to exit?");
        builder.setTitle(R.string.exit);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finishAffinity();
            }
        });

        exitDialog = builder.create();

        builder.setTitle(R.string.help);
        builder.setMessage(R.string.text_help);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        helpDialog = builder.create();

        builder.setTitle(R.string.tools);
        builder.setMessage(null);

        View view = getLayoutInflater().inflate(R.layout.tools_layout, null);
        final EditText textAmountBalls = view.findViewById(R.id.amount_balls);
        final RadioGroup radioGroup = view.findViewById(R.id.levels_groups);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = settings.edit();
                if (!TextUtils.isEmpty(textAmountBalls.getText())) {
                    String s = textAmountBalls.getText().toString();
                    boolean check = true;
                    for (char ch : s.toCharArray()) {
                        check &= ch <= '9' && ch >= '0';
                    }
                    if (check) {
                        int amount = Integer.parseInt(textAmountBalls.getText().toString());
                        Toast.makeText(MenuActivity.this, "dkmkde", Toast.LENGTH_SHORT).show();
                        editor.putInt(GameActivity.AMOUNT_KEY, amount);
                    }
                }
                editor.putInt(GameActivity.MODE_KEY, radioGroup.getCheckedRadioButtonId());
                editor.apply();
                dialog.cancel();
            }
        });

        toolsDialog = builder.create();
        toolsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                textAmountBalls.setText("" + settings.getInt(GameActivity.AMOUNT_KEY, 10));
                radioGroup.check(settings.getInt(GameActivity.MODE_KEY, R.id.easy_level));
            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onegame:
                startGame(GameActivity.class);
                break;
            case R.id.twogame:
                startGame(MultiGameActivity.class);
                break;
            case R.id.help:
                if (helpDialog != null) {
                    helpDialog.show();
                }
                break;
            case R.id.tools:
                if (toolsDialog != null) {
                    toolsDialog.show();
                }
                break;
            case R.id.exit:
                if (exitDialog != null) {
                    exitDialog.show();
                }
                break;
        }
    }

    private void startGame(Class<?> cls) {
        Intent intent = new Intent(MenuActivity.this, cls);
        intent.putExtra(GameActivity.MODE_KEY, getMode());
        intent.putExtra(GameActivity.AMOUNT_KEY, getAmount());
        startActivityForResult(intent, GameActivity.RESULT_MESSAGE);
    }

    private int getAmount() {
        return settings.getInt(GameActivity.AMOUNT_KEY, 10);
    }

    private int getMode() {
        if (getModeId() == R.id.easy_level) {
            return 0;
        }
        return getModeId() == R.id.medium_level ? 1 : 2;
    }

    private int getModeId() {
        return settings.getInt(GameActivity.MODE_KEY, R.id.easy_level);
    }
}

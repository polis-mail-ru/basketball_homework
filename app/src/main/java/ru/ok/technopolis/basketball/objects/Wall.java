package ru.ok.technopolis.basketball.objects;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import java.util.Stack;

import ru.ok.technopolis.basketball.R;
import ru.ok.technopolis.basketball.Tools.RandomGenerator;

public class Wall {
    private final ImageView object;
    private int lives = 3;
    private final Stack<Wall> wallsDead;
    private final Stack<Wall> wallsAlive;
    private AnimationDrawable anim;

    public Wall(final ImageView object, final Stack<Wall> wallsDead, final Stack<Wall> wallsAlive) {
        this.object = object;
        object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lives--) {
                    case 3:
                        object.setImageDrawable(object.getContext().getResources().getDrawable(R.drawable.wall1));
                        break;
                    case 2:
                        object.setImageDrawable(object.getContext().getResources().getDrawable(R.drawable.wall2));
                        break;
                    case 1:
                        object.setImageDrawable(object.getContext().getResources().getDrawable(R.drawable.wall3));
                        break;
                    case 0:
                        object.setImageDrawable(object.getContext().getResources().getDrawable(R.drawable.wallbreak));
                        anim = (AnimationDrawable) object.getDrawable();
                        anim.start();
                        die();
                        break;
                }
            }
        });
        this.wallsDead = wallsDead;
        this.wallsAlive = wallsAlive;
    }

    float getX() {
        return object.getX();
    }

    float getY() {
        return object.getY();
    }

    float getRadius() {
        return object.getHeight() / 1.25f;
    }

    void die() {
        wallsAlive.remove(this);
        wallsDead.push(this);
    }

    public void respawn() {
        if (anim != null)
            anim.stop();
        object.setImageDrawable(object.getContext().getResources().getDrawable(R.drawable.wall));
        object.setX(RandomGenerator.wallPosX());
        object.setY(RandomGenerator.wallPosY());
        object.setVisibility(View.VISIBLE);
        lives = 3;
    }
}

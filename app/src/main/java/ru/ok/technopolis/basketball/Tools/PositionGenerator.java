package ru.ok.technopolis.basketball.Tools;

import java.util.Random;

import ru.ok.technopolis.basketball.objects.Game;

public class PositionGenerator {
    private static final Random random = new Random();

    public static float ballX() {
        return Game.getBackView().getWidth() * 3 / 4f - random.nextInt(Game.getBackView().getWidth() / 3);
    }

    public static float ballY() {
        return random.nextInt(Game.getBackView().getHeight() / 5);
    }

    public static float coinX(){
        return Game.getBackView().getWidth() / 8f + Game.getBackView().getWidth() / 4f
                - random.nextInt(Game.getBackView().getWidth() / 3);
    }

    public static float coinY(){
        return Game.getBackView().getHeight() / 8f + random.nextInt(Game.getBackView().getHeight() / 3);
    }
}

package ru.ok.technopolis.basketball.Tools;

import java.util.Random;

import ru.ok.technopolis.basketball.objects.Game;

public class RandomGenerator {
    private static final Random random = new Random();

    public static float ballPosX() {
        return Game.getBackView() == null ? 0 : Game.getBackView().getWidth() * 7 / 8f - random.nextInt(Game.getBackView().getWidth() / 2);
    }

    public static float ballPosY() {
        return Game.getBackView() == null ? 0 :  random.nextInt(Game.getBackView().getHeight() / 5);
    }

    public static float coinPosX() {
        return Game.getBackView().getWidth() / 2f
                - random.nextInt(Game.getBackView().getWidth() / 3);
    }

    public static float coinPosY() {
        return Game.getBackView().getHeight() / 8f + random.nextInt(Game.getBackView().getHeight() / 3);
    }

    public static float wallPosX() {
        return Game.getBackView().getWidth() / 2f
                - random.nextInt(Game.getBackView().getWidth() / 4);
    }

    public static float wallPosY() {
        return Game.getBackView().getHeight() / 8f + random.nextInt(Game.getBackView().getHeight() / 3);
    }

    public static int randomInt(int bound) {
        return random.nextInt(bound);
    }
}

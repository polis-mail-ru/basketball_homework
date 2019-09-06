package ru.ok.technopolis.basketball.Tools;

import java.util.Random;

import ru.ok.technopolis.basketball.objects.Game;

public class PositionGenerator {
    private static final Random random = new Random();

    public static float posX() {
        return Game.getBackView().getWidth() * 3 / 4f - random.nextInt(Game.getBackView().getWidth() / 3);
    }

    public static float posY() {
        return random.nextInt(Game.getBackView().getHeight() / 5);
    }
}

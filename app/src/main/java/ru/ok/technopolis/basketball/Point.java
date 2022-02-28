package ru.ok.technopolis.basketball;

public class Point {

    private float x;
    private float y;
    private float vx;
    private float vy;

    public Point(float coordinate) {
        this(coordinate, coordinate);
    }

    public Point(float x, float y) {
        this(x, y, 0, 0);
    }

    public Point(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getDistance(float x, float y) {
        return (float) Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void changeVerticalAxisTo(float y0) {
        y = y0 - y;
        vy = -vy;
    }
}

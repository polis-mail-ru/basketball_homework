package ru.ok.technopolis.basketball;

public class MotionOfProjectile {

    private Point point;
    private float G;

    MotionOfProjectile(Point point, float G) {
        this.point = point;
        this.G = G;
    }

    public float getTimeFlight() {
        float t1 = (float) (-point.getVy() + Math.sqrt(point.getVy() * point.getVy() + 2 * G * point.getY())) / (-G);
        float t2 = (float) (-point.getVy() - Math.sqrt(point.getVy() * point.getVy() + 2 * G * point.getY())) / (-G);
        return t1 > t2 ? t1 : t2;
    }

    public float getXFromTime(float t) {
        return point.getX() + point.getVx() * t;
    }

    public float getYFromTime(float t) {
        return (point.getY() + point.getVy() * t - G * t * t / 2);
    }
}

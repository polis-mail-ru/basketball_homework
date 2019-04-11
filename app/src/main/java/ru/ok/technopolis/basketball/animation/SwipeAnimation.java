package ru.ok.technopolis.basketball.animation;

import ru.ok.technopolis.basketball.AnimationContext;

public interface SwipeAnimation {
    void throwing(float velocityX, float velocityY);
    void rollback();
    void setEventContext(AnimationContext eventContext);
    void stopAnimation();
}

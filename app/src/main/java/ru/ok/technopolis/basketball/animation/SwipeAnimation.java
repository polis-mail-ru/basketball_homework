package ru.ok.technopolis.basketball.animation;

import ru.ok.technopolis.basketball.EventContext;

public interface SwipeAnimation {
    void throwing(float velocityX, float velocityY);
    void rollback();
    void setEventContext(EventContext eventContext);
}

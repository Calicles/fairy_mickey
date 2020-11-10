package com.antoine.contracts;

public interface RenderedMotionEntity extends RenderedEntity {
    Direction getDirection();
    boolean isMoving();

    boolean hasKey(int key_id);
}

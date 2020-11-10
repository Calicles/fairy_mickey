package com.antoine.events;

public class PlayerGrabKeyEvent {
    int key_id;

    PlayerGrabKeyEvent( int key_id )
    {
        this.key_id = key_id;
    }

    public int getKey_id() {
        return key_id;
    }
}

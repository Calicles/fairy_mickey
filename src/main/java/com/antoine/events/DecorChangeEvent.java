package com.antoine.events;

public class DecorChangeEvent {
    private int nextDecorId;

    public DecorChangeEvent(int nextDecorId)
    {
        this.nextDecorId = nextDecorId;
    }

    public int getNextDecorId() { return nextDecorId; }
}

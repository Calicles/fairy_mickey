package com.antoine.contracts;

import com.antoine.events.DecorChangeEvent;

public interface DecorListener {
    void onDecorEnded(DecorChangeEvent event);
}

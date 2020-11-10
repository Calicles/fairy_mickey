package com.antoine.contracts;

import com.antoine.events.PlayerGrabKeyEvent;

public interface View {

    void loadDecor(int nextDecorId);

    void onPlayerGrabKey( PlayerGrabKeyEvent event );
}

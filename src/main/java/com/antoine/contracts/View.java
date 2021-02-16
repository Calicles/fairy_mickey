package com.antoine.contracts;

import com.antoine.events.PlayerGrabKeyEvent;

public interface View {

    void render( Decor decor );

    void loadDecor(int nextDecorId);

    void onPlayerGrabKey( PlayerGrabKeyEvent event );

    void onEndGame( Controler controler );

    void fadeOut();

    boolean isFadeOutFinished();

    void fadeIn();

    boolean isFadeInFinished();

    void onKeyFound(int key_id);
}

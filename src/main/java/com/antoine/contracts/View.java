package com.antoine.contracts;

public interface View {

    void render( Decor decor );

    void loadDecor(int nextDecorId);

    void onEndGame( Controler controler );

    void fadeOut();

    boolean isFadeOutFinished();

    void fadeIn();

    boolean isFadeInFinished();

    void onKeyFound(int key_id);
}

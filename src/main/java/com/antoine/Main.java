package com.antoine;

import com.antoine.game.Game;
import com.antoine.view.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();

        SwingUtilities.invokeLater(()->
        {
            new Frame( game );
        });

    }
}

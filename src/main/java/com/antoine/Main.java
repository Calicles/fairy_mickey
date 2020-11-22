package com.antoine;

import com.antoine.game.Game;
import com.antoine.view.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->
        {
            Game game = new Game();
            new Frame( game );
        });

    }
}

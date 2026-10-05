package com.antoine;

import com.antoine.view.SwingGame;
import com.antoine.view.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->
        {
            SwingGame game = new SwingGame();
            new Frame( game );
        });

    }
}

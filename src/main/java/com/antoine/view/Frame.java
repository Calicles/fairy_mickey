package com.antoine.view;

import com.antoine.contracts.Controler;
import com.antoine.contracts.View;
import com.antoine.events.PlayerGrabKeyEvent;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame implements View
{
    private GamePane        gamePane;
    private InventoryPane   inventoryPane;
    private Controler       controler;

    public Frame()
    {
        super("Fairy_Mickey" );

        this.gamePane = new GamePane();
        this.inventoryPane = new InventoryPane();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener( new InternKeyListener() );
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void loadDecor(int nextDecorId) {

    }

    @Override
    public void onPlayerGrabKey(PlayerGrabKeyEvent event) {

    }

    private class InternKeyListener implements KeyListener {


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        private boolean isKeyLeft(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_LEFT;
        }

        private boolean isKeyRight(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_RIGHT;
        }

        private boolean isKeyUp(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_UP;
        }

        private boolean isKeyDown(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_DOWN;
        }
    }
}

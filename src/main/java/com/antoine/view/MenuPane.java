package com.antoine.view;

import javax.swing.*;
import java.awt.*;

public class MenuPane extends JPanel
{
    public interface Command {
        void execute();
    }

    private int width;
    private int height;
    private final Command triggerNewGame;

    public MenuPane( Command onNewGame, int _width, int _height ) {
        super();

        this.width          = _width;
        this.height         = _height;
        this.triggerNewGame = onNewGame;
        this.setLayout( new BorderLayout() );
        JButton newGameButton = new JButton( "NEW GAME" );
        newGameButton.addActionListener( ( event )->this.triggerNewGame.execute() );
        this.setBackground( Color.BLACK );
        this.add( newGameButton, BorderLayout.CENTER );
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension( this.width, this.height );
    }
}

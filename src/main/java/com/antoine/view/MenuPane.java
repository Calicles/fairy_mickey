package com.antoine.view;


import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MenuPane extends JPanel
{
    public interface Command {
        void execute();
    }

    private int width;
    private int height;
    private double coef;
    private final Command triggerNewGame;

    public MenuPane( Command onNewGame, int _width, int _height, double _coef ) {
        super( true );

        this.width          = _width;
        this.height         = _height;
        this.coef           = _coef;
        this.triggerNewGame = onNewGame;
        this.setLayout( new BorderLayout() );
        JButton newGameButton = new JButton();
        newGameButton.setOpaque( false );
        newGameButton.setContentAreaFilled( false );
        newGameButton.setBorderPainted( false );
        newGameButton.addActionListener( ( event )->this.triggerNewGame.execute() );
        this.setBackground( Color.BLACK );
        this.add( newGameButton, BorderLayout.CENTER );
        this.repaint();
    }

    @Override
    public void paint( Graphics g )
    {
        Graphics2D g2d = (Graphics2D) g;

        Map<TextAttribute, Object> attributes = new HashMap<>();
        Font currentFont = g.getFont();
        attributes.put(TextAttribute.FAMILY, currentFont.getFamily());
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attributes.put(TextAttribute.SIZE, (int) (currentFont.getSize() * 2 * coef));
        Font myFont = Font.getFont(attributes);

        g2d.setFont(myFont);
        BufferedImage img = ImageReader.readImage( "/images/decor/transition/lac_castor_house.jpg" );
        g2d.drawImage( img, 0, 0, this.width, this.height, null );
        int width = g.getFontMetrics().stringWidth("NEW GAME");
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString("NEW GAME", this.width / 2 - width / 2, this.height / 2);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension( this.width, this.height );
    }
}

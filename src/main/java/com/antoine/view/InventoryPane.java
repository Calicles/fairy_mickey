package com.antoine.view;

import javax.swing.*;
import java.awt.*;

public class InventoryPane  extends JPanel
{

    InventoryPane()
    {
        super();
    }


    @Override
    public Dimension getPreferredSize() {
        return null;
    }


    /**
     * @see JPanel#paintComponent(Graphics)
     * @param g graphics chargé de l'affichage.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw
    }

    public void render()
    {
        this.repaint();
    }
}

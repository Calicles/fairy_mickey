package com.antoine.view;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class InventoryPane  extends JPanel
{
    private int width;
    private int height;
    private KeyImage[] images;

    InventoryPane( int width, double dim_coef )
    {
        super( true );
        this.width = width;
        this.height = (int) (DimensionHelper.INVENTORY_HEIGHT * dim_coef);
        this.setBackground( Color.black );
        String path = "/images/objects/keys/";
        String[] key_names = { "bronze.png", "gold.png", "silver.png" };
        int[]    key_ids    = { 1, 3, 2 };
        loadKeys( path, key_names, key_ids );
    }

    private void loadKeys(String path, String[] key_names, int[] key_ids ) {
        this.setLayout( new FlowLayout() );
        this.images = new KeyImage[ key_names.length ];
        for ( int i = 0; i < key_names.length; i++ )
        {
            this.images[ i ] = new KeyImage( path + key_names[i], key_ids[i] );
            this.add ( this.images[i] );
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension( width, height );
    }

    public void onKeyFound( int id )
    {
        KeyImage img = Arrays.stream( this.images ).filter(i->i.getId() == id).findFirst().orElse(null);
        if ( img != null )
        {
            img.setKeyFound();
            this.repaint();
        }
    }

    private class KeyImage extends JPanel
    {
        private JLabel image;
        private int    id;

        KeyImage( String path, int id )
        {
            super(true );
            this.id    = id;
            this.image = new JLabel( new ImageIcon( getClass().getResource( path )));
            this.setBackground( Color.DARK_GRAY );
            this.setLayout( new BorderLayout() );
            this.setBorder( BorderFactory.createLoweredBevelBorder() );
            this.add( image, BorderLayout.CENTER );
        }

        int getId(){ return this.id; }

        public void setKeyFound() {
            this.setBackground( Color.BLUE );
        }
    }
}

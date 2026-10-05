package com.antoine.view;

import com.antoine.helpers.JsonHelper;

import org.json.JSONObject;

import java.awt.*;

/**
 * <p>Calcule le coefficient d'agrandissement du jeu, partagé par le modèle et la vue.</p>
 * Le coefficient de conf.json est un maximum : il est réduit si la fenêtre
 * (décor + inventaire) ne tient pas dans l'écran.
 */
public class DimensionHelper {

    private static final String CONF_PATH = "/jsons/conf.json";
    /** hauteur de l'inventaire avant agrandissement */
    public static final int INVENTORY_HEIGHT = 100;
    /** place réservée aux bordures et à la barre de titre de la fenêtre */
    private static final int WINDOW_DECORATION = 40;

    private static Double dim_coef;

    public static synchronized double getDimCoef()
    {
        if ( dim_coef == null ) {
            dim_coef = computeDimCoef();
        }
        return dim_coef;
    }

    private static double computeDimCoef()
    {
        JSONObject conf = JsonHelper.strToJson( CONF_PATH );
        double max_coef = conf.getDouble( "dim_coef" );
        int width  = conf.getInt( "width" );
        int height = conf.getInt( "height" ) + INVENTORY_HEIGHT;

        if ( GraphicsEnvironment.isHeadless() ) {
            return max_coef;
        }
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double fit = Math.min(
                (double) ( screen.width - WINDOW_DECORATION ) / width,
                (double) ( screen.height - WINDOW_DECORATION ) / height );
        // arrondi au dixième inférieur pour garder des dimensions stables
        fit = Math.floor( fit * 10 ) / 10;
        return Math.max( 1, Math.min( max_coef, fit ));
    }
}

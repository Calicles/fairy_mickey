package com.antoine.game;

import com.antoine.contracts.Decor;
import com.antoine.contracts.Decor_key;
import com.antoine.helpers.DimensionHelper;
import com.antoine.helpers.Direction_helper;
import com.antoine.helpers.JsonHelper;
import com.antoine.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DecorFactory {
    static HashMap< Integer, Decor > container = new HashMap<>( 10 );
    static final String JSON_FILE_PATH = "/jsons/decor.json";
    static org.json.JSONObject json;
    static double dim_coef;

    static {
        json = JsonHelper.strToJson( JSON_FILE_PATH );
        dim_coef = DimensionHelper.getDimCoef();
    }

    static void build( Abstract_Decor decor, JSONObject decor_json )
    {
        decor.setPlayer_distance_transform_speed( decor_json.getDouble( "distance_ratio_speed" ));
        decor.setPlayer_distance_ratio( decor_json.getInt( "distance_ratio" ));
        org.json.JSONArray exits = decor_json.getJSONArray( "exits" );

        // build each exit
        for ( int i = 0; i < exits.length(); ++i ){
            JSONObject shape = exits.getJSONObject( i );
            decor.addExit( shape.getInt( "next_decor_id" ));
            JSONArray points = shape.getJSONArray( "points" );

            // add point to exit shape
            for ( int j = 0; j < points.length(); ++j ){
                JSONObject p = points.getJSONObject( j );
                decor.addPointToCurrentExit( (int) ( p.getInt( "x" ) * dim_coef ), (int) ( p.getInt( "y" ) * dim_coef ));
            }
        }

        // add entrance player position by player direction
        JSONArray entrances = decor_json.getJSONArray( "entrance_player_direction_coordinates" );
        for (int i = 0; i < entrances.length(); i++) {
            JSONObject entry = entrances.getJSONObject( i );
            JSONObject point_json = entry.getJSONObject( "position" );
            decor.addPlayerEntrance(
                    entry.getString( "name" ),
                    (int) ( point_json.getInt( "x" ) * dim_coef ),
                    (int) ( point_json.getInt( "y" ) * dim_coef ));
        }
    }


    static void build1D( Decor_1D decor, JSONObject decor_json) {
        JSONArray lineMask = decor_json.getJSONArray( "line_mask" );
        for ( int i = 0; i < lineMask.length(); ++i )
        {
            JSONObject point = lineMask.getJSONObject( i );
            decor.addPointToMask( (int) ( point.getInt( "x" ) * dim_coef ), (int) ( point.getInt( "y" ) * dim_coef ));
        }

        decor.setIs_vertical( decor_json.getBoolean( "is_vertical" ));
    }

    static void build2D( Decor_2D decor, JSONObject decor_json )
    {
        JSONArray solids = decor_json.getJSONArray( "solids" );
        for (int i = 0; i < solids.length(); i++) {
            decor.createShape();
            JSONArray solids_points =  solids.getJSONArray( i );
            for (int j = 0; j < solids_points.length(); j++) {
                JSONObject point = solids_points.getJSONObject( j );
                decor.addPointToCurrentShape(
                        (int) ( point.getInt( "x" ) * dim_coef ),
                        (int) (point.getInt( "y" ) * dim_coef )
                );
            }
        }
    }

    static void buildWithKey(Decor_key decor, JSONObject decor_json )
    {
        JSONObject key = decor_json.getJSONObject( "key" );
        decor.setKeyId( decor_json.getInt( "key_id" ));
        decor.setKeyCoordinates( (int) (key.getInt( "x") * dim_coef), (int) (key.getInt( "y" ) * dim_coef));
        decor.setKeyDimension( (int) (key.getInt( "width" ) * dim_coef), (int) (key.getInt( "height" ) * dim_coef ));
    }


    static Decor_1D createDecor1D(JSONObject decor_json, int id)
    {
        Decor_1D decor = new Decor_1D( id );
        build( decor, decor_json );
        build1D( decor, decor_json );
        return decor;
    }

    static Decor_2D createDecor2D(JSONObject decor_json, int id)
    {
        Decor_2D decor = new Decor_2D( id );
        build( decor, decor_json );
        // add all solids presents in map
        build2D( decor, decor_json );
        return decor;
    }

    static Decor_with_key createDecorWithKey(JSONObject decor_json, int id)
    {
        Decor_with_key decor = new Decor_with_key( id );
        build( decor, decor_json );
        build1D( decor, decor_json );
        buildWithKey( decor, decor_json );

        return decor;
    }


    private static Decor createDecorWithKey2D(JSONObject decor_json, int id) {
        Decor_with_key_2d decor = new Decor_with_key_2d( id );
        build       ( decor, decor_json );
        build2D     ( decor, decor_json );
        buildWithKey( decor, decor_json );
        return decor;
    }

    static Decor getDecor( int id )
    {
        if ( container.containsKey( id )) {
            return container.get( id );
        }
        String id_str = Integer.toString( id );
        org.json.JSONObject decor_json = json.getJSONObject( id_str );
        int type = decor_json.getInt( "type" );
        Decor decor = null;
        switch ( type )
        {
            case 1:
                decor = createDecor1D( decor_json, id );
                break;
            case 2:
                decor = createDecor2D( decor_json, id );
                break;
            case 3:
                decor = createDecorWithKey( decor_json, id );
                break;
            case 4:
                decor = createDecorWithKey2D( decor_json, id );
        }
        container.put( id, decor );
        decor.setPlayerLastDirection(
                Direction_helper.strToDir( decor_json.getString( "player_entrance_direction" )));
        return decor;
    }



    public static void setPlayer( Player player )
    {
        player.adaptSpeed( dim_coef );
        Abstract_Decor.setPlayer( player );
    }
}

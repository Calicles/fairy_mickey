package com.antoine.game;

import com.antoine.contracts.Decor;
import com.antoine.helpers.JsonHelper;
import com.antoine.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DecorFactory {
    static HashMap< Integer, Decor > container;
    static final String JSON_FILE_PATH = "";
    static org.json.JSONObject json;

    static {
        json = JsonHelper.strToJson( JSON_FILE_PATH );
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
                JSONObject p = points.getJSONObject( i );
                decor.addPointToCurrentExit( p.getInt( "x" ), p.getInt( "y" ));
            }
        }
    }

    static Decor_1D createDecor1D(JSONObject decor_json, int id)
    {
        Decor_1D decor = new Decor_1D( id );
        build( decor, decor_json );
        decor.setEntringPlayerPos( decor_json.getInt( "odd_x" ), decor_json.getInt( "odd_y" ));
        decor.setComeBackPlayerPos( decor_json.getInt( "even_x" ), decor_json.getInt( "even_y" ));
        JSONArray lineMask = decor_json.getJSONArray( "line_mask" );
        for ( int i = 0; i < lineMask.length(); ++i )
        {
            JSONObject point = lineMask.getJSONObject( i );
            decor.addPointToCurrentExit( point.getInt( "x" ), point.getInt( "y" ));
        }

        container.put( id, decor );
        return null;
    }

    static Decor_2D createDecor2D(JSONObject decor_json, int id)
    {
        Decor_2D decor = new Decor_2D( id );
        build( decor, decor_json );
        container.put( id, decor );
        return null;
    }

    static Decor_with_key createDecorWithKey(JSONObject decor_json, int id)
    {
        Decor_with_key decor = new Decor_with_key( id );
        build( decor,  decor_json );
        container.put( id, decor );
        return null;
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
        }
        return decor;
    }
}

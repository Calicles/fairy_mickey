package com.antoine.view;

import com.antoine.helpers.JsonHelper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RenderedDecorFactory {

    private static final String JSON_FILE_PATH = "/jsons/decorImages.json";
    private static final String IMG_PATH       = "/images/decor/";
    private static JSONObject json;

    static {
        try
        {
            json = JsonHelper.strToJson( JSON_FILE_PATH );
        } catch ( ExceptionInInitializerError e ) {
            System.out.println( e.getMessage());
        }
    }

    public static RenderedDecor getRenderedDecor( int id )
    {
        JSONObject decor_json = json.getJSONObject( Integer.toString( id ));
        String path = IMG_PATH + decor_json.getString( "path" );
        RenderedDecor decor = new RenderedDecor( id );
        decor.loadImgs( path + decor_json.getString( "bg" ), path + decor_json.getString( "fg" ));
        return decor;
    }
}

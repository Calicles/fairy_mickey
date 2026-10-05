package com.antoine.view;

import com.antoine.helpers.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;

public class RenderedDecorFactory {

    private static final String JSON_FILE_PATH = "/jsons/decorImages.json";
    private static final String IMG_PATH       = "/images/decor/";
    private static JSONObject json;

    static {
        json = JsonHelper.strToJson( JSON_FILE_PATH );
    }

    public static RenderedDecor getRenderedDecor( int id )
    {
        JSONObject decor_json = json.getJSONObject( Integer.toString( id ));
        String path = IMG_PATH + decor_json.getString( "path" );
        RenderedDecor decor = null;
        if ( ! decor_json.has( "key" )) {
            decor = new RenderedDecor( id );
        } else {
            RenderedDecorKey decorKey = new RenderedDecorKey( id );
            JSONObject key = decor_json.getJSONObject( "key" );
            String keyPath = key.getString( "path");
            JSONArray keyAnim = key.getJSONArray( "animation" );
            StringBuilder builder = new StringBuilder();
            builder.append( keyPath );
            for ( Object obj : keyAnim ) {
                builder.append( obj );
                decorKey.loadKeyImg( builder.toString() );
                builder.delete( keyPath.length(), builder.length() );
            }
            decor = decorKey;
        }
        decor.loadImgs( path + decor_json.getString( "bg" ), path + decor_json.getString( "fg" ));
        return decor;
    }
}

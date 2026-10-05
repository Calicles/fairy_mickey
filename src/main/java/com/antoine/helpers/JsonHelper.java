package com.antoine.helpers;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonHelper {

    /** ouvre une ressource à partir de son chemin (ex : "/jsons/decor.json") */
    public interface ResourceOpener {
        InputStream open( String path ) throws IOException;
    }

    /** par défaut les ressources sont lues dans le classpath ; Android les lit dans ses assets */
    private static ResourceOpener opener = JsonHelper.class::getResourceAsStream;

    public static void setResourceOpener( ResourceOpener resourceOpener ) {
        opener = resourceOpener;
    }

    public static JSONObject strToJson( String file_name_str ) {
        JSONObject json = null;
        try ( InputStream in = opener.open( file_name_str ))
        {
            if ( in == null ) {
                throw new IllegalArgumentException( "ressource introuvable : " + file_name_str );
            }
            BufferedReader reader = new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ));
            StringBuilder json_str = new StringBuilder();
            String s;
            while ( (s = reader.readLine()) != null ) {
                json_str.append( s );
            }
            json = new JSONObject( json_str.toString() );
        } catch ( IOException exc ){
            throw new UncheckedIOException( "lecture impossible : " + file_name_str, exc );
        } catch ( RuntimeException exc ) {
            throw exc;
        } catch ( Exception exc ) {
            // JSONException est une exception vérifiée sur Android
            throw new IllegalArgumentException( "json invalide : " + file_name_str, exc );
        }
        return json;
    }
}

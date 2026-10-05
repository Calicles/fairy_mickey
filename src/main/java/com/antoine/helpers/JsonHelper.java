package com.antoine.helpers;

import org.json.JSONObject;

import java.io.*;

public class JsonHelper {
    public static JSONObject strToJson( String file_name_str ) {
        JSONObject json = null;
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader(JsonHelper.class.getResourceAsStream( file_name_str ) )))
        {
            StringBuilder json_str = new StringBuilder();
            String s;
            while ( (s = reader.readLine()) != null ) {
                json_str.append( s );
            }
            json = new JSONObject( json_str.toString() );
        } catch ( IOException exc ){
            // handle error
        }
        return json;
    }
}

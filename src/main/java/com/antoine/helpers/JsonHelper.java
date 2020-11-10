package com.antoine.helpers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonHelper {
    public static JSONObject strToJson( String file_name_str ) {
        JSONObject json = null;
        try ( BufferedReader reader = new BufferedReader( new FileReader( ( file_name_str )))) {
            StringBuilder json_str = new StringBuilder();
            char c;
            while ( (c = (char) reader.read()) != -1 ) {
                json_str.append( c );
            }
            json = new JSONObject( json_str.toString() );
        } catch ( IOException exc ){
            // handle error
        }
        return json;
    }
}

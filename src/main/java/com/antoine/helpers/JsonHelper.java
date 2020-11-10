package com.antoine.helpers;

import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

public class JsonHelper {
    public static JSONObject strToJson( String file_name_str ) {
        JSONObject json = null;
        try ( BufferedReader reader = new BufferedReader(
                new FileReader(
                        new File( JsonHelper.class.getClassLoader().getResource( file_name_str ).toURI() ))))
        {
            StringBuilder json_str = new StringBuilder();
            char c;
            while ( (c = (char) reader.read()) != -1 ) {
                json_str.append( c );
            }
            json = new JSONObject( json_str.toString() );
        } catch ( IOException | URISyntaxException exc ){
            // handle error
        }
        return json;
    }
}

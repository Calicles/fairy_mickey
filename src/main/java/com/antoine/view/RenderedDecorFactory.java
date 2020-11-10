package com.antoine.view;

import com.antoine.helpers.JsonHelper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RenderedDecorFactory {

    static final String JSON_FILE_PATH = "";
    static org.json.JSONObject json;

    static {
        json = JsonHelper.strToJson( JSON_FILE_PATH );
    }

    public static RenderedDecor getRenderedDecor( int id )
    {
        return null;
    }
}

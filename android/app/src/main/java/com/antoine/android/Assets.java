package com.antoine.android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.antoine.helpers.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Accès aux ressources du jeu (src/main/resources), empaquetées comme assets Android.
 * Les chemins sont ceux du jeu desktop : "/images/...", "/jsons/...".
 */
final class Assets {

    private static AssetManager manager;

    private Assets() {}

    static void init( AssetManager assetManager )
    {
        manager = assetManager;
        JsonHelper.setResourceOpener( Assets::open );
    }

    static InputStream open( String path ) throws IOException
    {
        return manager.open( strip( path ));
    }

    static android.content.res.AssetFileDescriptor openFd( String path ) throws IOException
    {
        return manager.openFd( strip( path ));
    }

    static Bitmap bitmap( String path )
    {
        try ( InputStream in = open( path )) {
            Bitmap bitmap = BitmapFactory.decodeStream( in );
            if ( bitmap == null ) throw new IllegalArgumentException( "image illisible : " + path );
            return bitmap;
        } catch ( IOException e ) {
            throw new UncheckedIOException( "image introuvable : " + path, e );
        }
    }

    private static String strip( String path )
    {
        return path.startsWith( "/" ) ? path.substring( 1 ) : path;
    }
}

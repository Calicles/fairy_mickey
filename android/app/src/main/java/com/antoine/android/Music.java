package com.antoine.android;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.antoine.helpers.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Musique de fond (pistes de musics.json enchaînées en boucle) et bruitage du menu.
 * Toutes les opérations MediaPlayer se font sur le thread principal.
 */
final class Music {

    private static final String TAG = "FairyMickey";
    private static final String CLICK = "/musics/zemeckis_sound_higher.wav";

    private final Handler main = new Handler( Looper.getMainLooper() );
    private final String path;
    private final String[] tracks;

    private MediaPlayer current;
    private MediaPlayer click;
    private int index;
    private boolean playing;

    Music()
    {
        try {
            JSONObject json = JsonHelper.strToJson( "/jsons/musics.json" );
            path = json.getString( "path" );
            JSONArray arr = json.getJSONArray( "musics" );
            tracks = new String[ arr.length() ];
            for ( int i = 0; i < tracks.length; i++ ) tracks[ i ] = arr.getString( i );
        } catch ( JSONException e ) {
            throw new IllegalStateException( "musics.json invalide", e );
        }
    }

    /** joue le bruitage du menu puis appelle onEnd (immédiatement si le son est indisponible) */
    void playClick( Runnable onEnd )
    {
        main.post( () -> {
            release( click );
            click = create( CLICK );
            if ( click == null ) {
                onEnd.run();
                return;
            }
            click.setOnCompletionListener( mp -> {
                release( mp );
                click = null;
                onEnd.run();
            });
            click.start();
        });
    }

    void start()
    {
        main.post( () -> {
            playing = true;
            index = 0;
            playNext();
        });
    }

    void stop()
    {
        main.post( () -> {
            playing = false;
            release( current );
            current = null;
        });
    }

    void pause()
    {
        main.post( () -> {
            if ( current != null && current.isPlaying() ) current.pause();
        });
    }

    void resume()
    {
        main.post( () -> {
            if ( playing && current != null && ! current.isPlaying() ) current.start();
        });
    }

    void release()
    {
        main.post( () -> {
            playing = false;
            release( current );
            release( click );
            current = null;
            click = null;
        });
    }

    private void playNext()
    {
        release( current );
        current = null;
        if ( ! playing || tracks.length == 0 ) return;
        // on essaie chaque piste au plus une fois pour ne pas boucler sur des fichiers illisibles
        for ( int tries = 0; tries < tracks.length && current == null; tries++ ) {
            current = create( path + tracks[ index ] );
            index = ( index + 1 ) % tracks.length;
        }
        if ( current == null ) return;
        current.setOnCompletionListener( mp -> playNext() );
        current.start();
    }

    private static MediaPlayer create( String asset )
    {
        MediaPlayer player = new MediaPlayer();
        try ( AssetFileDescriptor afd = Assets.openFd( asset )) {
            player.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength() );
            player.prepare();
            return player;
        } catch ( Exception e ) {
            Log.w( TAG, "son désactivé (" + asset + ")", e );
            player.release();
            return null;
        }
    }

    private static void release( MediaPlayer player )
    {
        if ( player != null ) player.release();
    }
}

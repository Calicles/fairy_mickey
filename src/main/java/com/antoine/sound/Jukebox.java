package com.antoine.sound;

import com.antoine.helpers.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;

public class Jukebox {
    private String[]    musics_str;
    private String      music_path;
    private MusicPlayer current_music = null;
    private int         music_index;

    public Jukebox() {
        this.init();
    }
    
    public void start()
    {
        this.music_index = 0;
        this.loadNextMusic();
    }

    public void stop()
    {
        if ( this.current_music != null ) {
            this.current_music.arret();
            this.current_music = null;
        }
    }

    private void incrMusicIndex()
    {
        this.music_index = ++this.music_index % this.musics_str.length;
    }

    private void loadNextMusic() {
        this.current_music = new MusicPlayer(
            this.music_path + musics_str[ this.music_index ],
            1f,
            this::loadNextMusic );

        this.incrMusicIndex();
        this.current_music.play();
    }

    private void init()
    {
        String json_file_path = "/jsons/musics.json";
        JSONObject json = JsonHelper.strToJson( json_file_path );
        this.music_path = json.getString( "path" );
        JSONArray music_arr = json.getJSONArray( "musics" );
        this.musics_str = new String[ music_arr.length() ];

        for (int i = 0; i < this.musics_str.length; i++) {
            this.musics_str[ i ] = music_arr.getString( i );
        }
    }
}

package com.antoine.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private GameSurface surface;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        Assets.init( getAssets() );
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        surface = new GameSurface( this );
        setContentView( surface );
        surface.requestFocus();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hideSystemBars();
        surface.resume();
    }

    @Override
    protected void onPause()
    {
        surface.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        surface.release();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged( boolean hasFocus )
    {
        super.onWindowFocusChanged( hasFocus );
        if ( hasFocus ) hideSystemBars();
    }

    /** plein écran : les barres système réapparaissent temporairement d'un glissement depuis le bord */
    @SuppressWarnings( "deprecation" )
    private void hideSystemBars()
    {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
    }
}

package com.antoine.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.antoine.contracts.Controler;
import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
import com.antoine.contracts.View;
import com.antoine.helpers.Direction_helper;
import com.antoine.helpers.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue Android du jeu : dessine les décors, Mickey, les clés, l'inventaire et les fondus.
 * Le monde est dessiné dans son repère d'origine (700 x 400, coefficient 1) puis mis à l'échelle de l'écran.
 */
final class Renderer implements View {

    static final int WORLD_WIDTH      = 700;
    static final int WORLD_HEIGHT     = 400;
    static final int INVENTORY_HEIGHT = 100;
    static final int SCREEN_HEIGHT    = WORLD_HEIGHT + INVENTORY_HEIGHT;

    private static final String IMG_PATH        = "/images/decor/";
    private static final int    ALPHA_MAX       = 255;
    private static final int    ALPHA_STEP      = 2;
    private static final int    ANIM_SLOWER_MAX = 6;
    private static final int    KEY_ANIM_SLOWER_MAX = 15;

    /** images d'un décor */
    private static final class DecorImages {
        Bitmap background;
        Bitmap front;
        List< Bitmap > key;
    }

    private final GameSurface surface;
    private final JSONObject decorImagesJson = JsonHelper.strToJson( "/jsons/decorImages.json" );
    private final Map< Integer, DecorImages > cache = new HashMap<>();
    private final Map< Direction, List< Bitmap >> playerImgs = new EnumMap<>( Direction.class );
    private final Bitmap menuBackground;

    // inventaire, dans l'ordre d'affichage du jeu desktop
    private final int[]     keyIds   = { 1, 3, 2 };
    private final Bitmap[]  keyIcons = new Bitmap[ 3 ];
    private final boolean[] keyFound = new boolean[ 3 ];

    private DecorImages current;
    private RenderedEntity[] entities;
    private boolean inTransition;
    private int alpha;
    private int spriteIndex, animSlower;
    private int keySpriteIndex, keyAnimSlower;
    private boolean keyAnimForward = true;

    private final Paint bitmapPaint = new Paint( Paint.FILTER_BITMAP_FLAG );
    private final Paint spritePaint = new Paint();   // sans filtrage : pixel art net
    private final Paint fadePaint   = new Paint();
    private final Paint boxPaint    = new Paint();
    private final Paint borderPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
    private final Paint titlePaint  = new Paint( Paint.ANTI_ALIAS_FLAG );
    private final Paint hintPaint   = new Paint( Paint.ANTI_ALIAS_FLAG );
    private final Rect  srcRect = new Rect();
    private final RectF dstRect = new RectF();

    Renderer( GameSurface surface )
    {
        this.surface = surface;
        loadPlayerImages();
        String[] names = { "bronze.png", "gold.png", "silver.png" };
        for ( int i = 0; i < names.length; i++ ) {
            keyIcons[ i ] = Assets.bitmap( "/images/objects/keys/" + names[ i ] );
        }
        menuBackground = Assets.bitmap( "/images/decor/transition/lac_castor_house.jpg" );
        current = images( 1 );

        borderPaint.setStyle( Paint.Style.STROKE );
        borderPaint.setStrokeWidth( 2 );
        borderPaint.setColor( Color.BLACK );
        titlePaint.setColor( Color.WHITE );
        titlePaint.setTextSize( 44 );
        titlePaint.setTypeface( Typeface.DEFAULT_BOLD );
        titlePaint.setTextAlign( Paint.Align.CENTER );
        titlePaint.setShadowLayer( 4, 2, 2, Color.BLACK );
        hintPaint.set( titlePaint );
        hintPaint.setTextSize( 20 );
        hintPaint.setTypeface( Typeface.DEFAULT );
    }

    int playerWidth()  { return playerImgs.get( Direction.BOTTOM ).get( 0 ).getWidth(); }
    int playerHeight() { return playerImgs.get( Direction.BOTTOM ).get( 0 ).getHeight(); }

    /** remet la vue dans l'état d'une nouvelle partie */
    void reset()
    {
        for ( int i = 0; i < keyFound.length; i++ ) keyFound[ i ] = false;
        alpha    = 0;
        entities = null;
        loadDecor( 1 );
    }

    // ------------------------------------------------------------------ View

    @Override
    public void render( Decor decor )
    {
        this.entities     = decor.getEntities();
        this.inTransition = decor.isEnding() || decor.isStarting();
        surface.paintFrame( this::drawGame );
    }

    @Override
    public void loadDecor( int nextDecorId )
    {
        current = images( nextDecorId );
        keySpriteIndex = 0;
        keyAnimSlower  = 0;
        keyAnimForward = true;
    }

    @Override
    public void onEndGame( Controler controler )
    {
        surface.onEndGame( controler );
    }

    @Override
    public void fadeOut()
    {
        alpha = Math.min( ALPHA_MAX, alpha + ALPHA_STEP );
    }

    @Override
    public boolean isFadeOutFinished() { return alpha == ALPHA_MAX; }

    @Override
    public void fadeIn()
    {
        alpha = Math.max( 0, alpha - ALPHA_STEP );
    }

    @Override
    public boolean isFadeInFinished() { return alpha == 0; }

    @Override
    public void onKeyFound( int key_id )
    {
        for ( int i = 0; i < keyIds.length; i++ ) {
            if ( keyIds[ i ] == key_id ) keyFound[ i ] = true;
        }
    }

    // ------------------------------------------------------------------ dessin

    void drawMenu( Canvas canvas )
    {
        canvas.drawColor( Color.BLACK );
        surface.applyWorldTransform( canvas );
        drawStretched( canvas, menuBackground, 0, 0, WORLD_WIDTH, SCREEN_HEIGHT, bitmapPaint );
        canvas.drawText( "NEW GAME", WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, titlePaint );
        canvas.drawText( "Touchez l'écran pour jouer", WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f + 40, hintPaint );
        canvas.restore();
    }

    private void drawGame( Canvas canvas )
    {
        canvas.drawColor( Color.BLACK );
        surface.applyWorldTransform( canvas );
        canvas.save();
        canvas.clipRect( 0, 0, WORLD_WIDTH, WORLD_HEIGHT );
        drawStretched( canvas, current.background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT, bitmapPaint );
        drawEntities( canvas );
        if ( current.front != null ) {
            drawStretched( canvas, current.front, 0, 0, WORLD_WIDTH, WORLD_HEIGHT, bitmapPaint );
        }
        if ( inTransition ) {
            fadePaint.setColor( Color.argb( alpha, 0, 0, 0 ));
            canvas.drawRect( 0, 0, WORLD_WIDTH, WORLD_HEIGHT, fadePaint );
        }
        canvas.restore();
        drawInventory( canvas );
        canvas.restore();
        surface.drawControls( canvas );
    }

    private void drawEntities( Canvas canvas )
    {
        if ( entities == null ) return;
        RenderedMotionEntity player = (RenderedMotionEntity) entities[ 0 ];
        List< Bitmap > sprites = playerImgs.get( player.getDirection() );
        if ( player.isMoving() && ! inTransition ) {
            if ( ++animSlower % ANIM_SLOWER_MAX == 0 ) {
                animSlower  = 0;
                spriteIndex = ( spriteIndex + 1 ) % sprites.size();
            }
        } else {
            spriteIndex = 0;
        }
        drawStretched( canvas, sprites.get( spriteIndex ),
                player.getX(), player.getY(), player.getWidth(), player.getHeight(), spritePaint );

        if ( entities.length > 1 && entities[ 1 ] != null && current.key != null ) {
            animateKey();
            RenderedEntity key = entities[ 1 ];
            drawStretched( canvas, current.key.get( keySpriteIndex ),
                    key.getX(), key.getY(), key.getWidth(), key.getHeight(), bitmapPaint );
        }
    }

    /** animation aller-retour de la clé, comme RenderedDecorKey */
    private void animateKey()
    {
        if ( current.key.size() < 2 || ++keyAnimSlower % KEY_ANIM_SLOWER_MAX != 0 ) return;
        keyAnimSlower = 0;
        if ( keyAnimForward ) {
            if ( ++keySpriteIndex == current.key.size() - 1 ) keyAnimForward = false;
        } else {
            if ( --keySpriteIndex == 0 ) keyAnimForward = true;
        }
    }

    private void drawInventory( Canvas canvas )
    {
        float boxW = 95, boxH = 80, gap = 6;
        float x = ( WORLD_WIDTH - ( keyIcons.length * boxW + ( keyIcons.length - 1 ) * gap )) / 2f;
        float y = WORLD_HEIGHT + ( INVENTORY_HEIGHT - boxH ) / 2f;
        for ( int i = 0; i < keyIcons.length; i++ ) {
            boxPaint.setColor( keyFound[ i ] ? Color.BLUE : Color.DKGRAY );
            canvas.drawRect( x, y, x + boxW, y + boxH, boxPaint );
            canvas.drawRect( x, y, x + boxW, y + boxH, borderPaint );
            Bitmap icon = keyIcons[ i ];
            float ratio = Math.min( ( boxW - 10 ) / icon.getWidth(), ( boxH - 10 ) / icon.getHeight() );
            float w = icon.getWidth() * ratio, h = icon.getHeight() * ratio;
            drawStretched( canvas, icon, x + ( boxW - w ) / 2, y + ( boxH - h ) / 2, w, h, bitmapPaint );
            x += boxW + gap;
        }
    }

    private void drawStretched( Canvas canvas, Bitmap bitmap, float x, float y, float w, float h, Paint paint )
    {
        srcRect.set( 0, 0, bitmap.getWidth(), bitmap.getHeight() );
        dstRect.set( x, y, x + w, y + h );
        canvas.drawBitmap( bitmap, srcRect, dstRect, paint );
    }

    // ------------------------------------------------------------------ chargement

    private void loadPlayerImages()
    {
        try {
            readPlayerImages();
        } catch ( JSONException e ) {
            throw new IllegalStateException( "perso.json invalide", e );
        }
    }

    private void readPlayerImages() throws JSONException
    {
        JSONObject json = JsonHelper.strToJson( "/jsons/perso.json" );
        String prefix = json.getString( "global_path" );
        JSONArray paths = json.getJSONArray( "paths" );
        for ( int i = 0; i < paths.length(); i++ ) {
            JSONObject obj = paths.getJSONObject( i );
            JSONArray images = obj.getJSONArray( "images" );
            List< Bitmap > list = new ArrayList<>( images.length() );
            for ( int j = 0; j < images.length(); j++ ) {
                list.add( Assets.bitmap( prefix + images.getString( j )));
            }
            playerImgs.put( Direction_helper.strToDir( obj.getString( "direction" )), list );
        }
    }

    private DecorImages images( int id )
    {
        DecorImages cached = cache.get( id );
        if ( cached != null ) return cached;
        try {
            DecorImages images = readImages( id );
            cache.put( id, images );
            return images;
        } catch ( JSONException e ) {
            throw new IllegalStateException( "decorImages.json invalide pour le décor " + id, e );
        }
    }

    private DecorImages readImages( int id ) throws JSONException
    {
        JSONObject json = decorImagesJson.getJSONObject( Integer.toString( id ));
        String path = IMG_PATH + json.getString( "path" );
        DecorImages images = new DecorImages();
        images.background = Assets.bitmap( path + json.getString( "bg" ));
        String fg = json.getString( "fg" );
        if ( ! fg.contains( "null" )) images.front = Assets.bitmap( path + fg );
        if ( json.has( "key" )) {
            JSONObject key = json.getJSONObject( "key" );
            JSONArray anim = key.getJSONArray( "animation" );
            images.key = new ArrayList<>( anim.length() );
            for ( int i = 0; i < anim.length(); i++ ) {
                images.key.add( Assets.bitmap( key.getString( "path" ) + anim.getString( i )));
            }
        }
        return images;
    }
}

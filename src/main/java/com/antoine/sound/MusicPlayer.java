package com.antoine.sound;

import java.io.IOException;

/**
 * <b>Classe de lecteur de musique.</b>
 * Ne lit que les fichiers .wav
 *
 * @author Antoine
 */
public class MusicPlayer extends SoundMaker {

    public interface Command {
        void execute();
    }

    /**Etat pour savoir si la musique est entrain d'être utilisée ou si en pause*/
    volatile boolean playing;
    private Command music_end_cb;

    /**
     * <p>Initialise le Thread et les états</p>
     *
     * @param musicPath le path du fichier .wav
     * @param volume
     */
    public MusicPlayer( String musicPath, float volume, Command music_end_cb ) {
        super(musicPath, volume);
        playing=true;
        using= true;
        this.music_end_cb = music_end_cb;
    }

    /**
     * <p>Teste si le Thread doit se mettre en pause.</p>
     * @see #pause()
     */
    protected void testSleep() {
        synchronized (this) {
            while (!playing && using) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /**
     * <p>Implémente les actions du Thread.</p>
     * @return un Thread implémenté.
     */
    @Override
    protected Thread implementRun(){
        return new Thread(()-> {
            // lecteur muet : on n'enchaîne pas les pistes, cela bouclerait sans fin
            if (isMuted()) return;
            try {
                int totalRead = 0;
                byte bytes[] = new byte[1042];

                while (using && (totalRead = ais.read(bytes, 0, bytes.length)) != -1) {

                    testSleep();
                    if (!using) break;
                    bytes = adjustVolume(bytes);
                    line.write(bytes, 0, totalRead);
                }
                // fin naturelle de la piste : on laisse le tampon se vider
                if (using) line.drain();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                line.close();
                try { ais.close(); } catch (IOException ignored) {}
            }
            // la piste suivante n'est enchaînée que si la musique n'a pas été arrêtée
            if ( using && this.music_end_cb != null )    this.music_end_cb.execute();
        });
    }

    /**
     * <p>met sur pause pour reprise ultérieure.</p>
     */
    public void pause(){
        synchronized(this){
            playing= false;
        }
    }

    /**
     * <p>arrêt définitif.</p>
     * Le Thread sort de la boucle et meurt.
     */
    public void arret()
    {
        using= false;

        synchronized(this){
            playing= false;
            notifyAll();
        }
    }

    /**
     * <p>Redémarre le Thread.</p>
     * @see SoundMaker#play()
     */
    @Override
    public void play(){
        synchronized(this){
            playing = true;
        }
        super.play();
    }

    /**
     * <p>Ajuste le volume de la musique.</p>
     * les bytes sont multipliés par le coefficient volume.
     * @param audioSamples array de byte en train d'être lus.
     * @return une copie dont le volume est ajusté.
     */
    protected byte[] adjustVolume(byte[] audioSamples) {
        byte[] array = new byte[audioSamples.length];

        for (int i = 0; i < audioSamples.length; i+=2) {
            // convert byte pair to int
            short buf1 = audioSamples[i+1];
            short buf2 = audioSamples[i];

            buf1 = (short) ((buf1 & 0xff) << 8);
            buf2 = (short) (buf2 & 0xff);

            short res= (short) (buf1 | buf2);
            res = (short) (res * volume);

            // convert back
            array[i] = (byte) res;
            array[i+1] = (byte) (res >> 8);

        }
        return array;
    }
}

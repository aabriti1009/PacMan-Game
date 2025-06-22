package AudioEngine;

import Media.EAudio;
import Settings.EParam;
import Settings.Settings;

import javax.sound.sampled.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles all the audio-playing aspect of the game.
 */
public class AudioEngine {
    
    private static final ArrayList<AudioEntity> entities = new ArrayList<>();
    
    /**
     * Initializes the AudioEngine class.
     */
    public static void initAudioEngine() {
        try {
            entities.add(new AudioEntity(EAudio.ghost_moving, PlaybackMode.loop));
        } catch (Exception e) {
            System.err.println("\n[-] Couldn't init the audio engine.");
            e.printStackTrace();
        }
    }
    
    
    /**
     * Creates ad plays an AudioEntity on demand, without keeping track of it.
     * @param audio the audio to play
     * @param callback the callback function to call after the AudioEntity has finished playing
     */
    public static void play(EAudio audio, PlaybackMode mode, FunctionCallback callback) {
        if (!(boolean) Settings.get(EParam.audio_enabled)) {
            if (callback != null) {
                callback.callback();
            }
            return;
        }
        try {
            AudioEntity entity = new AudioEntity(audio, mode);
            entities.add(entity);
            
            entity.getClip().addLineListener(new LineListener() {
                AudioEntity a = entity;
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP && a.status != PlaybackStatus.paused && a.status != PlaybackStatus.stopped) {
                        a.getClip().removeLineListener(this);
                        a.getClip().close();
                        entities.remove(a);
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                }
            });
            entity.play();
        }catch (Exception e) {
            System.err.println("\n[-] Couldn't set up the AudioEntity correctly, calling callback immediately.");
            e.printStackTrace();
            if (callback != null) {
                callback.callback();
            }
        }
    }
    
    /**
     * Plays an audio only if its not playing already.
     * @param audio the audio to play
     * @param callback the callback function to call after the AudioEntity has finished playing
     */
    public static void playIfNotAlready(EAudio audio, PlaybackMode mode, FunctionCallback callback) {
        if (!(boolean) Settings.get(EParam.audio_enabled)) {
            if (callback != null) {
                callback.callback();
            }
            return;
        }
        if (!isPlaying(audio))
            play(audio,mode, callback);
    }
    
    /**
     * Tries to restart an existing SFX, otherwise plays a new one.
     * @param audio the audio to play
     * @param mode the mode to play the audio (e.g Loop or regular)
     * @param callback the callback function to call after the audio has finished playing
     */
    public static void restartOrPlay(EAudio audio, PlaybackMode mode, FunctionCallback callback) {
        if (!(boolean) Settings.get(EParam.audio_enabled)) {
            if (callback != null) {
                callback.callback();
            }
            return;
        }
        for (AudioEntity e : entities) {
            if(e.getAudio() == audio) {
                try{
                    e.restart();
                } catch(IOException ex) {
                    ex.printStackTrace();
                    play(audio, mode, callback);
                } finally{
                    return;
                }
            }
        }
        play(audio, mode, callback);
    }
    
    /**
     * Stops a certain audio sfx from playing.
     * @param audio the audio sfx to stop
     */
    public static void stop(EAudio audio){
        for (AudioEntity e : entities){
            if (e.getAudio() == audio){
                try{
                    e.stop();
                } catch (Exception ex) {
                    System.err.println("\n[-] Couldnt stop AudioEntity");
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Pauses all AudioEntities.
     */
    public static void pauseAll() {
        for(AudioEntity e : entities) {
            e.pause();
        }
    }
    
    /**
     * Resumes all AudioEntities.
     */
    public static void resumeALl() {
        for(AudioEntity e : entities) {
            e.resumeAudio();
        }
    }
    
    /**
     * Returns whether an audio sfx is playing.
     * @param audio the audio sfx to check for
     * @return whether it's playing
     */
    public static boolean isPlaying(EAudio audio) {
        for(AudioEntity a : entities) {
            if(a.isPlaying()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sets the volume for all audio entities.
     * @param volume volume level (0.0 to 1.0)
     */
    public static void setVolume(float volume) {
        for(AudioEntity a : entities) {
            a.setVolume(volume);
        }
    }
    
    /**
     * Sets the volume for a specific audio type (music or SFX).
     * @param audio the audio to set volume for
     * @param volume volume level (0.0 to 1.0)
     */
    public static void setVolume(EAudio audio, float volume) {
        for(AudioEntity a : entities) {
            if(a.getAudio() == audio) {
                a.setVolume(volume);
            }
        }
    }
    
    /**
     * Enables or disables all audio.
     * @param enabled true to enable audio, false to disable
     */
    public static void setAudioEnabled(boolean enabled) {
        if (!enabled) {
            pauseAll();
        } else {
            resumeALl();
        }
    }
    
    /**
     * Gets the current volume of an audio entity.
     * @param audio the audio to check
     * @return volume level (0.0 to 1.0)
     */
    public static float getVolume(EAudio audio) {
        for(AudioEntity a : entities) {
            if(a.getAudio() == audio) {
                return a.getVolume();
            }
        }
        return 1.0f;
    }
}

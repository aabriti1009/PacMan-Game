package Entities;

import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Map.Map;
import Media.EAudio;
import Media.EImage;
import Settings.EParam;
import Settings.Settings;

public class LargeFood extends Food {
    public LargeFood(int col, int row, Map map){
        super(col, row, EImage.large_food, map, (int)Settings.get(EParam.large_food_points));
    }

    @Override
    public void onCollision(Entity e) {
        super.onCollision(e);
        
        AudioEngine.playIfNotAlready(EAudio.large_food, PlaybackMode.regular, null);
        
        EntityManager.makeGhostVulnerable(true);
        
        AudioEngine.stop(EAudio.ghost_vulnerable_end);
        AudioEngine.restartOrPlay(EAudio.ghost_vulnerable, PlaybackMode.regular, new FunctionCallback() {
            @Override
            public void callback() {
                System.out.println("Almost finished Ghost vulnerability");
                AudioEngine.play(EAudio.ghost_vulnerable_end, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        System.out.println("Finished ghost vulnerability");
                        EntityManager.makeGhostVulnerable(false);
                    }
                });
                
            }
        });
    }
}

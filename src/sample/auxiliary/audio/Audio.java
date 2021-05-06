package sample.auxiliary.audio;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum Audio {
    bullet_hit_1("bullet_hit_1.wav"),
    bullet_hit_2("bullet_hit_2.wav"),
    bullet_hit_steel("bullet_hit_steel.wav"),
    bullet_shot("bullet_shot.wav"),
    explosion_1("explosion_1.wav"),
    explosion_2("explosion_2.wav"),
    star("star.wav"),
    get_bonus("getBonus.wav"),
    bonus_life("bonus.life.wav"),
    bonus_grenade("grenade.wav"),
    enemy_move("enemy.move.wav"),
    player_move("player.move.wav"),
    total_score_tick("total.score.tick.wav"),
    menu("menu.wav"),
    gama_over("game_over.wav"),
    stage_start("stage_start.wav");
    String url;

    Audio(String url) {
        this.url = "resources/audio/" + url;
    }

    public void play() {
        AudioStream as;
        try {
            InputStream resourceAsStream = new FileInputStream(url);
            as = new AudioStream(resourceAsStream);
            AudioPlayer.player.start(as);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }
}

package sample.auxiliary.state;

import sample.auxiliary.*;
import sample.auxiliary.audio.Audio;
import sample.auxiliary.audio.MediaPlayer;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.content.player.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MenuState extends GameState {
    private final BufferedImage title = ResourceLoader.loadImage("images/title.png");
    private Animation tank;
    private static final int CHOICE_TOP_Y = Constant.ELEMENT_SIZE * 7 - 24;
    private int ch_x, ch_y;
    private int[] choices_y;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        progress.set("hearts", "4");
        init();
    }

    @Override
    public void init() {
        //迫不得已将这两句话写到这里
        ElementBean.init();
        for(Player player : gsm.getPlayers()) {
            player.initLevel();
        }
        gsm.getHome().born();

        progress = Progress.getInstance();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(2 * Constant.ELEMENT_SIZE, 0, Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE),
                Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        tank = new Animation(new BufferedImage[]{sheet.getSprite(0), sheet.getSprite(1)}, 25);
        tank.start();
        ch_x = Constant.ELEMENT_SIZE * 4;
        ch_y = CHOICE_TOP_Y;
        choices_y = new int[3];
        for(int i = 0; i < choices_y.length; ++i) {
            choices_y[i] = CHOICE_TOP_Y + Constant.ELEMENT_SIZE * i;
        }
//        new MediaPlayer(Audio.menu.getUrl()).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("menu");
    }

    @Override
    public void stateAction() {
        if(Keys.PLAY2_UP.use()) {
            ch_y = Math.max(ch_y - Constant.ELEMENT_SIZE, CHOICE_TOP_Y);
            System.out.println("up");
        }
        if(Keys.PLAY2_DOWN.use()) {
            System.out.println("down");
            ch_y = Math.min(ch_y + Constant.ELEMENT_SIZE, CHOICE_TOP_Y + Constant.ELEMENT_SIZE * 2);
        }
        if(Keys.ENTER.use()) {
            if(ch_y == choices_y[0]) {
                progress.set("playerNum", "1");
                progress.set("hearts", "5");
                gsm.setGameState(STATE.STAGE);
            }else if(ch_y == choices_y[1]){
                progress.set("playerNum", "2");
                gsm.setGameState(STATE.STAGE);
            }else if(ch_y == choices_y[2]){
                gsm.setGameState(STATE.CONSTRUCTION);
            }
            progress.store();
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(font.deriveFont(15f));
        int tx = Constant.ELEMENT_SIZE * 4 + 17, ty = Constant.ELEMENT_SIZE * 2;

        g.drawString("HI-" + progress.get("highScore"), Constant.ELEMENT_SIZE + 17, ty);
        ty += Constant.ELEMENT_SIZE;

        g.drawImage(title, Constant.ELEMENT_SIZE + 17, ty, Constant.ELEMENT_SIZE * 12, Constant.ELEMENT_SIZE * 3 / 2, null);

        g.setFont(font.deriveFont(15f));
        ty += Constant.ELEMENT_SIZE * 4;
        //draw options
        g.setFont(font.deriveFont(20f));
        g.drawImage(tank.getSprite(), ch_x, ch_y, 34, 34, null);
        g.drawString("1 PLAYER", tx + 34, ty);
        g.drawString("2 PLAYERS", tx + 34, ty + Constant.ELEMENT_SIZE);
        g.drawString("CONSTRUCTION", tx + 34, ty + Constant.ELEMENT_SIZE * 2);
        tank.update();
    }
}

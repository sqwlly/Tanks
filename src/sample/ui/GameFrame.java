package sample.ui;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.TextureAtlas;
import sample.content.GameContent;
import sample.content.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    TextureAtlas textureAtlas;

    public GameFrame() {
        this.setTitle("Tanks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        //窗体居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension size = new Dimension(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        int width = toolkit.getScreenSize().width;
        int height = toolkit.getScreenSize().height;
        this.setBounds((int) (width - size.getWidth()) / 2,
                (int) (height - size.getHeight()) / 3, (int) size.getWidth(), (int) size.getHeight());
        this.init();
    }

    //初始化游戏内容
    public void init() {
        textureAtlas = new TextureAtlas(Constant.ATLAS_FILE_NAME);
        GameContent gameContent = new GameContent(textureAtlas);
        GamePanel panel = new GamePanel(gameContent);
//        GamePanel panel = new GamePanel(new Player(textureAtlas));
        this.add(panel);
        this.setVisible(true);
        CommonUtils.task(5, () -> {
            panel.repaint();

        });
        //玩家键盘监听
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Keys.remove(e.getKeyCode());
            }
        });
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}

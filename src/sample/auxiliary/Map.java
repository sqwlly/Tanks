package sample.auxiliary;

import sample.content.player.Player;
import sample.content.substance.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Map {
    private final int width = 26, height = 26;
    private int[][] map;
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map(String file) {
        BufferedReader br = ResourceLoader.loadMapConfig(file);
        String delimiters = "";
        try {
            map = new int[width][height];

            for(int i = 0; i < height; ++i) {
                String s = br.readLine();;
                if(s == null) break;
                String[] msg = s.split(delimiters);
                for(int j = 0; j < width && j < msg.length; ++j) {
                    System.out.print(msg[j] + " ");
                    map[i][j] = Integer.parseInt(msg[j]);
                }
                System.out.println();
            }
            System.out.println("--------------");
            loadMap();
        }catch (IOException ignored) {

        }
    }

    public void loadMap() {
//        for(int i = 0; i < 3; ++i) {
//            ElementBean.Substance.getService().add(new Tile(5 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2,
//                    11 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2 * (i + 1), 1));
//        }
//        for(int i = 0; i < 3; ++i) {
//            ElementBean.Substance.getService().add(new Tile(7 * Constant.ELEMENT_SIZE,
//                    11 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2 * (i + 1), 1));
//        }
//        for(int i = 0; i < 2; ++i) {
//            ElementBean.Substance.getService().add(new Tile(5 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2 * (i + 2),
//                    11 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2, 1));
//        }
        ElementBean.Substance.getService().add(new Home(6 * Constant.ELEMENT_SIZE, 12 * Constant.ELEMENT_SIZE));
        playerInit();
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                getEntity(map[i][j], j * Constant.ELEMENT_SIZE / 2, i * Constant.ELEMENT_SIZE / 2);
            }
        }
    }

    public void getEntity(int type, int x, int y) {
        switch (type) {
            case 1:
//                for(int i = 0; i < 2; ++i) {
//                    for(int j = 0; j < 2; ++j) {
//                        ElementBean.Substance.getService().add(new Tile(x + i * Constant.ELEMENT_SIZE / 2, y + j * Constant.ELEMENT_SIZE / 2, 1));
//                    }
//                }
                ElementBean.Substance.getService().add(new Steel(x, y));

                break;
            case 2:
//                for(int i = 0; i < 2; ++i) {
//                    ElementBean.Substance.getService().add(new Steel(x + i * Constant.ELEMENT_SIZE / 2, y));
//                }
                ElementBean.Substance.getService().add(new Tile(x, y, 1));
                break;
            case 3:
                ElementBean.Substance.getService().add(new Grass(x, y));
                break;
            case 5:
                ElementBean.Substance.getService().add(new Water(x, y));
                break;
        }
    }

    public void playerInit() {
        player = new Player(4 * Constant.ELEMENT_SIZE + Constant.ELEMENT_SIZE / 2, 12 * Constant.ELEMENT_SIZE);
        ElementBean.Player.getService().add(player);
        ElementBean.Substance.getService().add(player.getBorn());
        ElementBean.Substance.getService().add(player.getInvincible());

    }
}

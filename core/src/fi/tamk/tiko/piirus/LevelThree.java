package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anu on 8.3.2018.
 */


public class LevelThree {
    private PiirusMain game;


    public static int dots = 10;       //how many dots there are in the level

    //Dots
    public static Array<Dot> dotsArray;


    public LevelThree(PiirusMain g) {

        game = g;


        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            if (i == 0) {
                visible = true;
            } else {
                visible = false;
            }
            switch (i) {
                case 0:
                    x = game.WORLD_WIDTH * 0.5875f;
                    y = game.WORLD_HEIGHT * 0.84f;
                    break;
                case 1:
                    x = game.WORLD_WIDTH * 0.5625f;
                    y = game.WORLD_HEIGHT * 0.52f;
                    break;
                case 2:
                    x = game.WORLD_WIDTH * 0.395f;
                    y = game.WORLD_HEIGHT * 0.214f;
                    break;
                case 3:
                    x = game.WORLD_WIDTH * 0.5625f;
                    y = game.WORLD_HEIGHT * 0.52f;
                    break;
                case 4:
                    x = game.WORLD_WIDTH * 0.8f;
                    y = game.WORLD_HEIGHT * 0.65f;
                    break;
                case 5:
                    x = game.WORLD_WIDTH * 0.5625f;
                    y = game.WORLD_HEIGHT * 0.52f;
                    break;
                case 6:
                    x = game.WORLD_WIDTH * 0.3325f;
                    y = game.WORLD_HEIGHT * 0.618f;
                    break;
                case 7:
                    x = game.WORLD_WIDTH * 0.5625f;
                    y = game.WORLD_HEIGHT * 0.52f;
                    break;
                case 8:
                    x = game.WORLD_WIDTH * 0.75f;
                    y = game.WORLD_HEIGHT * 0.216f;
                    break;
                case 9:
                    x = game.WORLD_WIDTH * 0.5625f;
                    y = game.WORLD_HEIGHT * 0.52f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(game.dotSize);
        }
    }
}
package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anu on 8.3.2018.
 */


public class LevelSix {
    private PiirusMain game;

    public static Texture finishPic;      //the beautified picture at the end

    public static int dots = 14;       //how many dots there are in the level

    //Dots
    public static Array<Dot> dotsArray;


    public LevelSix(PiirusMain g) {

        game = g;

        finishPic = new Texture(Gdx.files.internal("clock.png"));

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
                    x = game.WORLD_WIDTH * 0.32f;
                    y = game.WORLD_HEIGHT * 0.052f;
                    break;
                case 1:
                    x = game.WORLD_WIDTH * 0.335f;
                    y = game.WORLD_HEIGHT * 0.113f;
                    break;
                case 2:
                    x = game.WORLD_WIDTH * 0.2575f;
                    y = game.WORLD_HEIGHT * 0.391f;
                    break;
                case 3:
                    x = game.WORLD_WIDTH * 0.303f;
                    y = game.WORLD_HEIGHT * 0.627f;
                    break;
                case 4:
                    x = game.WORLD_WIDTH * 0.261f;
                    y = game.WORLD_HEIGHT * 0.705f;
                    break;
                case 5:
                    x = game.WORLD_WIDTH * 0.347f;
                    y = game.WORLD_HEIGHT * 0.865f;
                    break;
                case 6:
                    x = game.WORLD_WIDTH * 0.536f;
                    y = game.WORLD_HEIGHT * 0.989f;
                    break;
                case 7:
                    x = game.WORLD_WIDTH * 0.721f;
                    y = game.WORLD_HEIGHT * 0.841f;
                    break;
                case 8:
                    x = game.WORLD_WIDTH * 0.766f;
                    y = game.WORLD_HEIGHT * 0.681f;
                    break;
                case 9:
                    x = game.WORLD_WIDTH * 0.72f;
                    y = game.WORLD_HEIGHT * 0.615f;
                    break;
                case 10:
                    x = game.WORLD_WIDTH * 0.755f;
                    y = game.WORLD_HEIGHT * 0.385f;
                    break;
                case 11:
                    x = game.WORLD_WIDTH * 0.648f;
                    y = game.WORLD_HEIGHT * 0.02f;
                    break;
                case 12:
                    x = game.WORLD_WIDTH * 0.501f;
                    y = game.WORLD_HEIGHT * 0.042f;
                    break;
                case 13:
                    x = game.WORLD_WIDTH * 0.32f;
                    y = game.WORLD_HEIGHT * 0.052f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(game.dotSize);
        }
    }
}
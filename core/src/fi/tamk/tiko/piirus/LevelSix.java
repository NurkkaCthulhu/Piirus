package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;
/**
 * Level Six's dot stats.
 *
 * Holds the information of the level: how many dots there are and where they are. Easy to modify without breaking the base Level class.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */
class LevelSix {
    //how many dots there are in the level
    static int dots = 14;
    //Dots
    static Array<Dot> dotsArray;


    /**
     * Constructor fot level six.
     * @param g the main game object(can be used to call all sorts of things)
     */
    LevelSix(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.32f;
                    y = g.WORLD_HEIGHT * 0.052f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.335f;
                    y = g.WORLD_HEIGHT * 0.113f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.2575f;
                    y = g.WORLD_HEIGHT * 0.391f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.303f;
                    y = g.WORLD_HEIGHT * 0.627f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.261f;
                    y = g.WORLD_HEIGHT * 0.705f;
                    break;
                case 5:
                    x = g.WORLD_WIDTH * 0.347f;
                    y = g.WORLD_HEIGHT * 0.865f;
                    break;
                case 6:
                    x = g.WORLD_WIDTH * 0.536f;
                    y = g.WORLD_HEIGHT * 0.989f;
                    break;
                case 7:
                    x = g.WORLD_WIDTH * 0.721f;
                    y = g.WORLD_HEIGHT * 0.841f;
                    break;
                case 8:
                    x = g.WORLD_WIDTH * 0.766f;
                    y = g.WORLD_HEIGHT * 0.681f;
                    break;
                case 9:
                    x = g.WORLD_WIDTH * 0.72f;
                    y = g.WORLD_HEIGHT * 0.615f;
                    break;
                case 10:
                    x = g.WORLD_WIDTH * 0.755f;
                    y = g.WORLD_HEIGHT * 0.385f;
                    break;
                case 11:
                    x = g.WORLD_WIDTH * 0.648f;
                    y = g.WORLD_HEIGHT * 0.02f;
                    break;
                case 12:
                    x = g.WORLD_WIDTH * 0.501f;
                    y = g.WORLD_HEIGHT * 0.042f;
                    break;
                case 13:
                    x = g.WORLD_WIDTH * 0.32f;
                    y = g.WORLD_HEIGHT * 0.052f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            //dot's size is the one that the user inputted in settings
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}
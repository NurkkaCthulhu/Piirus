package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;
/**
 * Level Two's dot stats.
 *
 * Holds the information of the level: how many dots there are and where they are. Easy to modify without breaking the base Level class.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */
class LevelTwo {
    //how many dots there are in the level
    static int dots = 7;
    //Dots
    static Array<Dot> dotsArray;

    /**
     * Constructor fot level two.
     * @param g the main game object(can be used to call all sorts of things)
     */
    LevelTwo(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.5f;
                    y = g.WORLD_HEIGHT * 0.214f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.3125f;
                    y = g.WORLD_HEIGHT * 0.33f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.3125f;
                    y = g.WORLD_HEIGHT * 0.664f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.5f;
                    y = g.WORLD_HEIGHT * 0.8f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.625f;
                    y = g.WORLD_HEIGHT * 0.664f;
                    break;
                case 5:
                    x = g.WORLD_WIDTH * 0.625f;
                    y = g.WORLD_HEIGHT * 0.33f;
                    break;
                case 6:
                    x = g.WORLD_WIDTH * 0.5f;
                    y = g.WORLD_HEIGHT * 0.214f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            //dot's size is the one that the user inputted in settings
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}
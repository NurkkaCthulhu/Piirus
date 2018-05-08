package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;

class LevelOne {
    static int dots = 5;       //how many dots there are in the level
    //Dots
    static Array<Dot> dotsArray;

    LevelOne(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.6875f;
                    y = g.WORLD_HEIGHT * 0.75f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.325f;
                    y = g.WORLD_HEIGHT * 0.775f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.225f;
                    y = g.WORLD_HEIGHT * 0.375f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.7f;
                    y = g.WORLD_HEIGHT * 0.325f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.6875f;
                    y = g.WORLD_HEIGHT * 0.75f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}
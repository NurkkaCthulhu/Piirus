package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;

class LevelThree {
    static int dots = 10;       //how many dots there are in the level
    //Dots
    static Array<Dot> dotsArray;

    LevelThree(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.5875f;
                    y = g.WORLD_HEIGHT * 0.84f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.5625f;
                    y = g.WORLD_HEIGHT * 0.52f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.395f;
                    y = g.WORLD_HEIGHT * 0.214f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.5625f;
                    y = g.WORLD_HEIGHT * 0.52f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.8f;
                    y = g.WORLD_HEIGHT * 0.65f;
                    break;
                case 5:
                    x = g.WORLD_WIDTH * 0.5625f;
                    y = g.WORLD_HEIGHT * 0.52f;
                    break;
                case 6:
                    x = g.WORLD_WIDTH * 0.3325f;
                    y = g.WORLD_HEIGHT * 0.618f;
                    break;
                case 7:
                    x = g.WORLD_WIDTH * 0.5625f;
                    y = g.WORLD_HEIGHT * 0.52f;
                    break;
                case 8:
                    x = g.WORLD_WIDTH * 0.75f;
                    y = g.WORLD_HEIGHT * 0.216f;
                    break;
                case 9:
                    x = g.WORLD_WIDTH * 0.5625f;
                    y = g.WORLD_HEIGHT * 0.52f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}
package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Anu on 8.3.2018.
 */

public class Cursor {

    private static boolean moved;

    public static void joystickMoving(PiirusMain game, Rectangle rect, float penSize){
        Vector2 positionVector = new Vector2(game.WORLD_WIDTH/2 + (game.getAdjustedY()*10) * game.WORLD_WIDTH/2* Gdx.graphics.getDeltaTime(),
                game.WORLD_HEIGHT/2 + (game.getAdjustedZ()*10) * game.WORLD_HEIGHT/2* Gdx.graphics.getDeltaTime());
        moved = false;

        positionVector.x = game.WORLD_WIDTH/2 + (game.getAdjustedY()) * game.WORLD_WIDTH/2* Gdx.graphics.getDeltaTime();
        positionVector.y = game.WORLD_HEIGHT/2 + (game.getAdjustedZ()) * game.WORLD_HEIGHT/2* Gdx.graphics.getDeltaTime();

        if(rect.y < game.WORLD_HEIGHT - penSize && rect.y > 0){
            rect.setY(rect.y + positionVector.y);
            moved = true;
        }

        if(rect.y > game.WORLD_HEIGHT - penSize){
            rect.setY(game.WORLD_HEIGHT - penSize - 0.01f);
        }

        if(rect.y < 0){
            rect.setY(0.01f);
        }

        if(rect.x < game.WORLD_WIDTH - penSize && rect.x > 0){
            rect.setX(rect.x + positionVector.x);
            moved = true;
        }

        if(rect.x > game.WORLD_WIDTH - penSize){
            rect.setX(game.WORLD_WIDTH - penSize - 0.01f);
        }

        if(rect.x < 0){
            rect.setX(0.01f);
        }

        rect.setPosition(positionVector);
    }
    public static boolean isPenMoved() {
        return moved;
    }

}

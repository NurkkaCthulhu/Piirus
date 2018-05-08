package fi.tamk.tiko.piirus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Cursor has all the things you need for moving the pencil in game.
 *
 * Uses a few of the methods from calibration screen modified. The movement in cursor is actually a tiny bit more
 * sensitive than user calibrated, makes them happier.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class Cursor {

    private static PiirusMain game;
    private static float penSize;
    private static Rectangle rect;

    /**
     * The constructor for the cursor.
     * @param g the main game object(can be used to call all sorts of things)
     * @param p pencil's size
     * @param rectangle pencil's rectangle that is moved
     */
    public Cursor (PiirusMain g, float p, Rectangle rectangle) {
        game = g;
        penSize = p;
        rect = rectangle;
    }

    /**
     * Moves the pencil.
     */
    public static void move() {
        //basic speeds of the pencil
        float horSpeed = 1.5f;
        float verSpeed = 0.6f;
        //check the cursor's position in Y&X axis and multiply the movement speed by the given calibration values
        if (deadzoneInput().y > 0) {
            rect.y = game.WORLD_HEIGHT/2 + deadzoneInput().y*(verSpeed + game.upYMultiplier);
        } else {
            rect.y = game.WORLD_HEIGHT/2 + deadzoneInput().y*(verSpeed + game.downYMultiplier);
        }
        if(deadzoneInput().x > 0) {
            rect.x = game.WORLD_WIDTH/2 + deadzoneInput().x * (horSpeed + game.rightXMultiplier);
        } else {
            rect.x = game.WORLD_WIDTH/2 + deadzoneInput().x * (horSpeed + game.leftXMultiplier);
        }
        //make the cursor be within the calibration bounds
        stayWithinBounds(rect);
        //used to help the cursor be more stable, average values are used to eliminate the stuttering the cursor has
        if (game.arraySpot == game.arrayLength-1) {
            game.arraySpot = 0;
        } else {
            game.arraySpot++;
        }
    }

    /**
     * Check that the pencil cursor stays within the screen
     * @param rect the pencil's rectangle
     */
    private static void stayWithinBounds(Rectangle rect) {
        //minimum and maximum rectangle position values
        float maxYPercent = 0.99f;
        float minYPercent = 0.01f;
        float maxXPercent = 0.99f;
        float minXPercent = 0.01f;

        if(rect.y > game.WORLD_HEIGHT*maxYPercent - penSize){
            rect.setY(game.WORLD_HEIGHT*maxYPercent - penSize - 0.01f);
        }

        if(rect.y < game.WORLD_HEIGHT*minYPercent){
            rect.setY(game.WORLD_HEIGHT*minYPercent - 0.01f);
        }

        if(rect.x > game.WORLD_WIDTH*maxXPercent - penSize){
            rect.setX(game.WORLD_WIDTH*maxXPercent - penSize - 0.01f);
        }

        if(rect.x < game.WORLD_WIDTH*minXPercent){
            rect.setX(game.WORLD_WIDTH*minXPercent + 0.01f);
        }
    }

    /**
     * Return the position of the cross hair.
     *
     * We have three vectors: the vanilla input vector, deadzoneVector for how big of a dead zone we want
     * and positionVector for the cross hair movement. Vector.nor() normalizes the vector instead of just returning a normalized version of the vector, so some creativity
     * had to be used to make the dead zone work.
     * @return positionVector for the cross hair movement
     */
    public static Vector2 deadzoneInput() {
        float deadzone = 0.2f;

        //averaged input vector
        Vector2 stickInput = new Vector2(game.getAverageX(),game.getAverageY());

        //length of the deadzone vector
        Vector2 deadzoneVector = new Vector2(game.getAverageX(),game.getAverageY());
        deadzoneVector.nor().scl(deadzone, deadzone);

        //the position of the cursor
        Vector2 positionVector = new Vector2(game.getAverageX(),game.getAverageY());
        positionVector.sub(deadzoneVector);

        if(stickInput.len() < deadzone) {
            positionVector = new Vector2(0,0);
        }

        return positionVector;
    }

    /**
     * Tells if the pen is moved and the drawing line can be expanded (currently always true, the goals is that someday it will have some real code)
     * @return whether the pen is drawing currently
     */
    public static boolean isPenMoved() {
        return true;
    }
}

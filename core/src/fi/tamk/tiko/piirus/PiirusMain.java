package fi.tamk.tiko.piirus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PiirusMain extends Game {
    //world camera specs
    public int WORLD_WIDTH;
    public int WORLD_HEIGHT;
    //font camera specs
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public float savedX, savedY, savedZ;
    SpriteBatch batch;

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        WORLD_WIDTH = 8;
        WORLD_HEIGHT = 4;

        SCREEN_WIDTH = WORLD_WIDTH * 100;
        SCREEN_HEIGHT = WORLD_HEIGHT * 100;

        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void letsFigurePositionForMePlease(Rectangle rect, float speed) {
        //float speed = 20f;

        //Moving
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            rect.setX(rect.x + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            rect.setX(rect.x - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            rect.setY(rect.y + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            rect.setY(rect.y - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        //Size
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rect.setWidth(rect.width - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rect.setWidth(rect.width + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            rect.setHeight(rect.height + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            rect.setHeight(rect.height - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }
    }

    public float getAdjustedY() {
        //adjustedY = 0;
        if(savedY < 0) {
            //Gdx.app.log("AdjustedY FIRST", "" + (Gdx.input.getAccelerometerY() + Math.abs(savedY)));
            return (Gdx.input.getAccelerometerY() + Math.abs(savedY));
        } else {
            //Gdx.app.log("AdjustedY SECOND", "" + (Gdx.input.getAccelerometerY() - savedY));
            return (Gdx.input.getAccelerometerY() - savedY);
        }
    }

    public float getAdjustedZ() {
        //adjustedZ = 0;
        if(savedZ < 0) {
            //Gdx.app.log("AdjustedZ FIRST", "" + (Gdx.input.getAccelerometerZ() + Math.abs(savedZ)));
            return (Gdx.input.getAccelerometerZ() + Math.abs(savedZ));
        } else {
            //Gdx.app.log("AdjustedZ SECOND", "" + (Gdx.input.getAccelerometerZ() - savedZ));
            return (Gdx.input.getAccelerometerZ() - savedZ);
        }
    }

    public void calibrate(){
        savedX = Gdx.input.getAccelerometerX();
        savedY = Gdx.input.getAccelerometerY();
        savedZ = Gdx.input.getAccelerometerZ();
    }
}
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
    //Käyttää 800x400, koska ei tarvitse Box2D ja todennäköisesti yhdellä kameralla voidaan
    //fontit tulostaa. Thoughts?
    public int SCREEN_WIDTH = 800;
    public int SCREEN_HEIGHT = 400;
    private MainMenu mainMenu;
    SpriteBatch batch;

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void letsFigurePositionForMePlease(Rectangle rect) {
        float speed = 20f;

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
}
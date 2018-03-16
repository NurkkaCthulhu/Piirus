package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Anu on 8.3.2018.
 */

/**
 * Nothing special, basic movement
 * 2 different movesets, toggleable?
 * only one works at the moment.
 *
 * For joystick movement maybe need to do 8 by 4 camera?
 * or coordinates * 100?
 */

public class LevelOne implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture penTexture;
    private Texture penDot;
    private Rectangle penRectangle;
    private Float penSpeed = 2f;
    private ArrayList<Rectangle> penDots;

    public LevelOne(PiirusMain g){
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        penTexture = new Texture(Gdx.files.internal("pen.png"));
        penDot = new Texture(Gdx.files.internal("dot.png"));
        penRectangle = new Rectangle(camera.viewportWidth / 2, camera.viewportHeight / 2, penTexture.getWidth(), penTexture.getHeight());
        penDots = new ArrayList<Rectangle>();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.4f, 0.4f,0.4f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        penDraw();
        batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width, penRectangle.height);
        batch.end();
        topdownMoving(penRectangle);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    //99.99% mahis ett liikkumiset siirtyy mainiin. Testailen täällä :d
    private void joystickMoving(Rectangle rect){

    }

    private void topdownMoving(Rectangle rect){
        boolean moved = false;
        if(Gdx.input.getAccelerometerY() > 1){
            rect.setX(rect.x + penSpeed);
            moved = true;
        }

        if(Gdx.input.getAccelerometerY() < -1){
            rect.setX(rect.x - penSpeed);
            moved = true;
        }

        if(Gdx.input.getAccelerometerZ() > 6){
            rect.setY(rect.y + penSpeed);
            moved = true;
        }

        if(Gdx.input.getAccelerometerZ() < 4){
            rect.setY(rect.y - penSpeed);
            moved = true;
        }

        if(moved){
            penDots.add(new Rectangle(rect.x, rect.y, 10, 10));
        }

        Gdx.app.log("ForgotHowToAccelo", "Y=" + Gdx.input.getAccelerometerY() + "||X=" + Gdx.input.getAccelerometerX() + "||Z=" + Gdx.input.getAccelerometerZ());
        //z = y
        //y = x
    }

    private void penDraw(){
        if(!penDots.isEmpty()){
            for(Rectangle r : penDots){
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }
}

package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import org.w3c.dom.css.Rect;

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
    private Texture buttonTexture;
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Float penSpeed = 2f;
    private Float penSize = 10f;
    private ArrayList<Rectangle> penDots;
    private Vector3 joyStickVector;

    public LevelOne(PiirusMain g){
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        penTexture = new Texture(Gdx.files.internal("pen.png"));
        penDot = new Texture(Gdx.files.internal("dot.png"));
        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));

        penRectangle = new Rectangle(camera.viewportWidth / 2, camera.viewportHeight / 2, penTexture.getWidth(), penTexture.getHeight());
        penSizeMinusRectangle = new Rectangle(0, 0, 60, 60);
        penSizePlusRectangle = new Rectangle(100, 0, 60, 60);

        penDots = new ArrayList<Rectangle>();
        joyStickVector = new Vector3();
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
        batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height); //plus ja miinus piirtäminen, ei tekstiä
        batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);
        batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width, penRectangle.height);
        batch.end();
        topdownMoving(penRectangle, joyStickVector);
        //joystickMoving(penRectangle, joyStickVector);
        buttonsTouched();
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
        penDot.dispose();
        penTexture.dispose();
    }

    private void buttonsTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(penSizePlusRectangle.contains(touchPos.x, touchPos.y)){
                penSize = penSize + 0.1f;
            }
            if(penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && penSize > 0){
                penSize = penSize - 0.1f;
            }
        }
    }

    //99.99% mahis ett liikkumiset siirtyy mainiin. Testailen täällä :d
    private void joystickMoving(Rectangle rect, Vector3 movement){

    }

    private void topdownMoving(Rectangle rect, Vector3 movement){
        /* Pitkä kommentti blokki on 8-diagonal movement, ei kommentti fluent topdown
        boolean moved = false;
        if(game.getAdjustedY() > 1 && rect.x < game.SCREEN_WIDTH - penSize){
            rect.setX(rect.x + penSpeed);
            moved = true;
        }

        if(game.getAdjustedY() < -1 && rect.x > 0){
            rect.setX(rect.x - penSpeed);
            moved = true;
        }

        if(game.getAdjustedZ() > 1 && rect.y < game.SCREEN_HEIGHT - penSize){
            rect.setY(rect.y + penSpeed);
            moved = true;
        }

        if(game.getAdjustedZ() < -1 && rect.y > 0){
            rect.setY(rect.y - penSpeed);
            moved = true;
        }

        if(moved){
            addPaint(rect);
        }

        Gdx.app.log("ForgotHowToAccelo", "Y=" + Gdx.input.getAccelerometerY() + "||X=" + Gdx.input.getAccelerometerX() + "||Z=" + Gdx.input.getAccelerometerZ());
        Gdx.app.log("AddjustedAccelo", "Y=" + game.getAdjustedY() + "||Z=" + game.getAdjustedZ());
        //z = y
        //y = x
        */

        boolean moved = false;
        movement.x = game.getAdjustedY();
        movement.y = game.getAdjustedZ();

        if(rect.y < game.SCREEN_HEIGHT - penSize && rect.y > 0){
            rect.setY(rect.y + movement.y);
            moved = true;
        }

        if(rect.y > game.SCREEN_HEIGHT - penSize){
            rect.setY(game.SCREEN_HEIGHT - penSize - 1);
        }

        if(rect.y < 0){
            rect.setY(1);
        }

        if(rect.x < game.SCREEN_WIDTH - penSize && rect.x > 0){
            rect.setX(rect.x + movement.x);
            moved = true;
        }

        if(rect.x > game.SCREEN_WIDTH - penSize){
            rect.setX(game.SCREEN_WIDTH - penSize - 1);
        }

        if(rect.x < 0){
            rect.setX(1);
        }

        if(moved){
            addPaint(rect);
        }
    }

    private void penDraw(){
        if(!penDots.isEmpty()){
            for(Rectangle r : penDots){
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    private void addPaint(Rectangle rect){
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
    }

    private void clearLine(){
        penDots.clear();
    }
}

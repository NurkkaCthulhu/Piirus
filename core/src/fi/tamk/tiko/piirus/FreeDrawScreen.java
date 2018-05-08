package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class FreeDrawScreen extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture backgroundTexture;
    private Rectangle menuRect;
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Rectangle clearRectanlge;
    private BitmapFont font;
    private ArrayList<Rectangle> penDots;
    private Float penSize = 0.1f;
    private Texture penTexture;
    private Texture penDot;
    private Vector3 joyStickVector;

    FreeDrawScreen(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        backgroundTexture = new Texture(Gdx.files.internal("levelbg.png"));
        penTexture = new Texture(Gdx.files.internal("pen.png"), true);
        penDot = new Texture(Gdx.files.internal("dot.png"));
        penTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        menuRect = new Rectangle(0,4, 0.4f, 0.4f);
        menuRect.setPosition(0, menuRect.y - menuRect.height);
        penRectangle = new Rectangle(game.WORLD_WIDTH / 2, game.WORLD_HEIGHT / 2, 0.2f, 0.2f);
        penSizeMinusRectangle = new Rectangle(0, 0, 0.6f, 0.6f);
        penSizePlusRectangle = new Rectangle(1, 0, 0.6f, 0.6f);
        clearRectanlge = new Rectangle(camera.viewportWidth - 0.6f, 0, 0.6f, 0.6f);

        joyStickVector = new Vector3();

        penDots = new ArrayList<Rectangle>();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f,0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture,0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);

        penDraw();
        batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width*6, penRectangle.height*6);
        batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
        batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);

        batch.draw(buttonTexture, clearRectanlge.x, clearRectanlge.y, clearRectanlge.width, clearRectanlge.height);


        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "-", penSizeMinusRectangle.x*100 + 20, (penSizeMinusRectangle.y + penSizeMinusRectangle.getHeight() / 2)*100 );
        font.draw(batch, "+", penSizePlusRectangle.x*100 + 20, (penSizePlusRectangle.y + penSizePlusRectangle.getHeight() / 2)*100 );
        font.draw(batch, "Tyh.", clearRectanlge.x*100, (clearRectanlge.y + clearRectanlge.getHeight() / 2)*100 );
        batch.end();

        topdownMoving(penRectangle, joyStickVector);
        holdButtonTouched();

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
        buttonTexture.dispose();
        backgroundTexture.dispose();
        font.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y)){
            dispose();
            game.setScreen(new MainMenu(game));
        } else if (clearRectanlge.contains(touchPos.x, touchPos.y)) {
            clearLine();
        }
        return false;
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
        movement.x = game.getAdjustedY()/100;
        movement.y = game.getAdjustedZ()/100;

        if(rect.y < game.WORLD_HEIGHT - penSize && rect.y > 0){
            rect.setY(rect.y + movement.y);
            moved = true;
        }

        if(rect.y > game.WORLD_HEIGHT - penSize){
            rect.setY(game.WORLD_HEIGHT - penSize - 0.01f);
        }

        if(rect.y < 0){
            rect.setY(0.01f);
        }

        if(rect.x < game.WORLD_WIDTH - penSize && rect.x > 0){
            rect.setX(rect.x + movement.x);
            moved = true;
        }

        if(rect.x > game.WORLD_WIDTH - penSize){
            rect.setX(game.WORLD_WIDTH - penSize - 0.01f);
        }

        if(rect.x < 0){
            rect.setX(0.01f);
        }

        if(moved){
            addPaint(rect);
        }
    }

    private void addPaint(Rectangle rect){
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
    }

    private void penDraw(){
        if(!penDots.isEmpty()){
            for(Rectangle r : penDots){
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    private void clearLine(){
        penDots.clear();
    }

    private void holdButtonTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(penSizePlusRectangle.contains(touchPos.x, touchPos.y)){
                penSize = penSize + 0.01f;
            }
            if(penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && penSize > 0.01f){
                penSize = penSize - 0.01f;
            }
        }
    }

}

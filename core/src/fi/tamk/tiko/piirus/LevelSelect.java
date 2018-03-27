package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anu on 8.3.2018.
 */

public class LevelSelect implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture backgroundTexture;
    private Rectangle menuRect;
    private Rectangle levelOneRect;
    private BitmapFont font;
    private boolean waitIsOver = false;
    private float waitTimer = 0f;

    public LevelSelect(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        levelOneRect = new Rectangle(4, 2, 0.5f, 0.5f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        waitTimer = waitTimer + delta;
        if(waitTimer >= 1){
            waitIsOver = true;
        }
        //Gdx.app.log("WaitTimer", waitTimer + "");
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f,0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture,0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
        batch.draw(buttonTexture, levelOneRect.x, levelOneRect.y, levelOneRect.width, levelOneRect.height);
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "1", (levelOneRect.x + levelOneRect.getWidth() / 2)*100  , (levelOneRect.y + levelOneRect.getHeight() / 2)*100 );
        batch.end();

        game.letsFigurePositionForMePlease(levelOneRect, 5f);
        whatHasBeenTouched();
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

    private void whatHasBeenTouched(){
        if(Gdx.input.isTouched() && waitIsOver){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if(menuRect.contains(touchPos.x, touchPos.y)){
                game.setScreen(new MainMenu(game));
            }

            if(levelOneRect.contains(touchPos.x, touchPos.y)){
                game.setScreen(new LevelOne(game, font));
            }
        }
    }
}

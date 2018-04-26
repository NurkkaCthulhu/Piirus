package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anu on 8.3.2018.
 */

public class LevelSelect extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture backgroundTexture;
    private Rectangle menuRect;
    private Rectangle levelOneRect;
    private Rectangle levelTwoRect;
    private Rectangle levelThreeRect;
    private Rectangle levelFourRect;
    private Rectangle levelFiveRect;
    private Rectangle levelSixRect;
    private BitmapFont font;

    public LevelSelect(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonPressedTexture = new Texture(Gdx.files.internal("levelbutton_pressed.png"));
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        levelOneRect = new Rectangle(2, 2, 1f, 1f);
        levelTwoRect = new Rectangle(3, 2, 1f, 1f);
        levelThreeRect = new Rectangle(4, 2, 1f, 1f);
        levelFourRect = new Rectangle(5, 2, 1f, 1f);
        levelFiveRect = new Rectangle(2, 1, 1f, 1f);
        levelSixRect = new Rectangle(3, 1, 1f, 1f);


        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.app.log("WaitTimer", waitTimer + "");
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f,0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture,0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
        batch.draw(buttonTexture, levelOneRect.x, levelOneRect.y, levelOneRect.width, levelOneRect.height);
        batch.draw(buttonTexture, levelTwoRect.x, levelTwoRect.y, levelTwoRect.width, levelTwoRect.height);
        batch.draw(buttonTexture, levelThreeRect.x, levelThreeRect.y, levelThreeRect.width, levelThreeRect.height);
        batch.draw(buttonTexture, levelFourRect.x, levelFourRect.y, levelFourRect.width, levelFourRect.height);
        batch.draw(buttonTexture, levelFiveRect.x, levelFiveRect.y, levelFiveRect.width, levelFiveRect.height);
        batch.draw(buttonTexture, levelSixRect.x, levelSixRect.y, levelSixRect.width, levelSixRect.height);
        showPressedButton();
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "1", (levelOneRect.x + levelOneRect.getWidth()/2)*100  , (levelOneRect.y + levelOneRect.getHeight()/2)*100 );
        font.draw(batch, "2", (levelTwoRect.x + levelTwoRect.getWidth()/2)*100  , (levelTwoRect.y + levelTwoRect.getHeight()/2)*100 );
        font.draw(batch, "3", (levelThreeRect.x + levelThreeRect.getWidth()/2)*100  , (levelThreeRect.y + levelThreeRect.getHeight()/2)*100 );
        font.draw(batch, "4", (levelFourRect.x + levelFourRect.getWidth()/2)*100  , (levelFourRect.y + levelFourRect.getHeight()/2)*100 );
        font.draw(batch, "5", (levelFiveRect.x + levelFiveRect.getWidth()/2)*100  , (levelFiveRect.y + levelFiveRect.getHeight()/2)*100 );
        font.draw(batch, "6", (levelSixRect.x + levelSixRect.getWidth()/2)*100  , (levelSixRect.y + levelSixRect.getHeight()/2)*100 );
        batch.end();

        game.letsFigurePositionForMePlease(levelThreeRect, 5f);
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

    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y)){
            font.dispose();
            game.setScreen(new MainMenu(game));
        }
        if(levelOneRect.contains(touchPos.x, touchPos.y)){
            game.setScreen(new Level(game, font, 1));
        }
        if (levelTwoRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new Level(game, font, 2));
        }
        if (levelThreeRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new Level(game, font, 3));
        }
        if (levelFourRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new Level(game, font, 4));
        }
        if (levelFiveRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new Level(game, font, 5));
        }
        if (levelSixRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new Level(game, font, 6));
        }
        return false;
    }

    private void showPressedButton(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(levelOneRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelOneRect.x, levelOneRect.y, levelOneRect.width, levelOneRect.height);
            if(levelTwoRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelTwoRect.x, levelTwoRect.y, levelTwoRect.width, levelTwoRect.height);
            if(levelThreeRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelThreeRect.x, levelThreeRect.y, levelThreeRect.width, levelThreeRect.height);
            if(levelFourRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelFourRect.x, levelFourRect.y, levelFourRect.width, levelFourRect.height);
            if(levelFiveRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelFiveRect.x, levelFiveRect.y, levelFiveRect.width, levelFiveRect.height);
            if(levelSixRect.contains(touchPos.x, touchPos.y))
                batch.draw(buttonPressedTexture, levelSixRect.x, levelSixRect.y, levelSixRect.width, levelSixRect.height);
        }
    }
}

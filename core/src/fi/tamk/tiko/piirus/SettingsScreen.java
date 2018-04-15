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

/**
 * Created by Rip10 on 26.3.2018.
 */

public class SettingsScreen extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture backgroundTexture;
    private BitmapFont font;
    private Rectangle menuRect;
    private Rectangle calibrationRect;
    private Rectangle sliderBackRect;
    private Rectangle sliderFrontRect;
    private Dot iShowDotSize;

    public SettingsScreen(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        iShowDotSize = new Dot(6, 1.8f, true);

        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        calibrationRect = new Rectangle(2,2, 0.8f, 0.4f);
        sliderBackRect = new Rectangle(5.04f, 1, 2f, 0.1f);
        sliderFrontRect = new Rectangle(6, 0.85f, 0.2f, 0.4f);

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
        batch.draw(buttonTexture, calibrationRect.x, calibrationRect.y, calibrationRect.width, calibrationRect.height);
        batch.draw(buttonTexture, sliderBackRect.x, sliderBackRect.y, sliderBackRect.width, sliderBackRect.height);
        batch.draw(buttonTexture, sliderFrontRect.x, sliderFrontRect.y, sliderFrontRect.width, sliderFrontRect.height);
        iShowDotSize.sprite.draw(batch);
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "Kalibroi", calibrationRect.x*100, (calibrationRect.y + calibrationRect.getHeight() / 2)*100 );
        batch.end();
        game.letsFigurePositionForMePlease(sliderBackRect, 2);
        sliderStuff();
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
            game.dotSize = sliderFrontRect.x - 5f;
            game.setScreen(new MainMenu(game));
        } else if (calibrationRect.contains(touchPos.x, touchPos.y)) {
            buttonTexture.dispose();
            backgroundTexture.dispose();
            game.calibrate();
            game.setScreen(new CalibrationScreen(game, font));
        }
        return false;
    }

    private void sliderStuff(){
        if(Gdx.input.isTouched()){
            Vector3 touchpos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpos);
            if(touchpos.x < 7 && touchpos.x > 5f && touchpos.y > 0.85f && touchpos.y < 1.25f){
                sliderFrontRect.x = touchpos.x;
                Gdx.app.log("SliderFrontX", "" + sliderFrontRect.x);
                Gdx.app.log("Multiplyer", "" + (sliderFrontRect.x - 5f));
                iShowDotSize.setSize(sliderFrontRect.x - 5f);
            }
        }
    }
}

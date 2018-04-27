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
    private Texture buttonPressedTexture;
    private Texture backgroundTexture;
    private Texture volumeTexture;
    private Texture volumeLvlTexture;
    private Texture musicOnTexture;
    private Texture musicOffTexture;
    private BitmapFont font;
    private Rectangle menuRect;
    private Rectangle calibrationRect;
    private Rectangle sliderBackRect;
    private Rectangle sliderFrontRect;
    private Rectangle volumeRect;
    private Rectangle musicRect;
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

        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonPressedTexture = new Texture(Gdx.files.internal("levelbutton_pressed.png"));
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        volumeLvlTexture = new Texture(Gdx.files.internal("volumelvl.png"));
        volumeTexture = new Texture(Gdx.files.internal("volume.png"));
        musicOnTexture = new Texture(Gdx.files.internal("musicOn.png"));
        musicOffTexture = new Texture(Gdx.files.internal("musicOff.png"));
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        calibrationRect = new Rectangle(2,2, 0.8f, 0.4f);
        sliderBackRect = new Rectangle(5.04f, 1, 2f, 0.1f);
        sliderFrontRect = new Rectangle(game.dotSize + 4.5f, 0.85f, 0.2f, 0.4f);
        volumeRect = new Rectangle(1.23f, 0.03f, 0.26f,0.36f);
        musicRect = new Rectangle(1.7f, 0.03f, 0.26f, 0.36f);
        iShowDotSize.setSize(game.dotSize);

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
        batch.draw(volumeTexture, volumeRect.x, volumeRect.y, volumeRect.width, volumeRect.height);
        if(game.sounds){
            batch.draw(volumeLvlTexture, volumeRect.x + 0.3f, volumeRect.y + 0.15f / 2, volumeRect.width / 3, volumeRect.height - 0.15f);
            batch.draw(volumeLvlTexture, volumeRect.x + 0.39f, volumeRect.y + 0.05f / 2, volumeRect.width / 3, volumeRect.height - 0.05f);
        }
        if(game.music){
            batch.draw(musicOnTexture, musicRect.x, musicRect.y, musicRect.width, musicRect.height);
        } else {
            batch.draw(musicOffTexture, musicRect.x, musicRect.y, musicRect.width, musicRect.height);
        }
        iShowDotSize.sprite.draw(batch);
        showPressedButtons();
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "Kalibroi", calibrationRect.x*100, (calibrationRect.y + calibrationRect.getHeight() / 2)*100 );
        batch.end();
        game.letsFigurePositionForMePlease(volumeRect, 2);
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
        volumeTexture.dispose();
        volumeLvlTexture.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y)){
            dispose();
            game.dotSize = sliderFrontRect.x - 4.5f;
            game.setScreen(new MainMenu(game));
        } else if (calibrationRect.contains(touchPos.x, touchPos.y)) {
            buttonTexture.dispose();
            backgroundTexture.dispose();
            game.calibrate();
            game.setScreen(new CalibrationScreen(game, font));
        } else if (volumeRect.contains(touchPos.x, touchPos.y)){
            if(game.sounds){
                game.sounds = false;
            } else {
                game.sounds = true;
            }
        } else if (musicRect.contains(touchPos.x, touchPos.y)){
            if(game.music){
                game.music = false;
            } else {
                game.music = true;
            }
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
                Gdx.app.log("Multiplyer", "" + (sliderFrontRect.x - 4.5f));
                iShowDotSize.setSize(sliderFrontRect.x - 4.5f);
            }
        }
    }

    private void showPressedButtons(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(calibrationRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, calibrationRect.x, calibrationRect.y, calibrationRect.width, calibrationRect.height);
            }
        }
    }
}

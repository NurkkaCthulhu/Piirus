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
    private Texture buttonCrossedTexture;
    private Texture backgroundTexture;
    private Texture volumeTexture;
    private Texture volumeLvlTexture;
    private Texture musicOnTexture;
    private Texture musicOffTexture;
    private Texture penTexture;
    private BitmapFont font;
    private Rectangle menuRect;
    private Rectangle calibrationRect;
    private Rectangle difficultyRect;
    private Rectangle sliderBackRect;
    private Rectangle sliderFrontRect;
    private Rectangle sliderBackRectTwo;
    private Rectangle sliderFrontRectTwo;
    private Rectangle sliderBackRectThree;
    private Rectangle sliderFrontRectThree;
    private Rectangle sliderBackRectFour;
    private Rectangle sliderFrontRectFour;
    private Rectangle soundRect;
    private Rectangle volumeRect;
    private Rectangle musicRect;
    private Rectangle penRect;
    private Rectangle scoreToggle;
    private Dot iShowDotSize;
    private String difficultyString;

    //localization Strings
    private String textCalibrationScreen;
    private String textDifficultyScreen;
    private String textSoundScreen;
    private String textPenSize;
    private String textDotSize;
    private String textCalculateScore;
    private String textDifficulty;
    private String textHard;
    private String textNormal;
    private String textEasy;
    private String textVeryEasy;
    private String textNoScoring;

    private boolean mainSettings;
    private boolean difficultySettings;
    private boolean soundSettings;

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
        buttonCrossedTexture = new Texture(Gdx.files.internal("levelbutton_cross.png"));
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        volumeLvlTexture = new Texture(Gdx.files.internal("volumelvl.png"));
        volumeTexture = new Texture(Gdx.files.internal("volume.png"));
        musicOnTexture = new Texture(Gdx.files.internal("musicOn.png"));
        musicOffTexture = new Texture(Gdx.files.internal("musicOff.png"));
        penTexture = new Texture(Gdx.files.internal("pen.png"), true);
        penTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        calibrationRect = new Rectangle(2,2.5f, 1.6f, 0.4f);
        difficultyRect = new Rectangle(2, 2f, 1.6f, 0.4f);
        sliderBackRect = new Rectangle(5.04f, 1, 2f, 0.1f);
        sliderFrontRect = new Rectangle(game.dotSize + 4.5f, 0.85f, 0.2f, 0.4f);
        sliderBackRectTwo = new Rectangle(2.04f, 1, 1f, 0.1f);
        sliderFrontRectTwo = new Rectangle(game.penSize + 1.5f, 0.85f, 0.2f, 0.4f);
        sliderBackRectThree = new Rectangle(1.54f, 1, 1.62f, 0.1f);
        sliderFrontRectThree = new Rectangle(1.5f + game.effectVolume * 1.5f, 0.85f, 0.2f, 0.4f);
        sliderBackRectFour = new Rectangle(5.04f, 1, 1.62f, 0.1f);
        sliderFrontRectFour = new Rectangle(5.04f + game.musicVolume * 1.5f, 0.85f, 0.2f, 0.4f);
        soundRect = new Rectangle(2, 1.5f, 1.6f, 0.4f);
        volumeRect = new Rectangle(1.23f, 0.03f, 0.26f,0.36f);
        musicRect = new Rectangle(1.7f, 0.03f, 0.26f, 0.36f);
        penRect = new Rectangle(2.5f, 1.3f, 1 * game.penSize, 1 * game.penSize);
        scoreToggle = new Rectangle(4, 1.3f, 0.4f, 0.4f);
        difficultyString = textNormal;
        iShowDotSize.setSize(game.dotSize);

        mainSettings = true;
        difficultySettings = false;
        soundSettings = false;

        updateSettingsText();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        fadeMusicIn();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f,0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture,0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
        if(mainSettings){
            batch.draw(buttonTexture, calibrationRect.x, calibrationRect.y, calibrationRect.width, calibrationRect.height);
            batch.draw(buttonTexture, difficultyRect.x, difficultyRect.y, difficultyRect.width, difficultyRect.height);
            batch.draw(buttonTexture, soundRect.x, soundRect.y, soundRect.width, soundRect.height);

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
            showPressedButtons();
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, textCalibrationScreen, calibrationRect.x*100 + calibrationRect.width * 100 / 10, (calibrationRect.y + calibrationRect.getHeight() / 2)*100 + 10);
            font.draw(batch, textDifficultyScreen, difficultyRect.x*100 + difficultyRect.width * 100 / 10, (difficultyRect.y + difficultyRect.getHeight() / 2)*100 + 10);
            font.draw(batch, textSoundScreen, soundRect.x*100 + soundRect.width * 100 / 10, (soundRect.y + soundRect.getHeight() / 2)*100 + 10);
            font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        }
        if(difficultySettings){
            batch.draw(buttonTexture, sliderBackRect.x, sliderBackRect.y, sliderBackRect.width, sliderBackRect.height);
            batch.draw(buttonTexture, sliderFrontRect.x, sliderFrontRect.y, sliderFrontRect.width, sliderFrontRect.height);
            batch.draw(buttonTexture, sliderBackRectTwo.x, sliderBackRectTwo.y, sliderBackRectTwo.width, sliderBackRectTwo.height);
            batch.draw(buttonTexture, sliderFrontRectTwo.x, sliderFrontRectTwo.y, sliderFrontRectTwo.width, sliderFrontRectTwo.height);
            if(game.scoreTracking)
                batch.draw(buttonCrossedTexture, scoreToggle.x, scoreToggle.y, scoreToggle.width, scoreToggle.height);
            if(!game.scoreTracking)
                batch.draw(buttonTexture, scoreToggle.x, scoreToggle.y, scoreToggle.width, scoreToggle.height);
            iShowDotSize.sprite.draw(batch);
            batch.draw(penTexture, penRect.x, penRect.y, penRect.width, penRect.height);
            showPressedButtons();
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
            if(sliderFrontRect.x - 4.5f > 0.5 && sliderFrontRect.x - 4.5f < 0.9)
                difficultyString = textHard;
            if(sliderFrontRect.x - 4.5f > 0.9 && sliderFrontRect.x - 4.5f < 1.2)
                difficultyString = textNormal;
            if(sliderFrontRect.x - 4.5f > 1.2 && sliderFrontRect.x - 4.5f < 2)
                difficultyString = textEasy;
            if(sliderFrontRect.x - 4.5f > 2)
                difficultyString = textVeryEasy;
            if(!game.scoreTracking)
                difficultyString = textNoScoring;
            font.draw(batch, textDifficulty + difficultyString, game.WORLD_WIDTH * 100 / 4, sliderBackRect.y * 100 - 30);
            sliderStuff();
        }
        if(soundSettings){
            batch.draw(buttonTexture, sliderBackRectThree.x, sliderBackRectThree.y, sliderBackRectThree.width, sliderBackRectThree.height);
            batch.draw(buttonTexture, sliderFrontRectThree.x, sliderFrontRectThree.y, sliderFrontRectThree.width, sliderFrontRectThree.height);
            batch.draw(buttonTexture, sliderBackRectFour.x, sliderBackRectFour.y, sliderBackRectFour.width, sliderBackRectFour.height);
            batch.draw(buttonTexture, sliderFrontRectFour.x, sliderFrontRectFour.y, sliderFrontRectFour.width, sliderFrontRectFour.height);
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, "Efektit: " + ((int) (Math.ceil((sliderFrontRectThree.x / 1.5f - 1) * 100))) + "%", sliderBackRectThree.x*100, (sliderBackRectThree.y + sliderBackRectThree.getHeight())*150 );
            font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
            sliderStuff();
        }

        batch.end();
        game.letsFigurePositionForMePlease(volumeRect, 2);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.menuMusicVolume = 0;
        game.menuMusic.pause();
    }

    @Override
    public void resume() {
        game.menuMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        buttonTexture.dispose();
        buttonPressedTexture.dispose();
        backgroundTexture.dispose();
        font.dispose();
        volumeTexture.dispose();
        volumeLvlTexture.dispose();
        musicOnTexture.dispose();
        musicOffTexture.dispose();
        penTexture.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y) && mainSettings){
            dispose();
            game.buttonSound.play(game.effectVolume);
            game.dotSize = sliderFrontRect.x - 4.5f;
            game.penSize = sliderFrontRectTwo.x - 1.5f;
            game.setScreen(new MainMenu(game));
        } else if(menuRect.contains(touchPos.x, touchPos.y) && difficultySettings) {
            game.buttonSound.play(game.effectVolume);
            difficultySettings = false;
            soundSettings = false;
            mainSettings = true;
        } else if(menuRect.contains(touchPos.x, touchPos.y) && soundSettings) {
            game.buttonSound.play(game.effectVolume);
            difficultySettings = false;
            soundSettings = false;
            mainSettings = true;
        } else if (calibrationRect.contains(touchPos.x, touchPos.y) && mainSettings) {
            game.buttonSound.play(game.effectVolume);
            buttonTexture.dispose();
            backgroundTexture.dispose();
            game.dotSize = sliderFrontRect.x - 4.5f;
            game.penSize = sliderFrontRectTwo.x - 1.5f;
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
        } else if(difficultyRect.contains(touchPos.x, touchPos.y) && mainSettings){
            game.buttonSound.play(game.effectVolume);
            mainSettings = false;
            difficultySettings = true;
            soundSettings = false;
        } else if(soundRect.contains(touchPos.x, touchPos.y) && mainSettings){
            game.buttonSound.play(game.effectVolume);
            mainSettings = false;
            difficultySettings = false;
            soundSettings = true;
        } else if(scoreToggle.contains(touchPos.x, touchPos.y) && difficultySettings){
            game.buttonSound.play(game.effectVolume);
            if(game.scoreTracking){
                game.scoreTracking = false;
            } else {
                game.scoreTracking = true;
            }
        }
        return false;
    }

    private void sliderStuff(){
        if(Gdx.input.isTouched()){
            Vector3 touchpos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpos);
            if(touchpos.x < 7 && touchpos.x > 5f && touchpos.y > 0.85f && touchpos.y < 1.25f && difficultySettings){
                sliderFrontRect.x = touchpos.x;
                Gdx.app.log("SliderFrontX", "" + sliderFrontRect.x);
                Gdx.app.log("Multiplyer", "" + (sliderFrontRect.x - 4.5f));
                iShowDotSize.setSize(sliderFrontRect.x - 4.5f);
            }
            if(touchpos.x < 3 && touchpos.x > 2f && touchpos.y > 0.85f && touchpos.y < 1.25f && difficultySettings){
                sliderFrontRectTwo.x = touchpos.x;
                Gdx.app.log("SliderFrontX", "" + sliderFrontRectTwo.x);
                Gdx.app.log("Multiplyer", "" + (sliderFrontRectTwo.x - 1.5f));
                penRect.setSize(sliderFrontRectTwo.x - 1.5f, sliderFrontRectTwo.x - 1.5f);
            }
            if(touchpos.x < 3.01f && touchpos.x > 1.5f && touchpos.y > 0.85f && touchpos.y < 1.25f && soundSettings) {
                sliderFrontRectThree.x = touchpos.x;
                Gdx.app.log("SliderFrontX", "" + sliderFrontRectThree.x);
                Gdx.app.log("Multiplyer", "" + (sliderFrontRectThree.x / 1.5f - 1));
                Gdx.app.log("Multiplyer", "" + (Math.ceil((sliderFrontRectThree.x / 1.5f - 1) * 100)));
                game.effectVolume = sliderFrontRectThree.x / 1.5f - 1;
            }
            if(touchpos.x < 6.51f && touchpos.x > 5.01f && touchpos.y > 0.85f && touchpos.y < 1.25f && soundSettings) {
                sliderFrontRectFour.x = touchpos.x;
                Gdx.app.log("SliderFrontX", "" + sliderFrontRectFour.x);
                Gdx.app.log("Multiplyer", "" + ((sliderFrontRectFour.x - 5) / 1.5f));
                Gdx.app.log("Multiplyer", "" + (Math.ceil(((sliderFrontRectFour.x - 5) / 1.5f) * 100)));
                game.musicVolume = (sliderFrontRectFour.x - 5) / 1.5f;
                game.menuMusicVolume = game.musicVolume;
                game.menuMusic.setVolume(game.musicVolume);
            }
        }
    }

    private void showPressedButtons(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(calibrationRect.contains(touchPos.x, touchPos.y) && mainSettings){
                batch.draw(buttonPressedTexture, calibrationRect.x, calibrationRect.y, calibrationRect.width, calibrationRect.height);
            } else if(menuRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
            } else if(difficultyRect.contains(touchPos.x, touchPos.y) && mainSettings){
                batch.draw(buttonPressedTexture, difficultyRect.x, difficultyRect.y, difficultyRect.width, difficultyRect.height);
            } else if(soundRect.contains(touchPos.x, touchPos.y) && mainSettings){
                batch.draw(buttonPressedTexture, soundRect.x, soundRect.y, soundRect.width, soundRect.height);
            }
        }
    }

    private void updateSettingsText() {
        textCalibrationScreen = game.getMyBundle().get("calibrate");
        textDifficultyScreen = game.getMyBundle().get("difficultyScreen");
        textSoundScreen = game.getMyBundle().get("soundScreen");
        textPenSize = game.getMyBundle().get("changePenSize");
        textDotSize = game.getMyBundle().get("changeDotSize");
        textCalculateScore = game.getMyBundle().get("calculateScore");
        textDifficulty = game.getMyBundle().get("difficulty");
        textHard = game.getMyBundle().get("hard");
        textNormal = game.getMyBundle().get("normal");
        textEasy = game.getMyBundle().get("easy");
        textVeryEasy = game.getMyBundle().get("veryEasy");
        textNoScoring = game.getMyBundle().get("scoring");
    }

    private void fadeMusicIn(){
        if(game.menuMusicVolume < game.musicVolume)
            game.menuMusicVolume += 0.001f;
        if(game.menuMusicVolume < game.musicVolume)
            game.menuMusic.setVolume(game.menuMusicVolume);
    }
}

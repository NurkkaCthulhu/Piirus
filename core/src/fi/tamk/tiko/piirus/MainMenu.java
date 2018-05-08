package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anu on 8.3.2018.
 */

/*
*
*/

public class MainMenu extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture backgroundTexture;
    private Texture exitTexture;
    private Texture localeFiFlag;
    private Texture localeEnFlag;
    private Rectangle gameRect;
    private Rectangle settingsRect;
    private Rectangle freeDrawRect;
    private Rectangle exitRect;
    private Rectangle exitYes;
    private Rectangle exitNo;
    private Rectangle rectFi;
    private Rectangle rectEn;
    //localization Strings
    private String textPlay;
    private String textSettings;
    private String textFreeDraw;
    private boolean exitConfirmation;

    private BitmapFont font; //FreeType best

    public MainMenu(PiirusMain g){
        //Initial stuff
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        exitConfirmation = false;

        //Creating font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 3;

        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();

        //Creating menu buttons
        localeEnFlag = new Texture(Gdx.files.internal("flag_en.png"), true);
        localeEnFlag.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        localeFiFlag = new Texture(Gdx.files.internal("flag_fi.png"), true);
        localeFiFlag.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        buttonTexture = new Texture(Gdx.files.internal("menuPen.png"), true);
        buttonPressedTexture = new Texture(Gdx.files.internal("menuPen_pressed.png"), true);
        buttonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        buttonPressedTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        backgroundTexture = new Texture(Gdx.files.internal("hopefullynotpermanentmainmenubackgground.png"));
        exitTexture = new Texture(Gdx.files.internal("application_exit.png"));
        exitTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameRect = new Rectangle(2.5f, 2.25f, 3f, 0.5f);
        settingsRect = new Rectangle(2.5f, 1.5f, 3f, 0.5f);
        freeDrawRect = new Rectangle(2.5f, 0.75f, 3f, 0.5f);
        exitRect = new Rectangle(0, 0, 0.8f, 0.8f);
        exitYes = new Rectangle(2.5f, 1.5f, 3f, 0.5f);
        exitNo = new Rectangle(2.5f, 0.75f, 3f, 0.5f);
        rectFi = new Rectangle(0.1f, 4.1f, game.WORLD_WIDTH*0.1f, game.WORLD_HEIGHT*0.1f);
        rectEn = new Rectangle(1f, 4.1f, game.WORLD_WIDTH*0.1f, game.WORLD_HEIGHT*0.1f);

        setFlagTexture();
        updateMenuText();
        game.menuMusic.play();
        game.menuMusic.setVolume(game.musicVolume);
        game.menuMusic.setLooping(true);

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
        if(!exitConfirmation){
            drawButtons();
            batch.draw(localeEnFlag, rectEn.x, rectEn.y, rectEn.width, rectEn.height);
            batch.draw(localeFiFlag, rectFi.x, rectFi.y, rectFi.width, rectFi.height);
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, textPlay, gameRect.x*100 + gameRect.width / 2 * 100 + 10, (gameRect.y + gameRect.getHeight() / 2)*100+10, 1, 1, true);
            font.draw(batch, textSettings, settingsRect.x*100 + settingsRect.width / 2 * 100 + 10, (settingsRect.y + settingsRect.getHeight() / 2)*100+10, 1, 1, true);
            font.draw(batch, textFreeDraw, freeDrawRect.x*100 + freeDrawRect.width / 2 * 100 + 10, (freeDrawRect.y + freeDrawRect.getHeight() / 2)*100+10, 1, 1, true);
        } else {
            showExitConfirmation();
        }

        batch.end();

        if(game.music)
            fadeMusicIn();
        else if(game.menuMusic.isPlaying())
            game.menuMusic.stop();

        //game.letsFigurePositionForMePlease(freeDrawRect, 5);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            exitConfirmation = true;
        }
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
        backgroundTexture.dispose();
        buttonPressedTexture.dispose();
        font.dispose();
        localeEnFlag.dispose();
        localeFiFlag.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);

        if(gameRect.contains(touchPos.x, touchPos.y) && !exitConfirmation){
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            game.setScreen(new LevelSelect(game, font));
        }

        if(settingsRect.contains(touchPos.x,touchPos.y) && !exitConfirmation){
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            game.setScreen(new SettingsScreen(game, font));
        }

        if(freeDrawRect.contains(touchPos.x, touchPos.y) && !exitConfirmation){
            game.calibrate();
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            game.setScreen(new FreeDrawScreen(game, font));
        }

        //locale flags
        if(rectFi.contains(touchPos.x, touchPos.y) && !exitConfirmation) {
            game.setLocale(0);
            localeEnFlag = new Texture(Gdx.files.internal("flag_en.png"), true);
            localeFiFlag = new Texture(Gdx.files.internal("flag_fi_selected.png"), true);
            updateMenuText();
        }
        if(rectEn.contains(touchPos.x, touchPos.y) && !exitConfirmation) {
            game.setLocale(1);
            localeEnFlag = new Texture(Gdx.files.internal("flag_en_selected.png"), true);
            localeFiFlag = new Texture(Gdx.files.internal("flag_fi.png"), true);
            updateMenuText();
        }
        if(exitRect.contains(touchPos.x, touchPos.y) && !exitConfirmation){
            exitConfirmation = true;
        }
        if(exitNo.contains(touchPos.x, touchPos.y) && exitConfirmation)
            exitConfirmation = false;
        return false;
    }

    private void setFlagTexture() {
        if(game.getLanguage().equalsIgnoreCase("en")) {
            localeEnFlag = new Texture(Gdx.files.internal("flag_en_selected.png"), true);
            localeFiFlag = new Texture(Gdx.files.internal("flag_fi.png"), true);
        } else {
            localeEnFlag = new Texture(Gdx.files.internal("flag_en.png"), true);
            localeFiFlag = new Texture(Gdx.files.internal("flag_fi_selected.png"), true);
        }
    }

    private void drawButtons(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(gameRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
            } else if(!gameRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
            }
            if(settingsRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, settingsRect.x, settingsRect.y, settingsRect.width, settingsRect.height);
            } else if(!settingsRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonTexture, settingsRect.x, settingsRect.y, settingsRect.width, settingsRect.height);
            }
            if(freeDrawRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, freeDrawRect.x, freeDrawRect.y, freeDrawRect.width, freeDrawRect.height);
            } else if(!freeDrawRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonTexture, freeDrawRect.x, freeDrawRect.y, freeDrawRect.width, freeDrawRect.height);
            }
        } else {
            batch.draw(buttonTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
            batch.draw(buttonTexture, settingsRect.x, settingsRect.y, settingsRect.width, settingsRect.height);
            batch.draw(buttonTexture, freeDrawRect.x, freeDrawRect.y, freeDrawRect.width, freeDrawRect.height);
        }
        batch.draw(exitTexture, exitRect.x, exitRect.y, exitRect.width, exitRect.height);
    }

    private void updateMenuText() {
        textPlay = game.getMyBundle().get("play");
        textSettings= game.getMyBundle().get("settings");
        textFreeDraw = game.getMyBundle().get("freedraw");
    }

    private void fadeMusicIn(){
        if(game.menuMusicVolume < game.musicVolume)
            game.menuMusicVolume += 0.005f;
        if(game.menuMusicVolume < game.musicVolume)
            game.menuMusic.setVolume(game.menuMusicVolume);
        if(game.gameMusic.isPlaying())
            game.gameMusic.pause();
    }

    private void showExitConfirmation(){
        Vector3 touchPos = new Vector3();
        if(Gdx.input.isTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        }
        if(exitYes.contains(touchPos.x, touchPos.y)){
            batch.draw(buttonPressedTexture, exitYes.x, exitYes.y, exitYes.width, exitYes.height);
            Gdx.app.exit();
        } else {
            batch.draw(buttonTexture, exitYes.x, exitYes.y, exitYes.width, exitYes.height);
        }
        if(exitNo.contains(touchPos.x, touchPos.y)){
            batch.draw(buttonPressedTexture, exitNo.x, exitNo.y, exitNo.width, exitNo.height);
        } else {
            batch.draw(buttonTexture, exitNo.x, exitNo.y, exitNo.width, exitNo.height);
        }

        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "Yes", exitYes.x * 100 + exitYes.width / 2 * 100, (exitYes.y + exitYes.getHeight() / 2)*100+10, 1, 1, true);
        font.draw(batch, "No", exitNo.x * 100 + exitNo.width / 2 * 100, (exitNo.y + exitNo.getHeight() / 2)*100+10, 1, 1, true);
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            Gdx.app.exit();
        }
    }
}

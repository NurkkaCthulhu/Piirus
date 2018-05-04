package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
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
* Very basic menu functionality 15.3.2018
*
* Position figuring moved to the Main class.
* Also some position figuring code for menu buttons
* No need for trial and error! :muscle:
*/

public class MainMenu extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture backgroundTexture;
    private Texture localeFiFlag;
    private Texture localeEnFlag;
    private Rectangle gameRect; //Peliin "nappi"
    private Rectangle settingsRect;
    private Rectangle highscoreRect;
    private Rectangle rectFi;
    private Rectangle rectEn;
    //localization Strings
    private String textPlay;
    private String textSettings;
    private String textFreeDraw;

    private BitmapFont font; //FreeType best

    public MainMenu(PiirusMain g){
        //Initial stuff
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

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
        gameRect = new Rectangle(2.5f, 2.25f, 3f, 0.5f);
        settingsRect = new Rectangle(2.5f, 1.5f, 3f, 0.5f);
        highscoreRect = new Rectangle(2.5f, 0.75f, 3f, 0.5f);
        rectFi = new Rectangle(0.1f, 0.1f, game.WORLD_WIDTH*0.1f, game.WORLD_HEIGHT*0.1f);
        rectEn = new Rectangle(1f, 0.1f, game.WORLD_WIDTH*0.1f, game.WORLD_HEIGHT*0.1f);

        updateMenuText();

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
        drawButtons();
        batch.draw(localeEnFlag, rectEn.x, rectEn.y, rectEn.width, rectEn.height);
        batch.draw(localeFiFlag, rectFi.x, rectFi.y, rectFi.width, rectFi.height);
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, textPlay, gameRect.x*100+100, (gameRect.y + gameRect.getHeight() / 2)*100+10);
        font.draw(batch, textSettings, settingsRect.x*100+75, (settingsRect.y + settingsRect.getHeight() / 2)*100+10);
        font.draw(batch, textFreeDraw, highscoreRect.x*100+60, (highscoreRect.y + highscoreRect.getHeight() / 2)*100+10);
        batch.end();

        game.letsFigurePositionForMePlease(highscoreRect, 5);
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
        buttonPressedTexture.dispose();
        font.dispose();
        localeEnFlag.dispose();
        localeFiFlag.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);

        if(gameRect.contains(touchPos.x, touchPos.y)){
            game.calibrate();
            game.buttonSound.play(game.effectVolume);
            game.setScreen(new LevelSelect(game, font));
        }

        if(settingsRect.contains(touchPos.x,touchPos.y)){
            game.calibrate();
            game.buttonSound.play(game.effectVolume);
            game.setScreen(new SettingsScreen(game, font));
        }

        // THIS GOES TO FREE DRAW NOW!
        if(highscoreRect.contains(touchPos.x, touchPos.y)){
            game.calibrate();
            game.buttonSound.play(game.effectVolume);
            game.setScreen(new FreeDrawScreen(game, font));
        }

        //locale flags
        if(rectFi.contains(touchPos.x, touchPos.y)) {
            game.setLocale(0);
            updateMenuText();
        }
        if(rectEn.contains(touchPos.x, touchPos.y)) {
            game.setLocale(1);
            updateMenuText();
        }

        return false;
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
            if(highscoreRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonPressedTexture, highscoreRect.x, highscoreRect.y, highscoreRect.width, highscoreRect.height);
            } else if(!highscoreRect.contains(touchPos.x, touchPos.y)){
                batch.draw(buttonTexture, highscoreRect.x, highscoreRect.y, highscoreRect.width, highscoreRect.height);
            }
        } else {
            batch.draw(buttonTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
            batch.draw(buttonTexture, settingsRect.x, settingsRect.y, settingsRect.width, settingsRect.height);
            batch.draw(buttonTexture, highscoreRect.x, highscoreRect.y, highscoreRect.width, highscoreRect.height);
        }
    }

    private void updateMenuText() {
        textPlay = game.getMyBundle().get("play");
        textSettings= game.getMyBundle().get("settings");
        textFreeDraw = game.getMyBundle().get("freedraw");
    }
}

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

public class MainMenu implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture; //Kaikille oma menu texture, vai vain yksi ja sitä piirretään monta kertaa? <-yks joka on monta kertaa imo
    private Rectangle gameRect; //Peliin "nappi"
    private Rectangle settingsRect;
    private Rectangle highscoreRect;

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
        parameter.size = 20;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);
        generator.dispose();

        //Creating menu buttons
        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        gameRect = new Rectangle(3f, 2.25f, 1.5f, 0.5f);
        settingsRect = new Rectangle(3f, 1.5f, 1.5f, 0.5f);
        highscoreRect = new Rectangle(3f, 0.75f, 1.5f, 0.5f);
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
        batch.draw(buttonTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
        batch.draw(buttonTexture, settingsRect.x, settingsRect.y, settingsRect.width, settingsRect.height);
        batch.draw(buttonTexture, highscoreRect.x, highscoreRect.y, highscoreRect.width, highscoreRect.height);
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "Play(WIP)", gameRect.x*100, (gameRect.y + gameRect.getHeight() / 2)*100);
        font.draw(batch, "Settings(WIP)", settingsRect.x*100, (settingsRect.y + settingsRect.getHeight() / 2)*100);
        font.draw(batch, "Highscore(WIP)", highscoreRect.x*100, (highscoreRect.y + highscoreRect.getHeight() / 2)*100);
        batch.end();

        game.letsFigurePositionForMePlease(highscoreRect, 5);

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
        font.dispose();
    }

    private void whatHasBeenTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if(gameRect.contains(touchPos.x, touchPos.y)){
                //Gdx.app.log("gameRect", "I GOT TOUCHED!");
                game.calibrate();
                //Mennään suoraan peliin
                game.setScreen(new LevelOne(game));

                /*
                * goto levelselect here
                * */
            }

            if(settingsRect.contains(touchPos.x,touchPos.y)){
                game.calibrate();
                game.setScreen(new SettingsScreen(game, font));
            }

            if(highscoreRect.contains(touchPos.x, touchPos.y)){
                game.setScreen(new HighScoreScreen(game, font));
            }
        }
    }
}

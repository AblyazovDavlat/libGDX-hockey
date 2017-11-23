package com.fandroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;



public class GameScreen implements Screen{
	SpriteBatch batch;
    OrthographicCamera camera;

    final MyGame game;

    Texture[] shipYourImage;
    Texture[] shipEnemyImage;
    Texture[] ballImage;
    Music[] backgroundMusic;
    Sound overlapsSound;

    Vector3 touchPos;

    Rectangle shipYour;
    Rectangle shipEnemy;
    Rectangle ball;

    float ballX;
    float ballY;
    float ballXSpeed = 3;
    float ballYSpeed = 3;

    float shipX;
    float shipXSpeed = 4;


    int goalYour = 0;
    int goalEnemy = 0;

    int level = 0;

    public GameScreen(final MyGame _game )
    {
        this.game = _game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

		batch = new SpriteBatch();

        touchPos = new Vector3();

        shipYourImage = new Texture[3];
        shipEnemyImage = new Texture[3];
        ballImage = new Texture[3];
        backgroundMusic = new Music[3];

        for (int i = 0; i < shipYourImage.length; i++){
            shipYourImage[i] = new Texture(String.format("yourShip_%d.png", i));
        }

        for (int i = 0; i < shipEnemyImage.length; i++){
            shipEnemyImage[i] = new Texture(String.format("enemyShip_%d.png", i));
        }

        for (int i = 0; i < ballImage.length; i++){
            ballImage[i] = new Texture(String.format("imageBall_%d.png", i));
        }
        for (int i = 0; i < backgroundMusic.length; i++){
            backgroundMusic[i] = Gdx.audio.newMusic(Gdx.files.internal(String.format("musicBackground_%d.mp3", i)));
        }

        overlapsSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));




        shipYour = new Rectangle();
        shipYour.x = 480 / 2 - 270 / 2;
        shipYour.y = 20;
        shipYour.width = 124;
        shipYour.height = 72;

        shipEnemy = new Rectangle();
        shipEnemy.x = 480 / 2 - 216 / 2;
        shipEnemy.y = 800-72;
        shipEnemy.width = 124;
        shipEnemy.height = 20;

        ball = new Rectangle();
        ball.x = 480/2 - 61/2;
        ball.y = 800/2 - 61/2;
        ball.width = 61;
        ball.height = 61;


	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch,"Score " + goalYour + " : " + goalEnemy, 0 , 800);
        game.batch.draw(shipYourImage[level], shipYour.x, shipYour.y);
        game.batch.draw(shipEnemyImage[level], shipEnemy.x, shipEnemy.y);
        game.batch.draw(ballImage[level], ball.x, ball.y);
        if (level > 0){
            backgroundMusic[level-1].stop();
        }
        backgroundMusic[level].play();
        game.batch.end();

        if (Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
            camera.unproject(touchPos);
            shipYour.x = (int) (touchPos.x - 124/2);
        }

        ballX = ballX + ballXSpeed;
        ballY = ballY + ballYSpeed;

        if (ballX < 0 || ballX > 480) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballY < 0) {
            ballYSpeed = -ballYSpeed;
            goalEnemy++;
            ballY = 400;
            ballX = 240;
        }
        if ( ballY > 800){
            ballYSpeed = -ballYSpeed;
            goalYour++;
            ballY = 400;
            ballX = 240;
        }

        ball.x = (int) ballX;
        ball.y = (int) ballY;

        shipX = shipX + shipXSpeed;
        if (shipX + 124/2 < 0 || shipX -124/2> 480) shipXSpeed = -shipXSpeed;

        shipEnemy.x = (int) shipX;



        if (ball.overlaps(shipYour)){
            ballYSpeed = -ballYSpeed;
            overlapsSound.play();
        }
        if (ball.overlaps(shipEnemy)){
            ballYSpeed = -ballYSpeed;
            overlapsSound.play();
        }

        if (goalYour == 2) {
            if (level < 2)
                level++;
            goalYour = 0;
            goalEnemy = 0;

            while (ballYSpeed < 9) {

                if (shipXSpeed < 0) shipXSpeed--;
                else shipXSpeed++;

                if (ballYSpeed < 0) ballYSpeed = ballYSpeed - 2;
                else ballYSpeed = ballYSpeed + 2;

                if (ballXSpeed < 0) ballXSpeed = ballXSpeed - 2;
                else ballXSpeed = ballXSpeed + 2;
            }
        }


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
    public void show() {

    }


    @Override
	public void dispose () {
		batch.dispose();
	}
}

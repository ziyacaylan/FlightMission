package com.ziya.flightmission;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlightMission extends ApplicationAdapter {

	SpriteBatch batch;
	Texture backrpound;
	Texture airship;
	Texture enemy1;
	Texture enemy2;
	Texture enemy3;

	float airshipX=0;
	float airshipY=0;
	int gameState=0;
	float velocity = 0;
	float gravity = 0.1f;
	float enemyVelocity= 2;
	Random random;
	BitmapFont font;
	BitmapFont font2;

	int score = 0;
	int scoreEnemy = 0;

	Circle airshipCircle;
	ShapeRenderer shapeRenderer;

	int numberOfEnemiesSet =4;
	float [] enemyX = new float[numberOfEnemiesSet];
	float enemyDistance = 0;

	float [] enemyOffset1 = new float[numberOfEnemiesSet];
	float [] enemyOffset2 = new float[numberOfEnemiesSet];
	float [] enemyOffset3 = new float[numberOfEnemiesSet];
	float distance;
	Circle[] enemyCircle1;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;

	@Override
	public void create () {
		batch = new SpriteBatch();
		backrpound=new Texture("background.png");
		airship = new Texture("airship.png");
		enemy1 = new Texture("enemy.png");
		enemy2 = new Texture("enemy.png");
		enemy3 = new Texture("enemy.png");

		distance = Gdx.graphics.getWidth() / 2;
		random = new Random();

		enemyDistance = Gdx.graphics.getWidth()/2;

		airshipX = Gdx.graphics.getWidth()/2-Gdx.graphics.getHeight()/2;
		airshipY = Gdx.graphics.getHeight()/3;

		shapeRenderer =new ShapeRenderer();

		airshipCircle = new Circle();
		enemyCircle1 = new Circle[numberOfEnemiesSet];
		enemyCircle2 = new Circle[numberOfEnemiesSet];
		enemyCircle3 = new Circle[numberOfEnemiesSet];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);

		// enemy initilatoin işlemi
		for (int i =0; i < numberOfEnemiesSet ;i++){
			// enemy y eksenini oluşturmak için kullanılan komut
			enemyOffset1[i] = (random.nextFloat()) * (Gdx.graphics.getHeight()-200);
			enemyOffset2[i] = (random.nextFloat()) * (Gdx.graphics.getHeight()-200);
			enemyOffset3[i] = (random.nextFloat()) * (Gdx.graphics.getHeight()-200);

			enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth()/2 + i * enemyDistance;

			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}


	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backrpound,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		//Eğer oyun başladıysa...
		if (gameState==1){

			// score hesaplama
			if(enemyX[scoreEnemy] < Gdx.graphics.getWidth() / 2 - airship.getHeight() / 2){
				score++;
				if (scoreEnemy < numberOfEnemiesSet-1){
					scoreEnemy++;
				}else{
					scoreEnemy = 0;
				}
			}


			// uçağın,ekran her tıklandığında zıplama hızı ayarlanıyor
			if (Gdx.input.justTouched()){
				velocity = -7;
			}
			// sonsuz enemy gelmesi ayarlanıyor (ekran dışına set çıktıkça random başa sarma işlemi)
			for (int i=0; i < numberOfEnemiesSet ; i++){
				if (enemyX[i] < 0){
					enemyX[i] = enemyX[i] + numberOfEnemiesSet * enemyDistance ;

					enemyOffset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

				}
				else{
					enemyX[i] =enemyX[i] - enemyVelocity;
				}

				enemyX[i] = enemyX [i] -enemyVelocity;

				batch.draw(enemy1,enemyX[i],(Gdx.graphics.getHeight()/2) + enemyOffset1[i], Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(enemy2,enemyX[i],(Gdx.graphics.getHeight()/2) + enemyOffset2[i], Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(enemy3,enemyX[i],(Gdx.graphics.getHeight()/2) + enemyOffset3[i], Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

				enemyCircle1[i] =new Circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);
				enemyCircle2[i] =new Circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);
				enemyCircle3[i] =new Circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);
			}







			// oyun başladığında uça aşağı düşmeye başlıyor
			if (airshipY > 0 ){
				velocity = velocity + gravity;
				airshipY = airshipY-velocity;
			}else{
				gameState = 2;
			}


		}else if(gameState == 0){
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}else if (gameState == 2 ){
			font2.draw(batch,"Game Over!, Tap To Play Again",100,Gdx.graphics.getHeight()/2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				airshipY = Gdx.graphics.getHeight() / 3;

				// enemy initilatoin işlemi
				for (int i = 0; i < numberOfEnemiesSet; i++) {
					// enemy y eksenini oluşturmak için kullanılan komut
					enemyOffset1[i] = (random.nextFloat()) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat()) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat()) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * enemyDistance;

					enemyCircle1[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();
				}
				velocity = 0;
				score =0;
				scoreEnemy =0;
			}
		}


		batch.draw(airship,airshipX,airshipY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
		font.draw(batch,String.valueOf(score),100,200);

		batch.end();

		airshipCircle.set(airshipX ,airshipY ,Gdx.graphics.getWidth()/30);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(airshipX + Gdx.graphics.getWidth()/30,airshipY + Gdx.graphics.getHeight()/20,airshipCircle.radius);



	    for (int i = 0; i < numberOfEnemiesSet; i++){
	    	//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30,(Gdx.graphics.getHeight()/2) + enemyOffset3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth()/30);

			// çarpışmalar burada kontrol ediliyor
			if(Intersector.overlaps(airshipCircle,enemyCircle1[i]) || Intersector.overlaps(airshipCircle,enemyCircle2[i]) || Intersector.overlaps(airshipCircle,enemyCircle3[i])){
				//System.out.println("collision detection");
				gameState = 2;
			}
	    }

		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}

package com.me.snake;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SnakeGameOld implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	static final int rectangleSize = 64;
	Texture startRecTexture;
	Rectangle startRec;
	
	Array<Rectangle> rectangleTail;
	
	
	
	char lastPressed;
	
	
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		//camera = new OrthographicCamera(1, h/w);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		
		/*texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);*/
		
		
		startRecTexture = new Texture(Gdx.files.internal("greensquare.png"));
		
				
		startRec = new Rectangle();
		startRec.x = 800/2 - rectangleSize/2;
		startRec.y = 480/2;
		startRec.width = rectangleSize;
		startRec.height = rectangleSize;
		
		
		rectangleTail = new Array<Rectangle>();
		add1ToTail();
		
			
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//sprite.draw(batch);
		
		batch.draw(startRecTexture, startRec.x, startRec.y);
		
		Iterator<Rectangle> tailIter = rectangleTail.iterator();
		while(tailIter.hasNext())
		{
			Rectangle curTailPart = tailIter.next();
			batch.draw(startRecTexture, curTailPart.x, curTailPart.y);
		}
		batch.end();
		
		
		
		switch(lastPressed)
		{
			case 'l': startRec.x -= 200 * Gdx.graphics.getDeltaTime();
					   //startRec.x -= rectangleSize * Gdx.graphics.getDeltaTime();
						break;
			case 'r': startRec.x += 200 * Gdx.graphics.getDeltaTime();
					  //startRec.x += rectangleSize  * Gdx.graphics.getDeltaTime();
						break;
			case 'u': startRec.y += 200 * Gdx.graphics.getDeltaTime();
				      //startRec.y += rectangleSize  * Gdx.graphics.getDeltaTime();
						break;
			case 'd': startRec.y -= 200 * Gdx.graphics.getDeltaTime();
						//startRec.y -= rectangleSize  * Gdx.graphics.getDeltaTime();
						break;
						
			default: break;
		
		}
		
		
		if(Gdx.input.isKeyPressed(Keys.LEFT) && lastPressed != 'r')
			{
			//startRec.x -= 200 * Gdx.graphics.getDeltaTime();
			lastPressed = 'l';
			}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)  && lastPressed != 'l') 
		{
			//startRec.x += 200 * Gdx.graphics.getDeltaTime();
			lastPressed = 'r';
		}
		if(Gdx.input.isKeyPressed(Keys.UP)  && lastPressed != 'd') {
			//startRec.y += 200 * Gdx.graphics.getDeltaTime();
			lastPressed = 'u';
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) && lastPressed != 'u'){
			//startRec.y -= 200 * Gdx.graphics.getDeltaTime();
			lastPressed = 'd';
		}
		
		
		
		if(startRec.x < 0) {
			startRec.x=0;
			lastPressed = ' ' ;
		}
		if(startRec.x > 800-rectangleSize) {
			startRec.x=800-rectangleSize;
			lastPressed = ' ';
		}
		
		if(startRec.y < 0) {
			startRec.y=0;
			lastPressed = ' ' ;
		}
		if(startRec.y > 480-rectangleSize) {
			startRec.y=480-rectangleSize;
			lastPressed = ' ';
		}
		
		
		
		Iterator<Rectangle> tailUpdIter = rectangleTail.iterator();
		while(tailUpdIter.hasNext())
		{
			Rectangle curTailPart = tailUpdIter.next();
			
			updTailPart(startRec.x, startRec.y, curTailPart, lastPressed);
			
			//raindrop.y -= 200* Gdx.graphics.getDeltaTime();
			//if(raindrop.y + 64 < 0) iter.remove();
			//if (raindrop.overlaps(bucket))
			//	{
			//		bloopSnd.play();
			//		iter.remove();
			//	}
		
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
	
	private void add1ToTail()
	{
		Rectangle newTailPart = new Rectangle();
		newTailPart.x = startRec.x;// startRec.x - rectangleSize;
		newTailPart.y = startRec.y;//startRec.y - rectangleSize;
		newTailPart.width = rectangleSize;
		newTailPart.height = rectangleSize;
		rectangleTail.add(newTailPart);
		//lastDrop = TimeUtils.nanoTime();
		
	}
	
	private void updTailPart(float priorX, float priorY, Rectangle curTailPart, char lastDir)
	{
		

	
		
		
		//curTailPart.x = priorX - rectangleSize;
		//curTailPart.y = priorY - rectangleSize;
	}
	
	
	
	
}

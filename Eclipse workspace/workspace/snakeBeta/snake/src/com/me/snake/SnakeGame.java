package com.me.snake;

import java.util.Iterator;
//import java.util.prefs.Preferences;






import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

public class SnakeGame implements ApplicationListener {

	private static final boolean inDebugModeFlag = false;
	
	//NOTE: Set the playarea Y fields for ads/no ads
	
	
	
	//private static final boolean adsON = true;
	
	
	
	//For camera and sprite batch
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
		
	//Screen size info
	static final int screenWidth = 800;
	static final int screenHeight = 480;
	//Play area bounds on screen
	static final int playAreaXmin = screenWidth/8;
	static final int playAreaXmax = screenWidth - screenWidth/8;
	
	static final int playAreaYmin = 0;		//set this to 0 for no ads on bottom
	//static final int playAreaYmin = 50;	//set this to 50 for ads on bottom
	
	static final int playAreaYmax = screenHeight; //set this to screenHeigh for no ads on top
	//static final int playAreaYmax = screenHeight-50; //set this to screenHeigh for no ads on top
	
	//Size of rectangles
	static final int rectangleSize = 16;
	static final int growSize = 16;
	
	
	//Textures
	Texture snakeHeadRecTexture;
	Texture snakeHeadOpenRecTexture;
	Texture snakeTailTexture;
	Texture growTexture;
	Texture spikeWallTexture;
	Texture backgroundTexture;
	Texture black512Texture;
	Texture arrowTexture;
	Texture bombTexture;
	Texture bombButton;
	
	Texture titleScreenTexture;
	Texture optionScreenTexture;
	Texture playBtnTexture;
	Texture pauseBtnTexture;
	Texture restartBtnTexture;
	Texture optionsBtnTexture;
	
	
	Texture sndOnBtnTexture;
	Texture sndOffBtnTexture;
	
	boolean SoundFlag;
	Sound yumSnd;
	Sound bombSnd;
	long lastTimeSoundWasTouched;
	
	
	//For spike walls
	Array<Rectangle> spikeWallArray;
	//For the snake tail
	Array<Rectangle> rectangleTail;
	
	
	//Rectangles on screen	
	Rectangle nextLocation;
	Rectangle currentLocation;
	
	Rectangle snakeHeadRec;
	Rectangle growLocation;
	Rectangle bombRec;
		
	//For determining locations for the snake array
	float lastXinTheTail;
	float lastYinTheTail;
	
	
	//For determining different actions
	private int scoreTotal;
	int lastNumOfGrowPillsEaten;
	int growPillsEaten;
	long nanosecondsToRecalc;  //records how long since last render
	long timeForUpdate;		//should we update?
	boolean gameOver;
	boolean afterFirstMove;  //Has the snake started moving
    boolean growPillActive;
    static boolean gamePaused;	
    char lastPressed;  //last direction input
    boolean openCloseHeadFlag;  //controls head animation
    int hiScore; //hi score from storage
    
    //fixing pause button
    long lastTimePauseWasTouched;
    
    
    boolean titleScreenActive;//should the title screen be up
    
    boolean bombOnField;
    boolean hasBomb;
	
	//For displaying messages
	private String gameOverStr;
	private String pauseStr;
	private String restartStr;
	private String quitStr;
	private String gamePausedDisplayStr;
	
	private String scoreStr;
	private String hiScoreStr;
	private String multiplierStr;
	
	
	
	//for debugging coords
	

	private String debugX;
	private String debugY;
	
	private String debugTouchMsgStr;
	
	
	//for text
	BitmapFont FontName;
		
	//for lines
	ShapeRenderer shapeRenderer;
	
	//for saving things
	
	Preferences prefStorage;
	
	
	//initialize game
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		gamePaused = false;
		
		
		//camera = new OrthographicCamera(1, h/w);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		batch = new SpriteBatch();
		
		/*texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);*/
		
		//Load Images into Textures
		snakeHeadRecTexture = new Texture(Gdx.files.internal("snakeImg/snakeHeadClose16.png"));
		snakeHeadOpenRecTexture= new Texture(Gdx.files.internal("snakeImg/snakeHeadOpen16.png"));
		snakeTailTexture= new Texture(Gdx.files.internal("snakeImg/snakeTail16.png"));
		growTexture = new Texture(Gdx.files.internal("snakeImg/growPill16.png"));
		spikeWallTexture =  new Texture(Gdx.files.internal("snakeImg/spikeWall16.png"));
		backgroundTexture = new Texture(Gdx.files.internal("snakeImg/grass1024.png"));
		black512Texture = new Texture(Gdx.files.internal("snakeImg/black512.png"));
		arrowTexture = new Texture(Gdx.files.internal("snakeImg/arrowUp32.png"));
		bombTexture = new Texture(Gdx.files.internal("snakeImg/bomb16.png"));
		bombButton = new Texture(Gdx.files.internal("snakeImg/bomb64.png"));
		
		
		
		 titleScreenTexture= new Texture(Gdx.files.internal("snakeImg/snakeTitleScreen1024.png"));
		 optionScreenTexture= new Texture(Gdx.files.internal("snakeImg/snakeSettingsScreen1024.png"));
		 playBtnTexture= new Texture(Gdx.files.internal("snakeImg/snakePlayBtn64.png"));
		 pauseBtnTexture= new Texture(Gdx.files.internal("snakeImg/snakePauseBtn64.png"));
		 restartBtnTexture= new Texture(Gdx.files.internal("snakeImg/snakeRestartBtn64.png"));
		 optionsBtnTexture= new Texture(Gdx.files.internal("snakeImg/snakeOptnBtn64.png"));
		
		
		 //Set Sounds
		 sndOnBtnTexture= new Texture(Gdx.files.internal("snakeImg/sndOnBtn64.png"));
		 sndOffBtnTexture= new Texture(Gdx.files.internal("snakeImg/sndOffBtn64.png"));
		 SoundFlag = false;
		 yumSnd = Gdx.audio.newSound(Gdx.files.internal("snakeImg/yum.mp3"));
		 bombSnd = Gdx.audio.newSound(Gdx.files.internal("snakeImg/bomb.mp3"));
		
		
		
		//Set Rectangles
		growLocation = new Rectangle();
		growLocation.width = rectangleSize;
		growLocation.height = rectangleSize;
		
		bombRec = new Rectangle();
		bombRec.width = rectangleSize;
		bombRec.height = rectangleSize;
		
		
		snakeHeadRec = new Rectangle();
		snakeHeadRec.x = screenWidth/2 - rectangleSize/2;
		snakeHeadRec.y = screenHeight/2;
		snakeHeadRec.width = rectangleSize;
		snakeHeadRec.height = rectangleSize;
				
		nextLocation = new Rectangle();
		nextLocation.x = snakeHeadRec.x;
		nextLocation.y = snakeHeadRec.y;
		
		currentLocation = new Rectangle();
		currentLocation.x = snakeHeadRec.x;
		currentLocation.y = snakeHeadRec.y;
		
		
		rectangleTail = new Array<Rectangle>();
		spikeWallArray = new Array<Rectangle>();
		
	
		
		
		
		
		//Set Message info
	    FontName = new BitmapFont();
	    gameOverStr = "Game Over";
	    pauseStr = "PAUSE";
	    gamePausedDisplayStr = "Game is Paused";
	    restartStr = "RESTART";
	    
	    
	    //set title screen to active;
	    titleScreenActive = true;
	    
	    gameOver = false;
	    afterFirstMove=false;
	    lastPressed= ' ';
	    scoreTotal = 0;
	    openCloseHeadFlag = false;
	    growPillActive = false;
	    hasBomb = false;
	    bombOnField = false;

	    //fixing pause button
	    lastTimePauseWasTouched = 0;
	    lastTimeSoundWasTouched = 0;
	    
	    
	    //nanosecondsToRecalc = 1000000000;
		nanosecondsToRecalc = 250000000;
	  	
		lastXinTheTail = -1;
	  	lastYinTheTail = -1;
	  	lastNumOfGrowPillsEaten = 0;
	  	growPillsEaten= 0;
	    
	    //for drawing lines
	    shapeRenderer= new ShapeRenderer();
	    
	    //get scores from preferences
	    prefStorage = Gdx.app.getPreferences("Preferences");
	    hiScore = prefStorage.getInteger("hiScore", 0);
	    
	    
	    //Setting score/multiplier strings 
	  //update the score string
	  		scoreStr = "SCORE: ";
	  		hiScoreStr = "Hi Score: " ;
	  		
	  		
	  		
	  		//debug
	  		
	  		debugX = "0";
	  		debugY = "0";
	  		
	  		debugTouchMsgStr="";
	  		
	  		Gdx.input.setCatchBackKey(true);
	    
	}

	@Override
	public void dispose() {
		snakeHeadRecTexture.dispose();
		growTexture.dispose();
		batch.dispose();
		texture.dispose();

		
	}
//This is the Main loop
	@Override
	public void render() {		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		//do this stuff in all games states
		if(Gdx.input.isKeyPressed(Keys.R))
		{
		 	restart();
		}
		if(Gdx.input.isKeyPressed(Keys.P))
		{
			pause();
		
		}
		if(Gdx.input.isKeyPressed(Keys.BACK))
		{
			
			pause();
			
		
		}
		
		//check if the snake got a grow pill
		if( growPillCollisionCheck() == false)
			 growPillActive = false;
		
		
		//check to see if snake has gotten bomb
		if( bombOnField )
		{
			if(  bombCollisionCheck() ==false)
			{
				hasBomb = true;
				bombOnField = false;
			}
		}
		
	
		//update strings
		multiplierStr = "Current Multiplier " + getMultiplier() + "X";
		
		
		
		//draw lines
		 Gdx.gl10.glLineWidth(2);
		 shapeRenderer.setProjectionMatrix(camera.combined);
		 
		 if(!titleScreenActive)
			{
		 
		   // menu line left vertical
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmin, playAreaYmin,playAreaXmin, playAreaYmax);
		  shapeRenderer.end(); 
			  
		  
		  // menu line right vertical
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmax, playAreaYmin,playAreaXmax, playAreaYmax);
		  shapeRenderer.end(); 
		  
		  
		  // Top of play
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmin,playAreaYmax,playAreaXmax, playAreaYmax);
		  shapeRenderer.end(); 
			  
		  
		  // bottom of play
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmin, playAreaYmin ,playAreaXmax, playAreaYmin);
		  shapeRenderer.end(); 
			}
		  
		  
		  if(inDebugModeFlag)
			{
		  
		  //top left horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmin, playAreaYmin,playAreaXmax, playAreaYmax);
		  shapeRenderer.end(); 
		   
		  //bottom left horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmin,  playAreaYmax,playAreaXmax, playAreaYmin);
		  shapeRenderer.end(); 
			}
		  
		  
		  
		  
		  /* 			  
		   
		  //top left horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(0, screenHeight*3/4,playAreaXmin, screenHeight*3/4);
		  shapeRenderer.end(); 
		   
		  //bottom left horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(0, screenHeight*1/4,playAreaXmin, screenHeight*1/4);
		  shapeRenderer.end(); 
		   
		   
		   
		  //top right horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmax, screenHeight/3*2,screenWidth, screenHeight/3*2);
		  shapeRenderer.end(); 
		   
		  //bottom right horizontal
		  shapeRenderer.begin(ShapeType.Line);
		  shapeRenderer.setColor(1, 1, 1, 1);
		  shapeRenderer.line(playAreaXmax, screenHeight/3,screenWidth, screenHeight/3);
		  shapeRenderer.end(); 
		  */
		  
		   
	     
		//Begin update batch for UI
		//-----------------------------------------------------------------------
		batch.begin();

		
		if(titleScreenActive)
		{
			batch.draw(titleScreenTexture,0,0);
			
		}
		else
		{
			
		
		
			//Draw background
			if(!inDebugModeFlag)
			{
				if(playAreaYmax!=screenHeight)
					batch.draw(backgroundTexture, playAreaXmin, playAreaYmin-(screenHeight-playAreaYmax));
					
				else	
				batch.draw(backgroundTexture, playAreaXmin, playAreaYmin);
			 
/*			Pixmap pixmap = new Pixmap(512, 64, Format.RGBA8888);
			pixmap.setColor(Color.BLACK); // add your 1 color here
			pixmap.fillRectangle(0, 0, 32, 32);
			
			// the outcome is an texture with an blue left square and an red right square
			Texture t = new Texture(pixmap);
			TextureRegion reg1 = new TextureRegion(t, 0, 0, playAreaXmax - playAreaXmin,screenHeight-playAreaYmax);
			
			
			batch.draw(reg1, screenWidth/2 ,playAreaYmax/2);
			*/
			
			}
			//batch.draw(black512Texture, playAreaXmax, playAreaYmin);
		
			//draw Arrows
			TextureRegion arrowRegion = new TextureRegion(arrowTexture,0,0,32,32);
		
			//left
			batch.draw(arrowRegion, playAreaXmin, playAreaYmax-((playAreaYmax - playAreaYmin)/2), rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 0f, false);
			//right
			batch.draw(arrowRegion, playAreaXmax-rectangleSize,playAreaYmax-((playAreaYmax - playAreaYmin)/2), rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 180f, false);
			
			
			//up right
			batch.draw(arrowRegion, screenWidth/2, playAreaYmax-rectangleSize, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 270f, false);
			
			//down left
			batch.draw(arrowRegion, screenWidth/2, playAreaYmin, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 90f, false);
			
			
			/*//up left
			batch.draw(arrowRegion, screenWidth/3, playAreaYmax-rectangleSize, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 270f, false);
			
			//up right
			batch.draw(arrowRegion, screenWidth/3*2, playAreaYmax-rectangleSize, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 270f, false);
			
			//down left
			batch.draw(arrowRegion, screenWidth/3, playAreaYmin, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 90f, false);
		
			//down right
			batch.draw(arrowRegion, screenWidth/3*2, playAreaYmin, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 90f, false);*/
		
		
		
			//Draw Text for pause, restart, score
			
			//FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			//FontName.draw(batch, pauseStr, 0, screenHeight);
			
			if(gamePaused)
			{
				batch.draw(playBtnTexture, 20, playAreaYmax-80);
			}
			else
			{
				batch.draw(pauseBtnTexture, 20, playAreaYmax-80);
			}
			
			
			if(SoundFlag)
			{
				batch.draw(sndOnBtnTexture, 20, playAreaYmax-((playAreaYmax - playAreaYmin)/2)-20);
			}
			else
			{
				batch.draw(sndOffBtnTexture, 20, playAreaYmax-((playAreaYmax - playAreaYmin)/2)-20);
			}
			
			
			
			
			//FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			//FontName.draw(batch, restartStr, 0, screenHeight * 1/4);
			batch.draw(restartBtnTexture,20, playAreaYmin+20);
		
			
			// region - draw scores and multiplier
			FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			FontName.draw(batch, hiScoreStr, playAreaXmax+5, playAreaYmax- rectangleSize);
			
			FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			FontName.draw(batch,  " "+hiScore , playAreaXmax+5, playAreaYmax- rectangleSize*2);
			
			
			FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			FontName.draw(batch, scoreStr, playAreaXmax+5, playAreaYmax- rectangleSize*3);
			
			FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			FontName.draw(batch, " "+scoreTotal, playAreaXmax+5, playAreaYmax- rectangleSize*4);
		
		
			
			FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			FontName.draw(batch, multiplierStr, screenWidth/2 -40, playAreaYmax- rectangleSize);

			if(inDebugModeFlag)
			{
				FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				FontName.draw(batch, debugTouchMsgStr, playAreaXmin,playAreaYmax- rectangleSize*2);
			}
			
			
			// endregion

		
			if(! gamePaused )	
			{
					
				if(!gameOver)
				{
					//draw grow pill
					if(growPillActive)
					{
						batch.draw(growTexture, growLocation.x, growLocation.y);
					}	
					//draw bomb
					if(bombOnField)
					{
						batch.draw(bombTexture, bombRec.x, bombRec.y);
					}	
					//draw bomb button 
				
					if(hasBomb)
					{
						batch.draw(bombButton, playAreaXmax+20, playAreaYmax/3 +35);
					}
				
				
					//draw spike walls
					Iterator<Rectangle> spikeWallIter = spikeWallArray.iterator();
					while(spikeWallIter.hasNext())
					{
						Rectangle curSpikeWall = spikeWallIter.next();
						batch.draw(spikeWallTexture, curSpikeWall.x, curSpikeWall.y);
					}
				
				
				
					//Rotate head depending on direction and draw
					//	
				
					if(lastPressed == ' ')
						batch.draw(snakeHeadRecTexture, snakeHeadRec.x, snakeHeadRec.y);
					
					TextureRegion snakeHeadregion;
					if(openCloseHeadFlag)
						snakeHeadregion= new TextureRegion(snakeHeadRecTexture,0,0,rectangleSize,rectangleSize);
					else
						snakeHeadregion= new TextureRegion(snakeHeadOpenRecTexture,0,0,rectangleSize,rectangleSize);
								
					switch(lastPressed)
					{
								
						case 'l': batch.draw(snakeHeadregion, snakeHeadRec.x, snakeHeadRec.y, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 90f, false);
							break;
						case 'r': batch.draw(snakeHeadregion, snakeHeadRec.x, snakeHeadRec.y, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 270f, false);
							break;
						case 'u':batch.draw(snakeHeadregion, snakeHeadRec.x, snakeHeadRec.y, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 0f, false);
							break;
						case 'd':batch.draw(snakeHeadregion, snakeHeadRec.x, snakeHeadRec.y, rectangleSize / 2f, rectangleSize / 2f, rectangleSize, rectangleSize, 1, 1, 180f, false);
							break;
						default: break;
		
					}
				
					//end head rotation section		
				
				
					//draw snake tail	
					Iterator<Rectangle> tailIter = rectangleTail.iterator();
					while(tailIter.hasNext())
					{
						Rectangle curTailPart = tailIter.next();
						batch.draw(snakeTailTexture, curTailPart.x, curTailPart.y);
					}
				
				
				}
				else{
				//you lost, calculate scores
					if(scoreTotal > hiScore) 
					{
						hiScore = scoreTotal;
						prefStorage.putInteger("hiScore", hiScore);
						prefStorage.flush();
					}
				
				
					//draw game over if you lose
					FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
					FontName.draw(batch, gameOverStr, screenWidth/2-20, playAreaYmax/2);
		
				}
		
		
			}
			else
			{
			//draw game paused if pause
				FontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				FontName.draw(batch, gamePausedDisplayStr, screenWidth/2-20, playAreaYmax/2);
		   
			}
		
		}
		batch.end();
		
		//end Draw
		
		
		//begin calc on what to do next
		if(!gamePaused)
		{
			if(!gameOver && afterFirstMove && !titleScreenActive)
			{
				/*//if snake goes out of bounds then fail
				if (snakeHeadRec.x == playAreaXmin || snakeHeadRec.x > playAreaXmax)
					gameOver = true;
				if (snakeHeadRec.y == playAreaYmin|| snakeHeadRec.y == playAreaXmax - rectangleSize)
					gameOver = true;*/
			
				//if snake touches its tail then fail
				Iterator<Rectangle> tailUpdIter = rectangleTail.iterator();
				while(tailUpdIter.hasNext())
				{		
					Rectangle curTailPart = tailUpdIter.next();
					
					if(snakeHeadRec.overlaps(curTailPart)||currentLocation.overlaps(curTailPart)||nextLocation.overlaps(curTailPart))
						gameOver = true;
				}
				
				//if the snake touches a spike wall you lose
				Iterator<Rectangle> spikeWallIter = spikeWallArray.iterator();
				while(spikeWallIter.hasNext())
				{
					Rectangle curSpikeWall = spikeWallIter.next();
					if(snakeHeadRec.overlaps(curSpikeWall)||currentLocation.overlaps(curSpikeWall)||nextLocation.overlaps(curSpikeWall))
						gameOver = true;
				}

			}

		
			//update this stuff every second(-your score total)
			if(TimeUtils.nanoTime() - timeForUpdate > (nanosecondsToRecalc - (100000*scoreTotal) ) )
			{
				//flip this to make the mouth open/close
				if(openCloseHeadFlag)
					openCloseHeadFlag=false;
				else
					openCloseHeadFlag = true;
				
				
			//update this section if the snake got a grow pill
				if(!growPillActive)
				{
					
					spawnGrowPill();
					growPillActive = true;
				
					growPillsEaten++;
					if(growPillsEaten>1)
					{
						//sound plays here to signify that a pill was just eaten
						if(SoundFlag)
							yumSnd.play();
						
						
						add1ToTail();
						scoreTotal+= (10 * getMultiplier());
					}
					
					
					if(growPillsEaten%3 ==0)
					{
						addToSpikeWall();
					}
						
				 }	
				
				
				//spawnBombl
				if(scoreTotal > 1 && growPillsEaten%7==0)
				{
					
					if(!hasBomb && bombOnField == false)
					{
						spawnBomb();
						bombOnField=true;
					}
					
				}
				
				
				
			
			    //update time to calc against
				timeForUpdate = TimeUtils.nanoTime();
			
				//set current location of snakehead
				currentLocation.x = snakeHeadRec.x;
				currentLocation.y = snakeHeadRec.y;
			
				//determine next location based off the last input
				switch(lastPressed)
				{
					case 'l': //snakeHeadRec.x -= 200 * Gdx.graphics.getDeltaTime();
							nextLocation.x -= rectangleSize;					
							//snakeHeadRec.x -= rectangleSize;
							break;
					case 'r':// snakeHeadRec.x += 200 * Gdx.graphics.getDeltaTime();
					    	nextLocation.x += rectangleSize;
					  	//snakeHeadRec.x += rectangleSize ;
					    	break;
					case 'u': //snakeHeadRec.y += 200 * Gdx.graphics.getDeltaTime();
							nextLocation.y += rectangleSize;
				      //	snakeHeadRec.y += rectangleSize  ;
							break;
					case 'd': //snakeHeadRec.y -= 200 * Gdx.graphics.getDeltaTime();
							nextLocation.y -= rectangleSize;
					//	snakeHeadRec.y -= rectangleSize ;
							break;
						
					default: break;
		
				}
			
				//update where the snake rectangles will be next render
				updateSnakeLocations();
			
			
		
			};
			
			//If not on the title screen  check for input
			if(!titleScreenActive)
			{
			
				//key press check/update - Set the most recent direction from keyboard
				if(Gdx.input.isKeyPressed(Keys.LEFT) && lastPressed != 'r')
				{
				//snakeHeadRec.x -= 200 * Gdx.graphics.getDeltaTime();
					afterFirstMove= true;
					lastPressed = 'l';
				}
				if(Gdx.input.isKeyPressed(Keys.RIGHT)  && lastPressed != 'l') 
				{
				//snakeHeadRec.x += 200 * Gdx.graphics.getDeltaTime();
					afterFirstMove= true;
					lastPressed = 'r';
				}
				if(Gdx.input.isKeyPressed(Keys.UP)  && lastPressed != 'd') {
				//snakeHeadRec.y += 200 * Gdx.graphics.getDeltaTime();
					afterFirstMove= true;
					lastPressed = 'u';
				}
				if(Gdx.input.isKeyPressed(Keys.DOWN) && lastPressed != 'u'){
			//snakeHeadRec.y -= 200 * Gdx.graphics.getDeltaTime();
					afterFirstMove= true;
					lastPressed = 'd';
				}
			
				if(Gdx.input.isKeyPressed(Keys.SPACE) && hasBomb){
					
						bombExplode();
					}
			
			
			
			
			
		
		//touch controls check/update -- set the most recent direction from touch
				if(Gdx.input.isTouched())
				{
					Vector3 touchPosition= new Vector3();
					touchPosition.set(Gdx.input.getX(), Gdx.input.getY(),0);
					camera.unproject(touchPosition);
			
			
				
					touchInputLogic(touchPosition);
				}
		
			}
			else{
				
				//start on the title screen
					if(Gdx.input.isTouched())
					{
						Vector3 touchPosition= new Vector3();
						touchPosition.set(Gdx.input.getX(), Gdx.input.getY(),0);
						camera.unproject(touchPosition);
				
				
						if(touchPosition.x > screenWidth/3 && touchPosition.x <(screenWidth/3)*2 && touchPosition.y < playAreaYmax/2  ) 
						{
				
							restart();
					
						}
					}
			
			}
				
			
			
			
		
		//check to make sure snake is in bounds
			if(snakeHeadRec.x < playAreaXmin) {
				snakeHeadRec.x=playAreaXmin;
				lastPressed = ' ' ;
				gameOver = true;
			}
			if(snakeHeadRec.x > playAreaXmax-rectangleSize/2) {
				snakeHeadRec.x=playAreaXmax-rectangleSize/2;
				lastPressed = ' ';
				gameOver = true;
			}
		
			if(snakeHeadRec.y < playAreaYmin) {
				snakeHeadRec.y=playAreaYmin;
				lastPressed = ' ' ;
				gameOver = true;
			}
			if(snakeHeadRec.y > playAreaYmax-rectangleSize/2) {
				snakeHeadRec.y=playAreaYmax-rectangleSize/2;
				lastPressed = ' ';
				gameOver = true;
			}
		
		}
		
		
		
		//menu button touch
		
		if(Gdx.input.isTouched())
		{
			Vector3 touchPosition= new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(),0);
			camera.unproject(touchPosition);
			//bucket.x = touchPosition.x-64/2;
			
			if (touchPosition.x>0 && touchPosition.x < playAreaXmin && touchPosition.y > playAreaYmax*(.75) && touchPosition.y < playAreaYmax )
			{	
				pause();
			}
			

			
			if (touchPosition.x>0 && touchPosition.x < playAreaXmin && touchPosition.y < playAreaYmax*(.75) && touchPosition.y > (playAreaYmax/4))
				soundControl();
			
		
			if (touchPosition.x>0 && touchPosition.x < playAreaXmin && touchPosition.y > playAreaYmin && touchPosition.y < (playAreaYmax/4))
				restart();
		
			//Explode bomb
			if(!gameOver & !gamePaused & hasBomb)
			{
			  if (touchPosition.x > playAreaXmax && touchPosition.y > playAreaYmax/3 && touchPosition.y< (playAreaYmax/3)*2 )
				bombExplode();
			}
			
		}
		
		
		
		
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		
	   // lastPressed= ' ';
		if(TimeUtils.millis() > lastTimePauseWasTouched + 500)
		{
			if(gamePaused)
				gamePaused = false;
			else
				gamePaused = true;
		
		lastTimePauseWasTouched = TimeUtils.millis();
		
		}
		
	}

	@Override
	public void resume() {
	}
	
	
	public void restart() {
	
		create();
		titleScreenActive = false;
		lastPressed = ' ';
	}
	
	
	public void soundControl()
	{
		if(TimeUtils.millis() > lastTimeSoundWasTouched + 500)
		{
			if(SoundFlag)
				SoundFlag = false;
			else
				SoundFlag = true;
		
			lastTimeSoundWasTouched = TimeUtils.millis();
		
		}
		
	}
	
	public void closeGame()
	{
		//dispose();
		Gdx.app.exit();
		
	}
	
	//update the x y coordinates for all tail parts
	private void updateSnakeLocations()
	{

		snakeHeadRec.x = nextLocation.x;
		snakeHeadRec.y = nextLocation.y;
		
		float nextX = currentLocation.x;
		float nextY = currentLocation.y;
		float curX = 0;
		float curY = 0;
		
		Iterator<Rectangle> tailUpdIter = rectangleTail.iterator();
		while(tailUpdIter.hasNext())
		{		
			Rectangle curTailPart = tailUpdIter.next();
			
			
			curX = curTailPart.x;
			curY = curTailPart.y;
			
			updTailPart(nextX, nextY, curTailPart, lastPressed);
		
			nextX = curX;
			nextY=curY;
			
			
			
			lastXinTheTail = curTailPart.x;
			lastYinTheTail = curTailPart.y;
			
			
		}
	}
	
	
	
	private boolean isWithinPlayBounds(float x, float y)
	{
		boolean within = true;
		
		
		if( x > playAreaXmax || x < playAreaXmin)
			within = false;
		
		if( y > playAreaYmax || y < playAreaYmin)
			within = false;
	
		return within;
		
	}
	
	
	
	private void touchInputLogic(Vector3 touchPosition)
	{
		//print debug message
		
		 boolean withinPlayBounds = isWithinPlayBounds(touchPosition.x, touchPosition.y);
		
		 debugX = ""+touchPosition.x;
		 debugY = ""+touchPosition.y;
		
		 debugTouchMsgStr = "X: "+ debugX +" , Y: "+ debugY;
		
		
		
		double playAreaHeight = playAreaYmax - playAreaYmin;
		double playAreaWidth = playAreaXmax - playAreaXmin;
		
	   //formulas
		//  ------   / line
		double bottomLeftToTopRight = playAreaYmin + (touchPosition.x - playAreaXmin) *(playAreaHeight/playAreaWidth);// = (touchPosition.x - playAreaXmin)*( (playAreaYmax + ( playAreaYmax/6)) / playAreaXmax );
		// ------------ \ line
		double topLeftToBottomRight = playAreaYmin + (-(touchPosition.x) + playAreaXmax)*(playAreaHeight/playAreaWidth);//= (-(touchPosition.x)+ playAreaXmax)*( (playAreaYmax + ( playAreaYmax/6)) / playAreaXmax );
		
		
		 debugX = ""+touchPosition.x;
		 debugY = ""+touchPosition.y;
		
		 debugTouchMsgStr = "X: "+ debugX +" , Y: "+ debugY + " --/ = " + bottomLeftToTopRight + " , --\\ = " + topLeftToBottomRight ;
		

		
		if(touchPosition.y > bottomLeftToTopRight &&  touchPosition.y < topLeftToBottomRight && lastPressed != 'r' && withinPlayBounds)
		{
			afterFirstMove= true;
			lastPressed = 'l';
		}
		
		if(touchPosition.y < bottomLeftToTopRight &&  touchPosition.y > topLeftToBottomRight  && lastPressed != 'l' && withinPlayBounds ) 
		{
		
			afterFirstMove= true;
			lastPressed = 'r';
		}
		
		if(touchPosition.y > bottomLeftToTopRight &&  touchPosition.y > topLeftToBottomRight  && lastPressed != 'd' && withinPlayBounds ) 
		{			
			afterFirstMove= true;
			lastPressed = 'u';
		}
		
		if(touchPosition.y < bottomLeftToTopRight &&  touchPosition.y < topLeftToBottomRight && lastPressed != 'u' && withinPlayBounds ) 
		{
		
			afterFirstMove= true;
			lastPressed = 'd';
		}
		
		
		
		
		//H controls
/*
		//if(touchPosition.x > screenWidth/6 && touchPosition.x < ((screenWidth-(screenWidth/6))/3)+(screenWidth/6) && touchPosition.y < screenHeight-rectangleSize*2 &&  lastPressed != 'r')
		if(touchPosition.x > playAreaXmin && touchPosition.x < screenWidth/3 && touchPosition.y < screenHeight-rectangleSize*2 &&  lastPressed != 'r')
		{
			afterFirstMove= true;
			lastPressed = 'l';
		}
		//if(touchPosition.x > screenWidth/6 && touchPosition.x > ((screenWidth-(screenWidth/6))/3)*2+(screenWidth/6) && touchPosition.y < screenHeight-rectangleSize*2  && lastPressed != 'l') 
		if(touchPosition.x > (screenWidth/3)*2 && touchPosition.x < playAreaXmax && touchPosition.y < screenHeight-rectangleSize*2  && lastPressed != 'l') 
		{
		
			afterFirstMove= true;
			lastPressed = 'r';
		}
		//if(touchPosition.x > screenWidth/6 && touchPosition.x <((screenWidth-(screenWidth/6))/3)*2+(screenWidth/6)&&touchPosition.x > ((screenWidth-(screenWidth/6))/3)+(screenWidth/6) && touchPosition.y > screenHeight/2  &&touchPosition.y < screenHeight-rectangleSize*2   && lastPressed != 'd') {
		if(touchPosition.x > screenWidth/3 && touchPosition.x <(screenWidth/3)*2 && touchPosition.y > screenHeight/2   && lastPressed != 'd') 
		{			
			afterFirstMove= true;
			lastPressed = 'u';
		}
		//if(touchPosition.x > screenWidth/6 && touchPosition.x < ((screenWidth-(screenWidth/6))/3)*2+(screenWidth/6) &&touchPosition.x > ((screenWidth-(screenWidth/6))/3)+(screenWidth/6) && touchPosition.y < screenHeight/2  && lastPressed != 'u'){
		if(touchPosition.x > screenWidth/3 && touchPosition.x <(screenWidth/3)*2 && touchPosition.y < screenHeight/2   && lastPressed != 'u') 
		{
		
			afterFirstMove= true;
			lastPressed = 'd';
		}
		*/
		
		
		
		
		
		
		
		//old method/H controls - don't use
		/*
		if(touchPosition.x > screenWidth/6 && touchPosition.x < (screenWidth/3) && touchPosition.y < screenHeight-rectangleSize*2 &&  lastPressed != 'r')
		{
	
			afterFirstMove= true;
			lastPressed = 'l';
		}
		if(touchPosition.x > screenWidth/6 && touchPosition.x > (screenWidth/3)*2 && touchPosition.y < screenHeight-rectangleSize*2  && lastPressed != 'l') 
		{
		
			afterFirstMove= true;
			lastPressed = 'r';
		}
		if(touchPosition.x > screenWidth/6 && touchPosition.x < (screenWidth/3)*2 &&touchPosition.x > (screenWidth/3) && touchPosition.y > screenHeight/2  &&touchPosition.y < screenHeight-rectangleSize*2   && lastPressed != 'd') {
			
			afterFirstMove= true;
			lastPressed = 'u';
		}
		if(touchPosition.x > screenWidth/6 && touchPosition.x < (screenWidth/3)*2 &&touchPosition.x > (screenWidth/3) && touchPosition.y < screenHeight/2  && lastPressed != 'u'){
		
			afterFirstMove= true;
			lastPressed = 'd';
		}*/
		
	}
	
	
	
	
	//check for overlaps --Returns false if it overlaps
    private	boolean growPillCollisionCheck()
	{
    	//Rectangle collisionCheck = new Rectangle();
		if(snakeHeadRec.overlaps(growLocation)||currentLocation.overlaps(growLocation)||nextLocation.overlaps(growLocation))
		{
			return false;
		}
		
		
		return true;
		
		
	}
    

	//check for overlaps --Returns false if it overlaps
    private	boolean bombCollisionCheck()
   	{
       	//Rectangle collisionCheck = new Rectangle();
   		if(snakeHeadRec.overlaps(bombRec)||currentLocation.overlaps(bombRec)||nextLocation.overlaps(bombRec))
   		{
   			return false;
   		}
   		
   		
   		return true;
   		
   		
   	}
    
    
	
	//Add a new tail part to the snake
	private void add1ToTail()
	{
		Rectangle newTailPart = new Rectangle();
		

		if (lastXinTheTail == -1 &&	lastYinTheTail ==-1)
		{		
			
			newTailPart.x = snakeHeadRec.x;// snakeHeadRec.x - rectangleSize;
			newTailPart.y = snakeHeadRec.y;//snakeHeadRec.y - rectangleSize;
		}
		else{
			newTailPart.x = lastXinTheTail;
			newTailPart.y = lastYinTheTail;
		}
		
		
		
		newTailPart.width = rectangleSize;
		newTailPart.height = rectangleSize;
		rectangleTail.add(newTailPart);
		//lastDrop = TimeUtils.nanoTime();
		
	}
	
	private void addToSpikeWall()
	{

		float randomX =0;
		float randomY =0 ;
		
		boolean goodLocation = false;
		
		
		
		
		while(goodLocation == false)
		{
			if (randomX != growLocation.x && randomY!=growLocation.y)
			
			{
			randomX =  MathUtils.random(playAreaXmin + growSize, playAreaXmax-growSize);
			randomY = MathUtils.random(playAreaYmin+growSize, playAreaYmax-growSize);
			goodLocation = checkLocation(randomX, randomY);
			}
		}
		
		
		
		
		Rectangle newSpikeWall = new Rectangle();
		newSpikeWall.width = rectangleSize;
		newSpikeWall.height = rectangleSize;
		
		
		newSpikeWall.x = randomX;
		newSpikeWall.y = randomY;
		
		spikeWallArray.add(newSpikeWall);
		
	}
	
	
	
	//set the provided tail part to the desired coordinates
	private void updTailPart(float priorX, float priorY, Rectangle curTailPart, char lastDir)
	{
			curTailPart.x = priorX;
			curTailPart.y = priorY;
	
	}
	
	
	//Set grow pill location
	private void spawnGrowPill()
	{
		
		
		float randomX =0;
		float randomY =0 ;
		
		boolean goodLocation = false;
		
		
		
		
		while(goodLocation == false)
		{
			randomX =  MathUtils.random(playAreaXmin + growSize, playAreaXmax-growSize);
			randomY = MathUtils.random(playAreaYmin+growSize, playAreaYmax-growSize);
			goodLocation = checkLocation(randomX, randomY);
		}
		
		
		growLocation.x = randomX;
		growLocation.y = randomY;
	}
	
	
	
	private void spawnBomb()
	{
		
		
		float randomX =0;
		float randomY =0 ;
		
		boolean goodLocation = false;
		
		
		
		
		while(goodLocation == false)
		{
			randomX =  MathUtils.random(playAreaXmin + growSize, playAreaXmax-growSize);
			randomY = MathUtils.random(playAreaYmin+growSize, playAreaYmax-growSize);
			goodLocation = checkLocation(randomX, randomY);
		}
		
		
		bombRec.x = randomX;
		bombRec.y = randomY;
	}
	
	private void bombExplode()
	{
		if(SoundFlag)
			bombSnd.play();
		
		spikeWallArray.clear();
		
		scoreTotal-= (scoreTotal * .35);
		/*Iterator<Rectangle> spikeWallIter = spikeWallArray.iterator();
		while(spikeWallIter.hasNext())
		{
			spikeWallIter.remove();
		}*/
		
		hasBomb= false;
		
		
	}
	
	
	
	
	
	
	
	
	//determine if the location intersects the snake
	private boolean checkLocation(float targetX, float targetY)
	{
		//build rectangle to represent target location
		Rectangle CompareAgainst = new Rectangle();
		CompareAgainst.height = 16;
		CompareAgainst.width = 16;
		CompareAgainst.x = targetX;
		CompareAgainst.y = targetY;
		
		//check for head
		if(CompareAgainst.overlaps(currentLocation))
			return false;
		
		//check for tail
		Iterator<Rectangle> tailCheckIter = rectangleTail.iterator();
		while(tailCheckIter.hasNext())
		{		
			Rectangle curTailPart = tailCheckIter.next();
			if(CompareAgainst.overlaps(curTailPart))
				return false;
		}
		
		
		//Check For spike wall
		Iterator<Rectangle> spikeCheckIterator = spikeWallArray.iterator();
		while(spikeCheckIterator.hasNext())
		{		
			Rectangle curSpikeWall = spikeCheckIterator.next();
						
			if (CompareAgainst.overlaps(curSpikeWall))
			{
				return false;
			}
		}
		
		
		return true;
	}
	
	// return a multiplier based off of the number of spikewalls on the map
	private int getMultiplier()
	{
		if( spikeWallArray.size > 20)
			return 5;
		else if( spikeWallArray.size > 16)
			return 4;
		else if( spikeWallArray.size > 12)
			return 4;
		else if( spikeWallArray.size > 8)
			return 3;
		else if( spikeWallArray.size > 4)
			return 2;
		else
			return 1;
	
	}
	
	
	
	//May use this enum to clean up the game state later.
	public enum GameState
	{
		
		atTitleScreen(1),
		paused (2),
		inPlay (3);
		
		  private final int value;

		    private GameState(int value) {
		        this.value = value;
		    }

		    public int getValue() {
		        return this.value;
		    }
		
	}
	
	
	
}




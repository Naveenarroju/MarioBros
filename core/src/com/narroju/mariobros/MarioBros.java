package com.narroju.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.narroju.mariobros.Screens.PlayScreen;


public class MarioBros extends Game { //loads and exectues all the Game class
	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIFHT = 208;
	public static final float PPM = 100;

    public static final short DEFAULT_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 2;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this)); // 1. sets the screen of Game class to PlayScreen
	}

	@Override
	public void render () {
		super.render();  //2. render of main Game class is called and as the screen is not null(PlayScreen extending Screen has been passed to it)
	}
	

}

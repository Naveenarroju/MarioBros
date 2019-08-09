package com.narroju.mariobros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.narroju.mariobros.Screens.PlayScreen;


public class MarioBros extends Game { //loads and exectues all the Game class
	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIFHT = 208;
	public static final float PPM = 100;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this)); // sets the screen of Game class to PlayScreen
	}

	@Override
	public void render () {
		super.render();  //render of Game class is called and as the screen is not null(PlayScreen extending Screen has been passed to it)
	}
	

}

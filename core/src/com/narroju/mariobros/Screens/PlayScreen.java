package com.narroju.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.narroju.mariobros.MarioBros;
import com.narroju.mariobros.Sences.Hud;
import com.narroju.mariobros.Sprites.Mario;
import com.narroju.mariobros.Tools.B2WorldCreator;
import com.narroju.mariobros.Tools.WorldContactListener;


public class PlayScreen implements Screen  {
    private  Mario player;
    private MarioBros game;
    private TextureAtlas atlas;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;  // reference to the MAP itself
    private OrthogonalTiledMapRenderer  renderer; // it renders the tiled map to the screen

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2br; //gives a graphical representation of fixtures in box2D world




    public PlayScreen(MarioBros game){  //1.1 game here is what was passed as an argument
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game; // this.game is the game object of the playscreen method
        gamecam = new OrthographicCamera();
        gamePort = new StretchViewport(MarioBros.V_WIDTH / MarioBros.PPM ,MarioBros.V_HEIFHT/MarioBros.PPM, gamecam);
        gamecam.setToOrtho(false,MarioBros.V_WIDTH,MarioBros.V_HEIFHT);
        hud = new Hud(game.batch);





        mapLoader = new TmxMapLoader(); //1.2used to load the map from the tool TMX
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/ MarioBros.PPM);  //1.3 renders the TiledMap on to the screen
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);


        world = new World(new Vector2(0,-10), true); //gravity and sleep for reducing the processor usage
        b2br = new Box2DDebugRenderer();

        new B2WorldCreator(world,map); //1.4 loads the objects from the MAP and create the BOX2D objects

            player = new Mario(world, this); //1.5 loads the player class object(Mario) with all the features

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas(){
        return atlas;
    } //returns the back to MarioBros game class

    @Override
    public void show() {
    }

    private void handleInput(float dt) { //2.4 handleinput method is called

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
//        System.out.println("world Centre"+player.b2body.getWorldCenter());
        //System.out.println("old Linear Velocity:"+player.b2body.getLinearVelocity());
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <=2) // need to work on this???
              player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(), true);
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >=-2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(), true);
    }

    public void update(float dt){ //2.2 this is called from the render method of playscreen class
        handleInput(dt); //2.3 handleInput method is called to handle input key events

        world.step(1/60f, 6, 2 ); //2.5 in 60f means 60 frames in 1 sec, mario renders 60 time in one sec. when there is any change

        player.update(dt);  //3 update method is called from MarioBros w.r.t dt time
        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update(); //always update the cam anytime it moves
        renderer.setView(gamecam); //its an tiled map renderer which renderer the game portion of map
    }



    @Override
    public void render(float delta) {

        game.batch.setProjectionMatrix(gamePort.getCamera().combined );
        update(delta); //2.2 update method is called from the playscreen render which is called by MarioBros render which is the Main loop method

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); //its render method from BatchTiledMapRenderer extended by OrthogonalTiledMapRenderer

        b2br.render(world, gamecam.combined); //the box2d create methods are rendered



        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch); //draw is the method of sprite and Mario extends sprite. batch is already declared inside Mario
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined );
        hud.stage.draw();

        world.setContactListener(new WorldContactListener());
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2br.dispose();
        hud.dispose();
    }
}

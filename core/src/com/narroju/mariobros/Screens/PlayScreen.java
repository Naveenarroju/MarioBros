package com.narroju.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.narroju.mariobros.MarioBros;
import com.narroju.mariobros.Sences.Hud;
import com.narroju.mariobros.Sprites.Mario;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.narroju.mariobros.Tools.B2WorldCreator;


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




    public PlayScreen(MarioBros game){
        atlas = new TextureAtlas("Mario_and_Enemies.text");
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new StretchViewport(MarioBros.V_WIDTH / MarioBros.PPM ,MarioBros.V_HEIFHT/MarioBros.PPM, gamecam);
        hud = new Hud(game.batch);
        gamecam.setToOrtho(false,MarioBros.V_WIDTH,MarioBros.V_HEIFHT);





        mapLoader = new TmxMapLoader(); //used to load the map from the tool TMX
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/ MarioBros.PPM);  //renders the TiledMap on to the screen
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);


        world = new World(new Vector2(0,-10), true); //gravity and sleep for reducing the processor usage
        b2br = new Box2DDebugRenderer();

        new B2WorldCreator(world,map);

            player = new Mario(world, this);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    private void handleInput(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <=2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(), true);
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >=-2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(), true);
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2 );

        player.update(dt);
        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update(); //always update the cam anytime it moves
        renderer.setView(gamecam); //its an tiled map renderer which renderer the game portion of map
    }



    @Override
    public void render(float delta) {

        game.batch.setProjectionMatrix(gamePort.getCamera().combined );
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); //its render method from BatchTiledMapRenderer extended by OrthogonalTiledMapRenderer

        b2br.render(world, gamecam.combined);



        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch); //draw is the method of sprite and Mario extends sprite. batch is already declared inside Mario
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined );
        hud.stage.draw();


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

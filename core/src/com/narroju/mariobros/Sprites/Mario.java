package com.narroju.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.narroju.mariobros.MarioBros;
import com.narroju.mariobros.Screens.PlayScreen;


public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING}
    public State currentState;
    public State previousState;
    public World world;  //this is the world that mario is going to live in
    public Body b2body;
    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioJump;
    private float stateTimer;
    private boolean runningRight;


    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario")); //1.5.1 get the sprite map of little_mario of the entire sprite sheet
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

            Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 17, 10, 16, 16)); //holds the 4 sprites for 0.1f sec.
        marioRun = new Animation(0.1f, frames); //1.5.4 the getKeyframes parameter takes frames(keyframes) as arguments
        frames.clear();

        for (int i = 4; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        marioJump = new Animation(0.1f, frames);


        defineMario(); //1.5.3 gets the mario
        marioStand = new TextureRegion(getTexture(), 0, 10, 17, 17); //get the sprite map of little_mario of the entire sprite sheet
        setBounds(60, 50, 17 / MarioBros.PPM, 17 / MarioBros.PPM);//how large to render the sprite on to the screen
        setRegion(marioStand); //actual texture region now associated with sprite??
    }

    public void update(float dt) { //3.1 attaches the mario to the fixture
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2); //sets the mario sprite to hold on to the fixture
        setRegion(getFrame(dt)); //3.2 this method is return the appropriate method as sprites texture region
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState(); //3.3 initially the when mario sprite falls on the land getState method is called

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                /*System.out.println("beforetime:"+stateTimer);
                System.out.println("Beforedeltatime:"+dt);*/
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer); //the value of statetime is altered w.r.t dt time
                //which is equal to time it takes for the system to render one frame
                /*System.out.println("statetime:"+stateTimer);
                System.out.println("deltatime:"+dt);
                System.out.println("CurrentSate:"+currentState);
                System.out.println("previousState:"+previousState);*/
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true); //value of getkeyframe is from keyframs in 1.5.4
                //the frame of animation for the given state time or given dt time - as per the equation with dt time,
                break;
            case FALLING:
            case STANDING: // 3.5 below if statements are not executed since it breaks it here
            default:
                region = marioStand;
                break;

        }


        if ((b2body.getLinearVelocity().x < 0 || runningRight) && !region.isFlipX()) { //need to check
            region.flip(false, false);
            runningRight = true;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }


        stateTimer = currentState == previousState ? stateTimer + dt : 0;  //the stateTimer is updated here as per the delta time
        //only if currentState is equal to previousState??

        previousState = currentState;
        return region;
    }


    public State getState() { //3.4 as the initial state is standing by default, it returns STANDING

        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;

    }

    public void defineMario() { //1.5.4 the body is defined here
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fDef.shape = shape;
        b2body.createFixture(fDef);
    }
}

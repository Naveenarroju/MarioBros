package com.narroju.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.narroju.mariobros.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener { //is something what gets called when two fixtures in box2d collide with each other
    @Override
    public void beginContact(Contact contact) { //when two fixtures begin to make a connection
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") { //this is to identify which body has heads data
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            //           if the userdata is of head ? go for fixA or go for fixB
            Fixture object = head == fixA ? fixB : fixA;
            //object can be any thing ground pipe or etc..

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit(); //the main abstracted method is overrided by the
                //object(Brick, coin etc..) and executes its own logic and is called back here
            }
        }


    }

    @Override
    public void endContact(Contact contact) { //when two fixtures disconnect from each other

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { //to change the characteristics of that collision

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {//gives the results of what happend due to that collision

    }
}

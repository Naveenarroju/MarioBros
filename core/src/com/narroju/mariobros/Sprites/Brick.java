package com.narroju.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends InteractiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this); //setting userdata to the object itself , to be identified as the fixture of the brick

    }

    @Override
    public void onHeadHit() {
        System.out.println("Brick collision");
    }
}

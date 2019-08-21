package com.narroju.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.narroju.mariobros.MarioBros;
import com.narroju.mariobros.Sences.Hud;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileset;
    private final int BLANK_COIN = 28;
    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileset = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        System.out.println("Coin collision");
        getCell().setTile(tileset.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}

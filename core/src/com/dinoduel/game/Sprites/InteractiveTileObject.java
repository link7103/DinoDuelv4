package com.dinoduel.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Tools.B2WorldCreator;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / DinoDuel.PPM, (bounds.getY() + bounds.getHeight() / 2) / DinoDuel.PPM);

        body = world.createBody(bDef);

        shape.setAsBox(bounds.getWidth() / 2 / DinoDuel.PPM, bounds.getHeight() / 2 / DinoDuel.PPM);
        fDef.shape = shape;
        fDef.filter.categoryBits = B2WorldCreator.CATEGORY_SCENERY;
        fDef.filter.maskBits = B2WorldCreator.MASK_SCENERY;
        fixture = body.createFixture(fDef);
    }

    public abstract void onHeadHit();
}

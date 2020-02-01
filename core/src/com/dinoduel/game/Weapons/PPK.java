package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class PPK extends Gun {
    public PPK(int x, int y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 16;
        ySize = 12;
        img = new TextureRegion(getTexture(), 18, 37, xSize, ySize);

        //unsure if necessary and will probably go int a method
        defineWeapon();


        setBounds(0, 0, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }

    @Override
    public void useWeapon() {

    }
}

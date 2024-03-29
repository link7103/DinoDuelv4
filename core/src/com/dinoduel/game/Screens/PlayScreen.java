package com.dinoduel.game.Screens;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Scenes.Hud;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Tools.B2WorldCreator;
import com.dinoduel.game.Tools.WorldContactListener;
import com.dinoduel.game.Weapons.AK;
import com.dinoduel.game.Weapons.Barrett;
import com.dinoduel.game.Weapons.Gun;
import com.dinoduel.game.Weapons.Mossberg;
import com.dinoduel.game.Weapons.PPK;
import com.dinoduel.game.Weapons.Weapon;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    //Main Game
    private DinoDuel game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    //Map
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Player
    private Dino player1;
    //Player Sprites
    private TextureAtlas dinoAtlas;
    //Weapon Sprites
    public TextureAtlas weaponAtlas;

    private boolean spawnWeapon;
    private float spawnX;
    private float spawnY;

    //weapon list
    public ArrayList<Gun> guns = new ArrayList<>();

    private Gun gun;

    public PlayScreen(DinoDuel game) {
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        weaponAtlas = new TextureAtlas("weapons/weapons.txt");


        this.game = game;
        //Camera that follows the players
        gameCam = new OrthographicCamera();
        //Fits the proper aspect ratio
        gamePort = new FitViewport(DinoDuel.V_WIDTH / DinoDuel.PPM, DinoDuel.V_HEIGHT / DinoDuel.PPM, gameCam);
        //Creates the hud
        hud = new Hud(game.batch);
        //Renders the map
        maploader = new TmxMapLoader();
        map = maploader.load("DinoDuel Basic Tilesets/testLevel.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / DinoDuel.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //Creates the world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map, this);
        //Player1
        player1 = new Dino(world, this);
        //Barrett test
        gun = new Mossberg(40, 32, world, this);
        guns.add(gun);

        //contact listener stuff
        world.setContactListener(new WorldContactListener());
    }//end constructor

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }//end getDinoAtlas

    public TextureAtlas getweaponAtlas() {
        return weaponAtlas;
    }//end getWeaponAtlas

    @Override
    public void show() {
    }//end show

    public void spawnWeapon(float x, float y) {
        spawnWeapon = true;
        spawnX = x;
        spawnY = y;
    }

    //dt = delta time
    public void update(float dt) { //Updates the screen every frame
        //handle user input first
        handleInput(dt);
        //takes 1 step in the physics simulation ( 60 times per second)
        world.step(1 / 60f, 6, 2);

        if (spawnWeapon) {
            Gun spawn;
            int rand = (int) (Math.random() * 4);
            Gdx.app.log("num", String.valueOf(rand));

            switch (rand) {
                default:
                    //PPK
                    spawn = new PPK(spawnX, spawnY, world, this);
                    break;
                case 1:
                    //Mossberg
                    spawn = new Mossberg(spawnX, spawnY, world, this);
                    break;
                case 2:
                    //Barrett
                    spawn = new Barrett(spawnX, spawnY, world, this);
                    break;
                case 3:
                    //AK
                    spawn = new AK(spawnX, spawnY, world, this);
                    break;
            }
            spawnWeapon = false;
            guns.add(spawn);
        }
        //updates player sprite position
        player1.update(dt);
        for (Gun gunU: guns
             ) {
            gunU.update();
        }


        //attach the gamecam to the p1s x coordinate
        gameCam.position.x = player1.b2body.getPosition().x;
        //update gamecam with correct coordinates after changes
        gameCam.update();
        //tell it to only render what the camera can see
        renderer.setView(gameCam);
    }//end update

    private void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.b2body.applyLinearImpulse(new Vector2(0, 3f), player1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player1.b2body.getLinearVelocity().x <= 2) {
            player1.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player1.b2body.getLinearVelocity().x >= -2) {
            player1.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player1.playerDucking = true;
        } else {
            player1.playerDucking = false;
        }
    }//end handleInput

    @Override
    public void render(float deltaTime) {
        //seperates update logic from render
        update(deltaTime);

        //clears the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renders the game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //renders the Dino1
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();


        //might render gun
        for (Gun drawGun: guns) {
            drawGun.draw(game.batch);
        }

        player1.draw(game.batch);
        game.batch.end();

        //sets the batch to draw what the camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }//end render

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }//end resize

    @Override
    public void pause() {

    }//end pause

    @Override
    public void resume() {

    }//end resume

    @Override
    public void hide() {

    }//end hide

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }//end dispose
}//end class

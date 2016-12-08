package com.bomb.asteroid;

import org.dyn4j.collision.narrowphase.Sat;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.*;
import org.newdawn.slick.util.ClasspathLocation;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Steam on 12/6/2016.
 */
public class AsteroidGame extends BasicGame{


    public static final int UNIT_LENGTH = 50;
    public static final Vector2 GRAVITY = new Vector2(0, 50);
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;
    private static final Sat sat = new Sat();

    private static AsteroidGame instance;

    private Set<GameObject> gameObjects;
    private Set<GameObject> spawnQueue;

    public static void main(String[] args) {

        AppGameContainer app = null;
        try{
            System.setProperty("java.library.path", "./lib/");
            System.setProperty("org.lwjgl.librarypath", new File("lib/natives/natives-windows").getAbsolutePath());

            app = new AppGameContainer(new ScalableGame(new AsteroidGame("Asteroid"), WIDTH, HEIGHT));
            //app.setDisplayMode(640, 480, false);
            //app.setDisplayMode(1920, 1080, false);

            app.setShowFPS(true);
            app.setTargetFrameRate(60);
            app.start();

        }
        catch (SlickException e){
            e.printStackTrace();
        } finally {
            if(app != null){
                app.destroy();
            }
        }
    }

    public static AsteroidGame getInstance(){
        return instance;
    }

    public AsteroidGame(String name){
        super(name);
        AsteroidGame.instance = this;
    }

    public Set<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void add(GameObject gameObject){
        spawnQueue.add(gameObject);
    }

    public void checkCollisions(){

        for(GameObject gameObject : gameObjects){
            if(gameObject.isDestroyed()){
                continue;
            }
            for (GameObject other : gameObjects){

                //don't allow this object to be destroyed by a destroyed object
                //prevents one bullet from being able to destroy multiple asteroids
                if(other.isDestroyed()){
                    continue;
                }

                //don't detect collisions for objects of the same type
                if(gameObject.getClass().equals(other.getClass())){
                    continue;
                }

                if(sat.detect(gameObject.getBody(), new Transform(), other.getBody(), new Transform())){
                    gameObject.destroy();
                    other.destroy();
                    break;
                }

            }
        }

    }

    public void initLevel(){

        spawnQueue = new HashSet<>();
        gameObjects = new HashSet<>();

        //eventually we will need to preserve the ship object from the last load
        gameObjects.add(new Ship(new Vector2(250, 250)));
        gameObjects.add(new Asteroid(Asteroid.Size.LARGE, new Vector2(100, 100)));
        gameObjects.add(new Asteroid(Asteroid.Size.LARGE, new Vector2(300, 300)));
        gameObjects.add(new Asteroid(Asteroid.Size.LARGE, new Vector2(100, 300)));
        gameObjects.add(new Asteroid(Asteroid.Size.LARGE, new Vector2(100, 300)));
        gameObjects.add(new Asteroid(Asteroid.Size.LARGE, new Vector2(100, 300)));

    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

        spawnQueue = new HashSet<>();

        gameObjects = new HashSet<>();
        Ship ship = new Ship(new Vector2(WIDTH/2, HEIGHT/2));
        gameObjects.add(ship);

        //draw a rectangle around the ship for the no-spawn zone
        Rectangle rectangle = Geometry.createRectangle(100, 100);
        rectangle.translate(new Vector2(new Vector2(rectangle.getCenter(), ship.getPosition())));
        for(Vector2 vertex : rectangle.getVertices()){
            System.out.println(vertex);
        }

        for(int i = 0; i < 6; i++){
            int x, y;
            Random random = new Random();
            Asteroid asteroid;
            Set<Asteroid> asteroids = new HashSet<>();
            do{
                x = random.nextInt(WIDTH);
                y = random.nextInt(HEIGHT);
                asteroid = new Asteroid(Asteroid.Size.LARGE, new Vector2(x, y));
            }while (sat.detect(asteroid.getBody(), new Transform(), rectangle, new Transform()));

            gameObjects.add(asteroid);
        }


    }

    public int countGameObject(Class c){
        int count = 0;
        Set<GameObject> objects = new HashSet<>(gameObjects);
        objects.addAll(spawnQueue);

        for(GameObject gameObject : objects){
            if(gameObject.getClass().equals(c)){
                count++;
            }
        }
        return count;
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        double dt = (double)delta / 1000;

        //move spawned objects to gameObjects
        gameObjects.addAll(spawnQueue);
        spawnQueue = new HashSet<>();

        checkCollisions();

        Iterator<GameObject> objectIterator = gameObjects.iterator();
        while (objectIterator.hasNext()){
            GameObject gameObject = objectIterator.next();
            gameObject.update(this, gameContainer, dt);
        }

        objectIterator = gameObjects.iterator();
        while(objectIterator.hasNext()){
            GameObject gameObject = objectIterator.next();

            //cleanup destroyed objects
            if(gameObject.isDestroyed()){
                objectIterator.remove();
                continue;
            }

        }

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

        for(GameObject gameObject : gameObjects){
            gameObject.render(gameContainer, graphics);
        }

    }
}

package com.bomb.asteroid;

import org.dyn4j.collision.narrowphase.Sat;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.*;

import java.util.Set;

/**
 * Created by Steam on 12/6/2016.
 */
public abstract class GameObject {

    private static final Sat sat = new Sat();
    protected Polygon body;
    protected Vector2 position, velocity, acceleration;
    protected boolean destroyed;

    public abstract void update(BasicGame engine, GameContainer container, double dt) throws SlickException;

    public abstract void render(GameContainer container, Graphics g) throws SlickException;

    public abstract GameObject move(Transform movement);

    public abstract GameObject rotate(Transform rotation);

    public abstract void destroy();

    public boolean isCollidingWithObject(){
        boolean colliding = false;
        Set<GameObject> gameObjects = AsteroidGame.getInstance().getGameObjects();
        for (GameObject gameObject : gameObjects){
            if(gameObject != this && colliding(gameObject)){
                colliding = true;
                break;
            }
        }
        return colliding;
    }

    public boolean colliding(GameObject gameObject){
        boolean colliding = sat.detect(body, new Transform(), gameObject.getBody(), new Transform());
        return colliding;
    }

    public Polygon getBody() {
        return body;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

}

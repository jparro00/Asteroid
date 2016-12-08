package com.bomb.asteroid;


import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Created by Steam on 12/7/2016.
 */
public class Bullet extends GameObject{

    private final static long LIFE_TIME = 500;

    private long startTime;

    public Bullet(Vector2 origin, Vector2 tradjectory){
        super();
        this.body = Geometry.createRectangle(2, 2);
        setPosition(origin);
        velocity = tradjectory;
        startTime = System.currentTimeMillis();

    }

    @Override
    public void update(BasicGame engine, GameContainer container, double dt) throws SlickException {
        super.update(engine, container, dt);

        move(velocity);

        if(System.currentTimeMillis() > startTime + LIFE_TIME){
            destroy();
        }


    }
}

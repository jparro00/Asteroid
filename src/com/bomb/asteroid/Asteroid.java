package com.bomb.asteroid;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.Set;

/**
 * Created by Steam on 12/7/2016.
 */
public class Asteroid extends GameObject{

    private Size size;

    public Asteroid(Size size){
        super();
        this.body = Geometry.createPolygonalCircle(8, size.radius);
        this.size = size;

        int degrees = (int)(Math.random() * 360 ) - 180;
        double radians = Math.toRadians(degrees);
        this.velocity = new Vector2(radians);
        this.velocity.setMagnitude(10);
    }

    public Asteroid(Size size, Vector2 position){
        this(size);
        setPosition(position);
    }

    @Override
    public void update(BasicGame engine, GameContainer container, double dt) throws SlickException {
        super.update(engine, container, dt);

        //update position
        Vector2 movement = velocity.copy();

        //multiply the distance traveled by time step
        movement.setMagnitude(velocity.getMagnitude() * dt);

        //move the object
        body.translate(movement);

    }

    @Override
    public void destroy(){
        destroyed = true;
        if(size == Size.LARGE){
            Set gameObjects = context.getGameObjects();
            Asteroid e,w;
            Vector2 position = getPosition();

            e = new Asteroid(Size.MEDIUM, position);
            e.setDirection(90);
            context.add(e);

            w = new Asteroid(Size.MEDIUM, position);
            w.setDirection(-90);
            context.add(w);

        }
        else if(size == Size.MEDIUM){
            Set gameObjects = context.getGameObjects();
            Asteroid nw, ne, se, sw;
            Vector2 position = getPosition();

            ne = new Asteroid(Size.SMALL, position);
            ne.setDirection(-45);
            context.add(ne);

            se = new Asteroid(Size.SMALL, position);
            se.setDirection(45);
            context.add(se);

            sw = new Asteroid(Size.SMALL, position);
            sw.setDirection(135);
            context.add(sw);

            nw = new Asteroid(Size.SMALL, position);
            nw.setDirection(-135);
            context.add(nw);
        }
    }

    public enum Size{
        SMALL(10),
        MEDIUM(20),
        LARGE(40);

        Size(int radius){
            this.radius = radius;
        }

        int radius;
    }
}

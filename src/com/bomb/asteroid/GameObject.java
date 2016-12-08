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
    protected Vector2 velocity, acceleration;
    protected boolean destroyed;
    protected AsteroidGame context;

    public GameObject(){
        this.context = AsteroidGame.getInstance();
    }

    public void update(BasicGame engine, GameContainer container, double dt) throws SlickException{
        double x = body.getCenter().x;
        double y = body.getCenter().y;
        double width = context.WIDTH;
        double height = context.HEIGHT;

        if(x > width){
            setX(x % width);
        }
        if(x < 0){
            setX(width - x);
        }

        if(y > height){
            setY(y % height);
        }
        if(y < 0){
            setY(height - y);
        }

    }

    public void render(GameContainer container, Graphics g) throws SlickException{
        float originX, originY, currentX, currentY, newX, newY;

        Vector2[] points = body.getVertices();
        originX = (float)points[0].x;
        originY = (float)points[0].y;

        for(int i = 0; i < points.length - 1; i++){
            g.drawLine((float) points[i].x, (float) points[i].y, (float) points[i + 1].x, (float) points[i + 1].y);
        }
        g.drawLine((float) points[points.length - 1].x, (float) points[points.length - 1].y, originX, originY);
    }

    public void destroy(){
        destroyed = true;
    }

    public boolean isCollidingWithObject(){
        boolean colliding = false;
        Set<GameObject> gameObjects = AsteroidGame.getInstance().getGameObjects();
        for (GameObject gameObject : gameObjects){

            //objects of the same type don't collide
            if(gameObject.getClass().equals(this.getClass())){
                continue;
            }

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

    public Vector2 getPosition(){
        return body.getCenter().copy();
    }

    public void setPosition(Vector2 position){
        Vector2 transaltion = new Vector2(body.getCenter(), position);
        body.translate(transaltion);
    }

    public void setDirection(double degrees){
        velocity.setDirection(Math.toRadians(degrees));
    }

    public void move(Vector2 movement){
        body.translate(movement);
    }

    public void setX(double x){
        Vector2 position = getPosition();
        position.x = x;
        setPosition(position);
    }
    public void setY(double y){
        Vector2 position = getPosition();
        position.y = y;
        setPosition(position);
    }
}

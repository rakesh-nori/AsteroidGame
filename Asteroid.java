import java.awt.*;
/**
 * The asteroid, which is the enemy force (in a sense) in the game that the ship has to destroy
 * or get killed by. Help on class given by Brandon Carroll's guide.
 * 
 * @author Rakesh Nori 
 * @version May 9th, 2017
 */
public class Asteroid
{
    double x, y, xVelocity, yVelocity, radius;
    int hitsLeft, numSplit;
    /**
     * Constructor for objects of class Asteroid.
     * @param x the column or horizontal location of the asteroid.
     * @param y the row or vertical location of the asteroid.
     * @param radius the size of the asteroid.
     * @param minVelocity minimum velocity possible of the asteroid.
     * @param maxVelocity the maxium velocity possible of the asteroid.
     * @param hitsLeft the number of shots left to destroy it.
     * @param numSplit the number of smaller asteroids the asteroid breaks up into.
     */
    public Asteroid(double x,double y,double radius,double minVelocity, 
                    double maxVelocity,int hitsLeft,int numSplit)
    {
        this.x=x;
        this.y=y;
        this.radius=radius;
        this.hitsLeft=hitsLeft; 
        this.numSplit=numSplit; 
        double vel=minVelocity + Math.random()*(maxVelocity-minVelocity);
        double dir = 2*Math.PI*Math.random();
        xVelocity=vel*Math.cos(dir);
        yVelocity=vel*Math.sin(dir); 
    }
    
    /**
     * Update's the asteroids state; it is called every frame of the game. 
     * @param scrnWidth the width of the game screen.
     * @param scrnHeight    the height of the game screen.
     */
    public void move(int scrnWidth, int scrnHeight)
    {
        x+=xVelocity;
        y+=yVelocity;
        if(x<0-radius) 
            x+=scrnWidth+2*radius;
        else if(x>scrnWidth+radius) 
            x-=scrnWidth+2*radius;
        if(y<0-radius) 
            y+=scrnHeight+2*radius;
        else if(y>scrnHeight+radius) 
            y-=scrnHeight+2*radius;
    }
    
    /**
     * draws the asteroid
     * @param g the graphics used to draw the asteroid.
     */
    public void draw(Graphics g)
    {
        g.setColor(Color.gray); 
        g.fillOval((int)(x-radius+.5),(int)(y-radius+.5),
                   (int)(2*radius),(int)(2*radius));
    }
    
    /**
     * checks the number of hits left on the asteroid
     * @return the number of hits left
     */
    public int getHitsLeft()
    {
        return hitsLeft;
    }
    
    /**
     * accesses the number of asteroids the given object should split into.
     * @return the number of asteroids to be created.
     */
    public int getNumSplit()
    {
        return numSplit;
    }
    
    /**
     * checks if it has collided with a ship.
     * @return true if the ship and the asteroid have collided; otherwise,
     * false.
     * @param ship  the ship that is being checked for collision.
     */
    public boolean shipCollision(Ship ship) 
    {
        if(Math.pow(radius+ship.getRadius(),2) > Math.pow(ship.getX()-x,2) +
            Math.pow(ship.getY()-y,2) && ship.isActive()) 
            return true;
        return false;
    }
    
    /**
     * checks if the given asteroid has collided with a shot.
     * @return true if the shot and the asteroid have collided; otherwise,
     * false.
     * @param shot  the shot that is being checked for collision.
     */
    public boolean shotCollision(Shot shot)
    {
        if(Math.pow(radius,2) > Math.pow(shot.getX()-x,2)+
            Math.pow(shot.getY()-y,2))
            return true;
        return false;
    }
    
    /**
     * creates a smaller asteroid when the asteroid is shot.
     * @param minVelocity   the minimum possible velocity of the new asteroid.
     * @param maxVelocity   the maximum possible velocity of the new asteroid.
     * @return the split asteroid.
     */
    public Asteroid createSplitAsteroid(double minVelocity, double maxVelocity)
    {
        return new Asteroid(x,y,radius/Math.sqrt(numSplit), 
                            minVelocity, maxVelocity, hitsLeft-1, numSplit);
    }
}

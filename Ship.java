import java.awt.*;
/**
 * A ship that shoots the asteroids and is controlled by the user. 
 * Help on class given by Brandon Carroll's guide. 
 * 
 * @author Rakesh Nori
 * @version 5/5/2017
 */
public class Ship
{
    private final double[] origXPts = {14, -10, -6, -10}, origYPts = {0, -8, 0, 8},
    origFlameXPts = {-6, -23, -6}, origFlameYPts = {-3, 0, 3};
    private final int radius = 6;
    
    private double x, y, angle, xVelocity, yVelocity, acceleration,
    velocityDecay, rotationalSpeed;
        
    private boolean turningLeft, turningRight, accelerating, active;
    private int [] xPts, yPts, flameXPts, flameYPts;
    
    int shotDelay, shotDelayLeft;

    /**
     * Constructor for objects of class Ship
     * @param x the column location of the ship.
     * @param y the row location of the ship.
     * @param angle the angle the ship is rotated when initialized.
     * @param acceleration the accleration constant of the ship.
     * @param velocityDecay the rate that velocity naturally decreases.
     * @param rotationalSpeed   the speed that the ship rotates.
     * @param shotDelay the rate the ship is able to shoot.
     */
    public Ship(double x, double y, double angle, double acceleration, 
                double velocityDecay, double rotationalSpeed,
                int shotDelay)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.acceleration = acceleration;
        this.velocityDecay = velocityDecay;
        this.rotationalSpeed = rotationalSpeed;
        xVelocity = 0;
        yVelocity = 0;
        turningLeft = false;
        turningRight = false;
        accelerating = false;
        active = false;
        xPts = new int[4];
        yPts = new int[4];
        flameXPts = new int[3];
        flameYPts = new int[3];
        this.shotDelay = shotDelay;
        shotDelayLeft = 0;
    }

    /**
     * draws the ship
     * @param g the graphics used to draw the ship.
     */
    public void draw(Graphics g)
    {
        if (accelerating && active)
        {
            for (int i = 0; i < 3; i++)
            {
                flameXPts [i] = (int) (origFlameXPts[i] * Math.cos(angle) - 
                              origFlameYPts[i] * Math.sin(angle) + x + .5);
                              
                flameYPts [i] = (int) (origFlameXPts[i] * Math.sin(angle) + 
                              origFlameYPts[i] * Math.cos(angle) + y + .5);
                        
            }
            g.setColor(Color.orange);
            g.fillPolygon(flameXPts, flameYPts, 3);
        }
        for (int i = 0; i < 4; i++)
        {
            xPts[i] = (int)(origXPts[i] * Math.cos(angle) - 
                          origYPts[i] * Math.sin(angle) + x + .5);
            yPts[i] = (int)(origXPts[i] * Math.sin(angle) + 
                          origYPts[i] * Math.cos(angle) + y + .5);
        }
        if (active)
            g.setColor(Color.white);
        else
            g.setColor(Color.darkGray);
        g.fillPolygon(xPts, yPts, 4);
    }
    
    /**
     * the move method that updates the ship and is called every frame of the game
     * @param scrnWidth the width of the game screen.
     * @param scrnHeight the length of the game screen.
     */
    public void move(int scrnWidth, int scrnHeight)
    {
        if (shotDelay > 0)
            shotDelay -= 1;
        if (turningLeft)
            angle -= rotationalSpeed;
        if (turningRight)
            angle += rotationalSpeed;
        if (angle > (2 * Math.PI))
            angle -= 2 * Math.PI;
        else if (angle < 0)
            angle += 2 * Math.PI;
        if (accelerating)
        {
            xVelocity += acceleration * Math.cos(angle);
            yVelocity += acceleration * Math.sin(angle);
        }
        x += xVelocity;
        y += yVelocity;
        xVelocity *= velocityDecay;
        yVelocity *= velocityDecay;
        if (x < 0)
            x += scrnWidth;
        else if (x > scrnWidth)
            x -= scrnWidth;
        if (y < 0)
            y += scrnHeight;
        else if (y > scrnHeight)
            y -= scrnHeight;
    }
    
    /**
     *changes the state of the ship's accelerating
     *@param accelerating   the new boolean for accelerating.
     */
    public void setAccelerating(boolean accelerating)
    {
        this.accelerating=accelerating; 
    }
    
    /**
     * turns the turningLeft on or off.
     * @param turningLeft   the new boolean for seeing if the ship is turning left.
     */
    public void setTurningLeft(boolean turningLeft)
    { 
        this.turningLeft=turningLeft; 
    }
    
    /**
     * turns the turningRight on or off.
     * @param turningRight   the new boolean for seeing if the ship is turning right.
     */
    public void setTurningRight(boolean turningRight)
    { 
        this.turningRight=turningRight;
    }
    
    /**
     * gets the x location of the ship.
     * @return the x location of the ship.
     */
    public double getX()
    {
        return x; 
    }
    
    /**
     * gets the y location of the ship.
     * @return the y location of the ship.
     */
    public double getY()
    { 
        return y;
    }
    
    /**
     * gets the radius of the circle that the ship occupies.
     * @return the radius of the ship.
     */
    public double getRadius()
    {
        return radius; 
    }
    
    /**
     * starts or pauses the game.
     * @param active    the new state of the game.
     */
    public void setActive(boolean active)
    {
        this.active=active; 
    }
    
    /**
     * checks if the game is pasued or not.
     * @return true if the game is not paused;
     * otherwise, return false..
     */
    public boolean isActive()
    { 
        return active;
    }
    
    /**
     * stops the ship's movement.
     */
    public void stop()
    {
        xVelocity = 0;
        yVelocity = 0;
    }
    
    /**
     * checks if the shotDelay has cooled down. 
     * @return true if the ship can shoot; otherwise,
     * return false.
     */
    public boolean canShoot()
    {
        if(shotDelayLeft > 0) 
            return false; 
        return true;
    }
    
    /**
     * creates a new shot projectile.
     * @return the new shot projectile.
     */
    public Shot shoot()
    {
        shotDelayLeft = shotDelay;
        return new Shot(x, y, angle, xVelocity, yVelocity, 40);
    }
}

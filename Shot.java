import java.awt.*;
/**
 * The projectile that comes from the ship, which is used to destroy asteroids.
 * Help on class given by Brandon Carroll's guide.
 * 
 * @author Rakesh Nori
 * @version May 9th, 2017
 */
public class Shot
{
    final double shotSpeed = 12;
    double x, y, xVelocity, yVelocity;
    int lifeLeft;

    /**
     * Constructor for objects of class Shot
     * @param x the horizontal location of the shot
     * @param y the vertical location of the shot
     * @param angle the angle that the shot is heading
     * @param shipXVel the xVelocity of the ship.
     * @param shipYVel the yVelocity of the ship.
     * @param lifeLeft the life left on the shot before it is removed.
     */
    public Shot(double x, double y, double angle, double shipXVel, double shipYVel, int lifeLeft)
    {
        this.x = x;
        this.y = y;
        xVelocity += shotSpeed * Math.cos(angle) + shipXVel;
        yVelocity += shotSpeed * Math.sin(angle) + shipYVel;
        this.lifeLeft = lifeLeft;
    }

    /**
     * updates the state of the shot. This method is called every frame of the game.
     * @param scrnWidth the width of the game screen.
     * @param scrnHeight    the height of the game screen.
     */
    public void move(int scrnWidth, int scrnHeight)
    {
        lifeLeft--;
        x += xVelocity;
        y += yVelocity;
        if (x < 0)
            x += scrnWidth; 
        else if(x > scrnWidth)
            x -= scrnWidth; 
        if( y < 0)
            y += scrnHeight; 
        else if (y > scrnHeight)
            y -= scrnHeight;
    }
    
    /**
     * draws the shot
     * @param g the graphics used to draw the shot.
     */
    public void draw(Graphics g)
    {
        g.setColor(Color.red);
        g.fillOval((int)(x-.5), (int)(y-.5), 3, 3);
    }
    
    /**
     * gets the x location of the shot.
     * @return the x location of the shot.
     */
    public double getX()
    { 
        return x;
    }
    
    /**
     * gets the y location of the shot.
     * @return the y location of the shot.
     */
    public double getY()
    { 
        return y;
    }
    
    /**
     * gets the life left on the shot.
     * @return the life left on the shot.
     */
    public int getLifeLeft()
    {
        return lifeLeft;
    }
}

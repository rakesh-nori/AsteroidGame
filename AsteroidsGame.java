import java.applet.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The main body that runs the Asteroids game. Help and parts of the code were given from a guide 
 * created by Brandon Carroll, who is a student at BYU and is studying Computer Science.
 * 
 * @author Rakesh Nori
 * @version May 3rd, 2017
 */
public class AsteroidsGame extends Applet implements Runnable, KeyListener
{
    Ship ship;
    boolean paused;
    Thread thread;
    long startTime, endTime, framePeriod;
    Dimension dim;
    Image img;
    Graphics g;
    Shot [] shots;
    int numShots;
    boolean isShooting;
    Asteroid [] asteroids;
    int numAsteroids;
    double astRadius, minAstVel, maxAstVel;
    int astNumHits, astNumSplit;
    int shotTime;
    int score;
    int comboTime;
    int level;
    int lives;
    /**
     * Overrides the Applet method and is used to start the game.
     * Also resizes the applet to 500 by 500 pixels.
     */
    public void init()
    {
        resize(500,500);
        shots = new Shot[41];
        numAsteroids= 0;
        score = 0;
        level= 0;
        ship = new Ship(250,250,0,.35,.98,.1,12);
        numShots = 0;
        isShooting = false;
        paused = false;
        shotTime = 0;
        comboTime = 0;
        lives = 3;
        astRadius=60; 
        minAstVel=.5;
        maxAstVel= 5;
        astNumHits= 3;
        astNumSplit= 2;
        addKeyListener(this);
        startTime = 0;
        endTime = 0;
        framePeriod = 25;
        dim = getSize();
        img = createImage(dim.width, dim.height);
        g = img.getGraphics();
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Sets up the next level of the game.
     */
    public void setUpNextLevel()
    {
        score += level * 50;
        level++;
        if (level % 3 == 0)
            lives++;
        ship = new Ship(250,250,0,.35,.98,.1,12);
        numShots = 0;
        isShooting = false;
        paused = false;
        asteroids=new Asteroid[level *
            (int)Math.pow(astNumSplit,astNumHits-1)+1];
        numAsteroids=level;
        for(int i=0;i<numAsteroids;i++)
            asteroids[i]=new Asteroid(Math.random()*dim.width, 
                                      Math.random()*dim.height,astRadius,minAstVel,
                                      maxAstVel,astNumHits,astNumSplit);
        
    }
    /**
     *  paints the game.
     *  @param gfx  the graphics.
     */
    public void paint(Graphics gfx)
    {
        g.setColor(Color.black);
        g.fillRect(0,0,500,500);
        for (int i = 0; i < numShots; i++)
        {
            shots[i].draw(g);
            
        }
        for (int i = 0; i < numAsteroids; i++)
            asteroids[i].draw(g);
        ship.draw(g);
        g.setColor(Color.cyan);
        g.drawString("Level" + level,20,20);
        g.drawString("Score: " + score, 420, 20);
        g.drawString("Lives: " + lives, 20, 480);
        gfx.drawImage(img, 0, 0, this);
    }
    
    /**
     * Updates the screen
     * @param gf the graphics that are updated.
     */
    public void update (Graphics gf)
    {
        paint(gf);
    }
    
    /**
     * Runs the thread. This is the method to be implemented fromt the Runnable interface.
     * Make sure to press enter at the start of each level. 
     */
    public void run()
    {
        for (;;)
        {
            startTime = System.currentTimeMillis();
            if (numAsteroids <= 0)
                setUpNextLevel();
            if (!paused)
            {
                ship.move(dim.width, dim.height);
                shotTime--;
                for (int i = 0; i < numShots; i++)
                {
                    shots[i].move(dim.width, dim.height);
                    if (shots[i].getLifeLeft() <= 0)
                    {
                        deleteShot(i);
                        i--;
                    }
                }
                updateAsteroids();
                if (! (shotTime > 0))
                {
                    if (isShooting && ship.canShoot())
                    {
                        shots[numShots] = ship.shoot();
                        numShots++;
                       
                        if (level < 7)
                            shotTime =  14 - level;
                        else
                        {
                            shotTime = 5;
                        }
                    }
                }
            }
            repaint();
            try
            {
                endTime = System.currentTimeMillis();
                if (framePeriod - (endTime - startTime) > 0)
                    Thread.sleep(framePeriod - (endTime - startTime));
            }
            catch (InterruptedException e)
            {
                //nothing
            }
        }
    }
    
    /**
     * deletes a shot in the shots array
     * @param index the index in the array to be deleted.
     */
    private void deleteShot(int index)
    {
        numShots--;
        for (int i = index; i < numShots; i++)
            shots[i] = shots[i+1];
        shots[numShots] = null;
    }
    
    /**
     * deletes an asteroid in the asteroids array
     * @param index the index in the asteroids array to be deleted.
     */
    private void deleteAsteroid(int index)
    {
        numAsteroids--;
        for(int i=index;i<numAsteroids;i++)
            asteroids[i]=asteroids[i+1]; 
        asteroids[numAsteroids]=null;
    }
    
    /**
     * adds an asteroid to the asteroids array
     * @param ast   the asteroid to be added to the array.
     */
    private void addAsteroid(Asteroid ast)
    {
        asteroids[numAsteroids]=ast;
        numAsteroids++;
    }
    
    /**
     * updates the asteroids and checks their interactions.
     */
    private void updateAsteroids()
    { 
        for(int i=0;i<numAsteroids;i++)
        {
            asteroids[i].move(dim.width,dim.height);
            if (comboTime > 0)
                comboTime--;
            if(asteroids[i].shipCollision(ship))
            {
                level--;
                lives--;
                if (lives <= 0)
                {
                    level = 0;
                    lives = 3;
                    score = 0;
                    setUpNextLevel();
                    return;
                }
                else
                {  
                    score -= level * 50;
                    numAsteroids=0;
                    if ((level+1) % 3 == 0)
                        lives--;
                    return;
                }
            }
            for(int j=0;j<numShots;j++)
            { 
                if(asteroids[i].shotCollision(shots[j]))
                {
                    deleteShot(j);
                    if(asteroids[i].getHitsLeft()>1)
                    {
                        for(int k=0;k<asteroids[i].getNumSplit();k++)
                            addAsteroid(asteroids[i].createSplitAsteroid(
                                        minAstVel,maxAstVel));
                    }
                    if (comboTime > 0)
                            score+= 10;
                    
                    score += 10;
                    comboTime = 60;
                    deleteAsteroid(i);
                    j=numShots;
                    i--;
                }
            }
        }
    }
      
    /**
     * Moves the ship when a key is pressed
     * @param e the key that the user presses.
     */
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (!ship.isActive() && !paused)
                ship.setActive(true);
            else
            {
                paused = !paused;
                if (paused)
                    ship.setActive(false);
                else
                    ship.setActive(true);
            }    
        }
        else if (paused || !ship.isActive())
            return;
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            ship.setAccelerating(true);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            ship.setTurningRight(true);
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            ship.setTurningLeft(true);
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            isShooting = true;
        
    }
    
    /**
     * Stops the circle's motion when the key is released.
     * @param e the key released by the user.
     */
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            ship.setAccelerating(false);
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            ship.setTurningLeft(false);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            ship.setTurningRight(false);
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            isShooting = false;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            ship.stop();
    }
        
    /**
     * Method needed to implement the KeyListner interface. No code should be present.
     * @param e a key event.
     */
    public void keyTyped (KeyEvent e)
    {
    }
    
}

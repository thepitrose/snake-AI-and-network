
package game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class Sologamepanel  extends JPanel implements KeyListener{

    ArrayList<dots> mysnake= new ArrayList<dots>(); // the snake

    private int width = 25;   //Pixel size
    private int height = 25; //Pixel size

    private int xfood;      // X variable of food
    private int yfood;		// Y variable of food
    private int end=0;		//Checks if the player is disqualified
    private int bodysize=0;  // sanke size
    private int gog= -1;     // Variable for the last movement performed - for continuous movement
    private int nojump=0;       //Prevents "jumping" with double clicking
    private int gameSpeed=100;  // Sanke speed
    private boolean isRun;      //  it the game is runing


    private JTextField input;



//-------------------------------------------------------------------------- Set the panel

    public  Sologamepanel()
    {
        newgame();
    }


//-------------------------------------------------------------------------- new game
    public void newgame()
    {
        end=0;		//Checks if the player is disqualified
        bodysize=0;  // sanke size
        gog= -1;     // Variable for the last movement performed - for continuous movement
        nojump=0;     //Prevents "jumping" with double clicking
        gameSpeed=100;    // Sanke speed

        if (mysnake.size()!=0)
        {
            mysnake.clear();
        }

        Random rand = new Random();          //A random starting point for food
        xfood = rand.nextInt((39 - 1) + 1);  //A random starting X point for food
        yfood = rand.nextInt((39 - 1) + 1); //A random starting Y point for food

        xfood=xfood*25;  //Change the food to grow appropriate pixels
        yfood=yfood*25; //Change the food to grow appropriate pixels

        dots  xx = new dots(500,500);  //The starting point of the snake, the center of the screen
        mysnake.add(xx); //set the starting point as the snake head

        input = new JTextField();  //Panel settings
        input.setPreferredSize(new Dimension  (0,0));  //Panel settings
        input.addKeyListener(this);  //Panel settings
        isRun=true;          //  it the game is runing
        this.add(input);  //Panel settings

    }
//-------------------------------------------------------------------------- graphics

    public void paintComponent(Graphics g)
    {

        try
        {
            Thread.sleep(gameSpeed);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        super.paintComponent(g);

        if (mysnake.get(bodysize).getX()>1000) //Check to see if it gets stuck in the left wall
        {
            end++;
        }


        if (mysnake.get(bodysize).getY()<0)  //Check to see if it gets stuck in the top wall
        {

            end++;
        }

        if (mysnake.get(bodysize).getY()>975) //Check to see if it gets stuck in the down wall
        {
            end++;
        }


        if (mysnake.get(bodysize).getX()<0) //Check to see if it gets stuck in the right wall
        {
            end++;
        }


        if (end!=0)  //Checking if it does get stuck in the wall
        {
            theend(); 					// View game end message
            super.paintComponent(g);  //Clean the screen for aesthetic reasons
        }

        else //If the player is not dead
        {
            g.fillRect(xfood, yfood, width, height); //Show the food on the screen


            for (int i =bodysize ; i>=0 ; i--)  //View all Snake
            {
                g.setColor(Color.green);
                g.fillRect(mysnake.get(i).getX(), mysnake.get(i).getY(), width, height);
            }

            if(gog==0)       //if last click was up
            {
                moveit(gog); //Continue up
            }

            if(gog==1) //if last click was down
            {
                moveit(gog);  //Continue down
            }

            else if(gog==2) //if last click was left
            {
                moveit(gog); //Continue left
            }

            else if(gog==3) //if last click was right
            {
                moveit(gog); //Continue right
            }
        }

    }


//-------------------------------------------------------------------------- End game

    public void theend () //if the game end
    {
        int s = bodysize*10;
        JOptionPane.showMessageDialog(null,"Points " + s ,"End Game",JOptionPane.INFORMATION_MESSAGE); //show message
        isRun=false;
    }


//-------------------------------------------------------------------------- Resize the snake

    public void csize (int bx, int by) //Change the size of the snake
    {
        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= mysnake.size()-1;


        while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
        {
            if (temps==bodysize) // to set a new head
            {
                tempxx= mysnake.get(temps-1).getX();  //get the X variable of the old head
                tempyy=mysnake.get(temps-1).getY();  //get the Y variable of the old head

                mysnake.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                mysnake.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
            }

            else //if not the head
            {

                tempx=tempxx; //set as temp the old X variable of the point before
                tempy=tempyy; //set as temp the old Y variable of the point before

                tempxx= mysnake.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                tempyy=mysnake.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                mysnake.get(temps-1).setX(tempx); //set the "new" X
                mysnake.get(temps-1).setY(tempy); //set the "new" Y
            }
            temps--;
        }

    }


//--------------------------------------------------------------------------  Player Input

    @Override
    public void keyPressed(KeyEvent e) {   //Pretty self explanatory

        int keyCode = e.getKeyCode();

        switch( keyCode )
        {
            case KeyEvent.VK_UP:
                if (nojump==keyCode)
                {

                }
                else if (bodysize>2)
                {
                    if (gog==1)
                    {

                    }
                    else
                    {
                        nojump=keyCode;
                        moveit(0);
                    }
                }
                else
                {
                    nojump=keyCode;
                    moveit(0);
                }
                break;


            case KeyEvent.VK_DOWN:
                if (nojump==keyCode)
                {

                }
                else if (bodysize>2)
                {
                    if (gog==0)
                    {

                    }
                    else
                    {
                        nojump=keyCode;
                        moveit(1);
                    }
                }
                else
                {
                    nojump=keyCode;
                    moveit(1);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (nojump==keyCode)
                {

                }
                else if (bodysize>2)
                {
                    if (gog==3)
                    {

                    }
                    else
                    {
                        nojump=keyCode;
                        moveit(2);
                    }
                }
                else
                {
                    nojump=keyCode;
                    moveit(2);
                }
                break;

            case KeyEvent.VK_RIGHT :
                if (nojump==keyCode)
                {

                }
                else if (bodysize>2)
                {
                    if (gog==2)
                    {

                    }
                    else
                    {
                        nojump=keyCode;
                        moveit(3);
                    }
                }
                else
                {
                    nojump=keyCode;
                    moveit(3);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        if (isRun==false)
        {
            newgame();
            moveit(4); //For displaying the board in a new game
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }



//--------------------------------------------------------------------------  Movement


    public void moveit (int x)
    {

        int temp =0;
        Random rand = new Random();

        int tempx = mysnake.get(bodysize).getX();
        int tempy = mysnake.get(bodysize).getY();

        if (x==0)  // If pressed up
        {
            temp = mysnake.get(bodysize).getY() - 25; //move the Pixel "Step" up
            mysnake.get(bodysize).setY(temp);		//move the Pixel "Step" up
            csize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==1)
        {
            temp = mysnake.get(bodysize).getY()+ 25;   //move the Pixel "Step" down
            mysnake.get(bodysize).setY(temp);	     	//move the Pixel "Step" down
            csize(tempx,tempy);				    		//Apply to the rest of the body
        }

        if (x==2)
        {
            temp = mysnake.get(bodysize).getX()- 25;  //move the Pixel "Step" left
            mysnake.get(bodysize).setX(temp);		 //move the Pixel "Step" left
            csize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==3)
        {
            temp = mysnake.get(bodysize).getX()+ 25; //move the Pixel "Step" right
            mysnake.get(bodysize).setX(temp);		 //move the Pixel "Step" right
            csize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==4)   //Creates an "event" for a new game For displaying the board in a new game
        {

        }


        int okx = Math.abs(mysnake.get(bodysize).getX()-xfood);	// To Check with the snake's head "touched" the food in X point
        int oky = Math.abs(mysnake.get(bodysize).getY()-yfood);	// To Check with the snake's head "touched" the food in Y point

        if (okx==0)			//Check with the snake's head "touched" the food in X point
        {
            if (oky==0)	//Check with the snake's head "touched" the food in Y point
            {

                dots  xx = new dots(xfood,yfood); //add the food point to the snake
                mysnake.add(xx);					 //add the food point to the snake
                bodysize++;						//Increasing the size of the snake's body

                xfood = rand.nextInt((39 - 1) + 1);	//set a new food
                yfood = rand.nextInt((39 - 1) + 1);	//set a new food

                xfood=xfood*25;						//set a new food
                yfood=yfood*25;						//set a new food

                if (gameSpeed>20)               //As the snake grows the game gets faster
                {
                gameSpeed=gameSpeed-2;
                }
            }
        }


        if (bodysize >3)							//Check if the snake has "eaten" itself
        {
            for (int i =0; i<bodysize-2; i++)
            {
                if (mysnake.get(bodysize).getX()==mysnake.get(i).getX() && mysnake.get(bodysize).getY()==mysnake.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
                {
                    end++;
                }
            }
        }

        if (end!=0) // if so end the game
        {
            theend();
        }

        else
        {
            gog=x;       //Remember the movement  that was pressed, for the continuity of this movement
            repaint();		//reprint Self-explained
        }

    }

}
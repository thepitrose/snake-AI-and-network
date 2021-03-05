package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class vslocal  extends JPanel implements KeyListener
{
    ArrayList<dots> greenSanke= new ArrayList<dots>();
    ArrayList<dots> orangeSnake= new ArrayList<dots>();

    private int width = 25;   //Pixel size
    private int height = 25; //Pixel size

    private int xfood;               // X variable of food
    private int yfood;		        // Y variable of food
    private int greenend;		    //Checks if the green player is disqualified
    private int orangeend;      	//Checks if the orange player is disqualified
    private int greenSize;          // green sanke size
    private int orangeSize;         // green orange size
    private int greengog;           // Variable for the last green movement performed - for continuous movement
    private int orangegog;          // Variable for the last orange movement performed - for continuous movement
    private int greenNoJump;        //Prevents green "jumping" with double clicking
    private int orangeNoJump;       //Prevents orange "jumping" with double clicking

    private int gameSpeed;  // Sanke speed
    private boolean isRun;      //  it the game is runing

    private JTextField input;


    public vslocal()
    {
        newgame();
    }

//============================================= new game
    public void newgame()
    {
        greenend=0;
        orangeend = 0;
        greenSize=0;
        orangeSize=0;
        greengog= -1;
        orangegog= -1;
        greenNoJump=-2;
        orangeNoJump=-2;
        gameSpeed=100;

        if (greenSanke.size()!=0)
        {
            greenSanke.clear();
        }
        if (orangeSnake.size()!=0)
        {
            orangeSnake.clear();
        }

        dots  xx = new dots(250,50);                         //The starting point of the green snake, the center of the screen
        greenSanke.add(xx);                                           //set the starting point as the snake head

        dots  rx = new dots(750,50);                        //The starting point of the orange snake, the center of the screen
        orangeSnake.add(rx);                                         //set the starting point as the snake head

        xfood=500;                                                   //Change the food to grow appropriate pixels
        yfood=250;                                                  //Change the food to grow appropriate pixels

        input = new JTextField();                                    //Panel settings
        input.setPreferredSize(new Dimension(0,0));    //Panel settings
        input.addKeyListener(this);                                //Panel settings
        isRun=true;                                                  //  it the game is runing

        this.add(input);                                            //Panel settings
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

        g.setColor(Color.black);
        g.fillRect(xfood, yfood, 25, 25); //Show the food on the screen


        if (greenSanke.get(greenSize).getX()>1000) //Check to see if the green snake gets stuck in the  wall
        {
            greenend++;
        }


        if (greenSanke.get(greenSize).getY()<0)  //Check to see if the green snake gets stuck in the  wall
        {
            greenend++;
        }

        if (greenSanke.get(greenSize).getY()>975) //Check to see if the green snake gets stuck in the  wall
        {
            greenend++;
        }


        if (greenSanke.get(greenSize).getX()<0) //Check to see if the green snake gets stuck in the  wall
        {
            greenend++;
        }

        for (int i =0; i<orangeSize; i++)       //Check if the green sanke hit the orange snake
        {
            if (greenSanke.get(greenSize).getX()==orangeSnake.get(i).getX() && greenSanke.get(greenSize).getY()==orangeSnake.get(i).getY())
            {
                greenend++;
            }
        }


        if (greenend!=0)
        {
            theend(); 					// View game end message
            super.paintComponent(g);  //Clean the screen for aesthetic reasons
        }


        if (orangeSnake.get(orangeSize).getX()>1000) //Check to see if the orange snake gets stuck in the  wall
        {
            orangeend++;
        }


        if (orangeSnake.get(orangeSize).getY()<0)  //Check to see if the orange snake gets stuck in the  wall
        {
            orangeend++;
        }

        if (orangeSnake.get(orangeSize).getY()>975) //Check to see if the orange snake gets stuck in the  wall
        {
            orangeend++;
        }


        if (orangeSnake.get(orangeSize).getX()<0) //Check to see if the orange snake gets stuck in the  wall
        {
            orangeend++;
        }

        for (int i =0; i<greenSize; i++)        //Check if the orange sanke hit the green snake
        {
            if (orangeSnake.get(orangeSize).getX()==greenSanke.get(i).getX() && orangeSnake.get(orangeSize).getY()==greenSanke.get(i).getY())
            {
                orangeend++;
            }
        }


        if (orangeend!=0)
        {
            theend(); 					// View game end message
            super.paintComponent(g);  //Clean the screen for aesthetic reasons
        }


        else //If the player is not dead
        {
            if (greenSize >= orangeSize)
            {
                int j = orangeSize;
                for (int i = greenSize; i >= 0; i--) // View all Snake
                {
                    g.setColor(Color.green);
                    g.fillRect(greenSanke.get(i).getX(), greenSanke.get(i).getY(), width, height);

                    if (j >= 0)
                    {
                        g.setColor(Color.orange);
                        g.fillRect(orangeSnake.get(j).getX(), orangeSnake.get(j).getY(), width, height);
                        j--;
                    }
                }

            }
            else
            {
                int i = greenSize;
                for (int j = orangeSize; j >= 0; j--) // View all Snake
                {
                    g.setColor(Color.orange);
                    g.fillRect(orangeSnake.get(j).getX(), orangeSnake.get(j).getY(), width, height);

                    if (i >= 0) {
                        g.setColor(Color.green);
                        g.fillRect(greenSanke.get(i).getX(), greenSanke.get(i).getY(), width, height);
                        i--;
                    }
                }
            }
        }

        moveit(orangegog);
        moveit(greengog);

    }


//-------------------------------------------------------------------------- End game

    public void theend () //if the game end
    {
        if (greenend!=0)
        {
            JOptionPane.showMessageDialog(null,"Orange Win! " ,"End Game",JOptionPane.INFORMATION_MESSAGE); //show message
        }
        else if (orangeend!=0)
        {
            JOptionPane.showMessageDialog(null,"Green Win! " ,"End Game",JOptionPane.INFORMATION_MESSAGE); //show message
        }

        newgame();
    }


//-------------------------------------------------------------------------- Resize the green snake


    public void gcsize (int bx, int by) //Change the size of the snake
    {
        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= greenSanke.size()-1;


        while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
        {
            if (temps==greenSize) // to set a new head
            {
                tempxx= greenSanke.get(temps-1).getX();  //get the X variable of the old head
                tempyy=greenSanke.get(temps-1).getY();  //get the Y variable of the old head

                greenSanke.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                greenSanke.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
            }

            else //if not the head
            {

                tempx=tempxx; //set as temp the old X variable of the point before
                tempy=tempyy; //set as temp the old Y variable of the point before

                tempxx= greenSanke.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                tempyy=greenSanke.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                greenSanke.get(temps-1).setX(tempx); //set the "new" X
                greenSanke.get(temps-1).setY(tempy); //set the "new" Y
            }
            temps--;
        }

    }


//-------------------------------------------------------------------------- Resize the orange snake

    public void ocsize (int bx, int by) //Change the size of the snake
    {
        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= orangeSnake.size()-1;


        while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
        {
            if (temps==orangeSize) // to set a new head
            {
                tempxx= orangeSnake.get(temps-1).getX();  //get the X variable of the old head
                tempyy=orangeSnake.get(temps-1).getY();  //get the Y variable of the old head

                orangeSnake.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                orangeSnake.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
            }

            else //if not the head
            {

                tempx=tempxx; //set as temp the old X variable of the point before
                tempy=tempyy; //set as temp the old Y variable of the point before

                tempxx= orangeSnake.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                tempyy=orangeSnake.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                orangeSnake.get(temps-1).setX(tempx); //set the "new" X
                orangeSnake.get(temps-1).setY(tempy); //set the "new" Y
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
                if (greenNoJump==keyCode)
                {

                }
                else if (greenSize>2)
                {
                    if (greengog==1)
                    {

                    }
                    else
                    {
                        greenNoJump=keyCode;
                        moveit(0);
                    }
                }
                else
                {
                    greenNoJump=keyCode;
                    moveit(0);
                }
                break;


            case KeyEvent.VK_DOWN:
                if (greenNoJump==keyCode)
                {

                }
                else if (greenSize>2)
                {
                    if (greengog==0)
                    {

                    }
                    else
                    {
                        greenNoJump=keyCode;
                        moveit(1);
                    }
                }
                else
                {
                    greenNoJump=keyCode;
                    moveit(1);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (greenNoJump==keyCode)
                {

                }
                else if (greenSize>2)
                {
                    if (greengog==3)
                    {

                    }
                    else
                    {
                        greenNoJump=keyCode;
                        moveit(2);
                    }
                }
                else
                {
                    greenNoJump=keyCode;
                    moveit(2);
                }
                break;

            case KeyEvent.VK_RIGHT :
                if (greenNoJump==keyCode)
                {

                }
                else if (greenSize>2)
                {
                    if (greengog==2)
                    {

                    }
                    else
                    {
                        greenNoJump=keyCode;
                        moveit(3);
                    }
                }
                else
                {
                    greenNoJump=keyCode;
                    moveit(3);
                }
                break;


            case KeyEvent.VK_W:
                if (orangeNoJump==keyCode)
                {

                }
                else if (orangeSize>2)
                {
                    if (orangegog==40)
                    {

                    }
                    else
                    {
                        orangeNoJump=keyCode;
                        moveit(38);
                    }
                }
                else
                {
                    orangeNoJump=keyCode;
                    moveit(38);
                }
                break;


            case KeyEvent.VK_S:
                if (orangeNoJump==keyCode)
                {

                }
                else if (orangeSize>2)
                {
                    if (orangegog==38)
                    {

                    }
                    else
                    {
                        orangeNoJump=keyCode;
                        moveit(40);
                    }
                }
                else
                {
                    orangeNoJump=keyCode;
                    moveit(40);
                }
                break;

            case KeyEvent.VK_A:
                if (orangeNoJump==keyCode)
                {

                }
                else if (orangeSize>2)
                {
                    if (orangegog==39)
                    {

                    }
                    else
                    {
                        orangeNoJump=keyCode;
                        moveit(37);
                    }
                }
                else
                {
                    orangeNoJump=keyCode;
                    moveit(37);
                }
                break;

            case KeyEvent.VK_D:
                if (orangeNoJump==keyCode)
                {

                }
                else if (orangeSize>2)
                {
                    if (orangegog==37)
                    {

                    }
                    else
                    {
                        orangeNoJump=keyCode;
                        moveit(39);
                    }
                }
                else
                {
                    orangeNoJump=keyCode;
                    moveit(39);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
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


        int tempx = greenSanke.get(greenSize).getX();
        int tempy = greenSanke.get(greenSize).getY();
        int tempAIx = orangeSnake.get(orangeSize).getX();
        int tempAIy = orangeSnake.get(orangeSize).getY();



        if (x==0)  // If pressed up
        {
            temp = greenSanke.get(greenSize).getY() - 25; //move the Pixel "Step" up
            greenSanke.get(greenSize).setY(temp);		//move the Pixel "Step" up
            gcsize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==1)
        {
            temp = greenSanke.get(greenSize).getY()+ 25;   //move the Pixel "Step" down
            greenSanke.get(greenSize).setY(temp);	     	//move the Pixel "Step" down
            gcsize(tempx,tempy);				    		//Apply to the rest of the body
        }

        if (x==2)
        {
            temp = greenSanke.get(greenSize).getX()- 25;  //move the Pixel "Step" left
            greenSanke.get(greenSize).setX(temp);		 //move the Pixel "Step" left
            gcsize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==3)
        {
            temp = greenSanke.get(greenSize).getX()+ 25; //move the Pixel "Step" right
            greenSanke.get(greenSize).setX(temp);		 //move the Pixel "Step" right
            gcsize(tempx,tempy);						//Apply to the rest of the body
        }

        if (x==4)   //Creates an "event" for a new game For displaying the board in a new game
        {

        }

        if(x==38)
        {
            temp = orangeSnake.get(orangeSize).getY() - 25; //move the Pixel "Step" up
            orangeSnake.get(orangeSize).setY(temp);		//move the Pixel "Step" up
            ocsize(tempAIx,tempAIy);
        }

        if (x==40)
        {
            temp = orangeSnake.get(orangeSize).getY()+ 25;   //move the Pixel "Step" down
            orangeSnake.get(orangeSize).setY(temp);	     	//move the Pixel "Step" down
            ocsize(tempAIx,tempAIy);
        }

        if (x==37)
        {
            temp = orangeSnake.get(orangeSize).getX()- 25;  //move the Pixel "Step" left
            orangeSnake.get(orangeSize).setX(temp);		 //move the Pixel "Step" left
            ocsize(tempAIx,tempAIy);
        }

        if (x==39)
        {
            temp = orangeSnake.get(orangeSize).getX()+ 25; //move the Pixel "Step" right
            orangeSnake.get(orangeSize).setX(temp);		 //move the Pixel "Step" right
            ocsize(tempAIx,tempAIy);
        }


        int okxgreen = Math.abs(greenSanke.get(greenSize).getX()-xfood);	// To Check with the snake's head "touched" the food in X point
        int okygreen = Math.abs(greenSanke.get(greenSize).getY()-yfood);	// To Check with the snake's head "touched" the food in Y point

        if (okxgreen==0)			//Check with the snake's head "touched" the food in X point
        {
            if (okygreen==0)	//Check with the snake's head "touched" the food in Y point
            {

                dots  xx = new dots(xfood,yfood); //add the food point to the snake
                greenSanke.add(xx);					 //add the food point to the snake
                greenSize++;						//Increasing the size of the snake's body

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




        int okxorange = Math.abs(orangeSnake.get(orangeSize).getX()-xfood);	// To Check with the snake's head "touched" the food in X point
        int okyorange = Math.abs(orangeSnake.get(orangeSize).getY()-yfood);	// To Check with the snake's head "touched" the food in Y point

        if (okxorange==0)			//Check with the snake's head "touched" the food in X point
        {
            if (okyorange==0)	//Check with the snake's head "touched" the food in Y point
            {

                dots  xx = new dots(xfood,yfood); //add the food point to the snake
                orangeSnake.add(xx);					 //add the food point to the snake
                orangeSize++;						//Increasing the size of the snake's body

                xfood = rand.nextInt((39 - 1) + 1);	//set a new food
                yfood = rand.nextInt((39 - 1) + 1);	//set a new food

                xfood=xfood*25;						//set a new food
                yfood=yfood*25;						//set a new food

            }
        }

        if (greenSize >3)							//Check if the snake has "eaten" itself
        {
            for (int i =0; i<greenSize-2; i++)
            {
                if (greenSanke.get(greenSize).getX()==greenSanke.get(i).getX() && greenSanke.get(greenSize).getY()==greenSanke.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
                {
                    greenend++;
                }
            }
        }

        if (greenend!=0) // if so end the game
        {
            theend();
        }

        if (orangeSize >3)							//Check if the snake has "eaten" itself
        {
            for (int i =0; i<orangeSize-2; i++)
            {
                if (orangeSnake.get(orangeSize).getX()==greenSanke.get(i).getX() && orangeSnake.get(orangeSize).getY()==greenSanke.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
                {
                    orangeend++;
                }
            }
        }

        if (orangeend!=0) // if so end the game
        {
            theend();
        }

        else
        {
            if (x==0 || x==1 || x==2 || x==3)
            {
                greengog=x;       //Remember the movement  that was pressed, for the continuity of this movement
            }

            if (x==37 || x==38 || x==39 || x==40)
            {
                orangegog=x;       //Remember the movement  that was pressed, for the continuity of this movement
            }

            repaint();		//reprint Self-explained
        }

    }
}

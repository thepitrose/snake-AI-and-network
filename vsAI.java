package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class vsAI extends JPanel implements KeyListener
{

    ArrayList<dots> mysnake= new ArrayList<dots>();
    ArrayList<dots> AIsnake= new ArrayList<dots>();

    private int width = 25;   //Pixel size
    private int height = 25; //Pixel size

    private int xfood;                // X variable of food
    private int yfood;		         // Y variable of food
    private int MYend=0;		     //Checks if the player is disqualified
    private int bodysize=0;          // sanke size
    private int AIbodysize=0;          // AI sanke size
    private int gog= -1;            // Variable for the last movement performed - for continuous movement
    private int nojump=-2;            //Prevents "jumping" with double clicking
    private int gameSpeed=100;       // Sanke speed
    private boolean isRun;              //  it the game is runing

    private JTextField input;

    public vsAI()
    {
        newgame();
    }

    public void newgame()
    {
        MYend=0;		//Checks if the player is disqualified
        bodysize=0;  // sanke size
        AIbodysize=0;
        gog= -1;     // Variable for the last movement performed - for continuous movement
        nojump=-2;     //Prevents "jumping" with double clicking
        gameSpeed=100;    // Sanke speed

        if (mysnake.size()!=0)
        {
            mysnake.clear();
        }
        if (AIsnake.size()!=0)
        {
            AIsnake.clear();
        }

        dots  xx = new dots(250,50);  //The starting point of the snake, the center of the screen
        mysnake.add(xx);                         //set the starting point as the snake head

        dots  rx = new dots(750,50);  //The starting point of the snake, the center of the screen
        AIsnake.add(rx);                         //set the starting point as the snake head

        xfood=500;  //Change the food to grow appropriate pixels
        yfood=250; //Change the food to grow appropriate pixels

        input = new JTextField();  //Panel settings
        input.setPreferredSize(new Dimension(0,0));  //Panel settings
        input.addKeyListener(this);  //Panel settings
        isRun=true;          //  it the game is runing

        this.add(input);  //Panel settings
    }


//-------------------------------------------------------------------------- graphics

    public void paintComponent(Graphics g)
    {

        try
        {
            Thread.sleep(gameSpeed);  // Wait half a second, for movement
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        super.paintComponent(g);

        if(gog!=-1)
        {
          runAI();
        }

        g.setColor(Color.black);
        g.fillRect(xfood, yfood, 25, 25); //Show the food on the screen


        if (mysnake.get(bodysize).getX()>1000) //Check to see if it gets stuck in the left wall
        {
            MYend++;
        }


        if (mysnake.get(bodysize).getY()<0)  //Check to see if it gets stuck in the top wall
        {
            MYend++;
        }

        if (mysnake.get(bodysize).getY()>975) //Check to see if it gets stuck in the down wall
        {
            MYend++;
        }


        if (mysnake.get(bodysize).getX()<0) //Check to see if it gets stuck in the right wall
        {
            MYend++;
        }

        for (int i =0; i<AIbodysize-1; i++)
        {
            if (mysnake.get(bodysize).getX()==AIsnake.get(i).getX() && mysnake.get(bodysize).getY()==AIsnake.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
            {
                MYend++;
            }
        }



        if (MYend!=0)  //Checking if it does get stuck in the wall
        {
            theend(); 					// View game end message
            super.paintComponent(g);  //Clean the screen for aesthetic reasons
        }

        else //If the player is not dead
        {
            if (bodysize >= AIbodysize)
            {
                int j = AIbodysize;
                for (int i = bodysize; i >= 0; i--) // View all Snake
                {
                    g.setColor(Color.green);
                    g.fillRect(mysnake.get(i).getX(), mysnake.get(i).getY(), width, height);

                    if (j >= 0)
                    {
                        g.setColor(Color.orange);
                        g.fillRect(AIsnake.get(j).getX(), AIsnake.get(j).getY(), width, height);
                        j--;
                    }
                }
            }
            else
            {
                int i = bodysize;
                for (int j = AIbodysize; j >= 0; j--) // View all Snake
                {
                    g.setColor(Color.orange);
                    g.fillRect(AIsnake.get(j).getX(), AIsnake.get(j).getY(), width, height);

                    if (i >= 0)
                    {
                        g.setColor(Color.green);
                        g.fillRect(mysnake.get(i).getX(), mysnake.get(i).getY(), width, height);
                        i--;
                    }
                }
            }
        }

          moveit(gog);
    }


//-------------------------------------------------------------------------- End game

    public void theend () //if the game end
    {
        isRun=false;
    }


//-------------------------------------------------------------------------- Resize the player snake

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



//------------------------------------------------------------------------- Resize the AI snake
    public void AIcsize (int bx, int by) //Change the size of the snake
    {
        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= AIsnake.size()-1;


        while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
        {
            if (temps==AIbodysize) // to set a new head
            {
                tempxx= AIsnake.get(temps-1).getX();  //get the X variable of the old head
                tempyy=AIsnake.get(temps-1).getY();  //get the Y variable of the old head

                AIsnake.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                AIsnake.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
            }

            else //if not the head
            {

                tempx=tempxx; //set as temp the old X variable of the point before
                tempy=tempyy; //set as temp the old Y variable of the point before

                tempxx= AIsnake.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                tempyy=AIsnake.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                AIsnake.get(temps-1).setX(tempx); //set the "new" X
                AIsnake.get(temps-1).setY(tempy); //set the "new" Y
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
        int tempAIx = AIsnake.get(AIbodysize).getX();
        int tempAIy = AIsnake.get(AIbodysize).getY();



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

        if(x==38)
        {
            temp = AIsnake.get(AIbodysize).getY() - 25; //move the Pixel "Step" up
            AIsnake.get(AIbodysize).setY(temp);		//move the Pixel "Step" up
            AIcsize(tempAIx,tempAIy);
        }

        if (x==40)
        {
            temp = AIsnake.get(AIbodysize).getY()+ 25;   //move the Pixel "Step" down
            AIsnake.get(AIbodysize).setY(temp);	     	//move the Pixel "Step" down
            AIcsize(tempAIx,tempAIy);
        }

        if (x==37)
        {
            temp = AIsnake.get(AIbodysize).getX()- 25;  //move the Pixel "Step" left
            AIsnake.get(AIbodysize).setX(temp);		 //move the Pixel "Step" left
            AIcsize(tempAIx,tempAIy);
        }

        if (x==39)
        {
            temp = AIsnake.get(AIbodysize).getX()+ 25; //move the Pixel "Step" right
            AIsnake.get(AIbodysize).setX(temp);		 //move the Pixel "Step" right
            AIcsize(tempAIx,tempAIy);
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


        int okxAI = Math.abs(AIsnake.get(AIbodysize).getX()-xfood);	// To Check with the snake's head "touched" the food in X point
        int okyAI = Math.abs(AIsnake.get(AIbodysize).getY()-yfood);	// To Check with the snake's head "touched" the food in Y point

        if (okxAI==0)			//Check with the snake's head "touched" the food in X point
        {
            if (okyAI==0)	//Check with the snake's head "touched" the food in Y point
            {

                dots  xx = new dots(xfood,yfood); //add the food point to the snake
                AIsnake.add(xx);					 //add the food point to the snake
                AIbodysize++;						//Increasing the size of the snake's body

                xfood = rand.nextInt((39 - 1) + 1);	//set a new food
                yfood = rand.nextInt((39 - 1) + 1);	//set a new food

                xfood=xfood*25;						//set a new food
                yfood=yfood*25;						//set a new food

            }
        }

        if (bodysize >3)							//Check if the snake has "eaten" itself
        {
            for (int i =0; i<bodysize-2; i++)
            {
                if (mysnake.get(bodysize).getX()==mysnake.get(i).getX() && mysnake.get(bodysize).getY()==mysnake.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
                {
                    MYend++;
                }
            }
        }

        if (MYend!=0) // if so end the game
        {
            theend();
        }

        else
        {
            if (x==0 || x==1 || x==2 || x==3)
            {
            gog=x;       //Remember the movement  that was pressed, for the continuity of this movement
            }

            repaint();		//reprint Self-explained
        }


    }

//================================================ the AI
    public void runAI()
    {
        int tempxl = Math.abs(AIsnake.get(AIbodysize).getX()-xfood-25);
        int tempxr = Math.abs(AIsnake.get(AIbodysize).getX()-xfood+25);
        int tempyu = Math.abs(AIsnake.get(AIbodysize).getY()-yfood-25);
        int tempyd = Math.abs(AIsnake.get(AIbodysize).getY()-yfood+25);

        int ygo;
        int xgo;

        int y;
        int x;
        int nottox=0;
        int nottoy=0;

        if (tempyu<=tempyd)
        {
            ygo=tempyu;
            if (AIbodysize>3)							//Prevent the snake from hit itself
            {
                for (int i =0; i<AIbodysize-2; i++)
                {
                    if (AIsnake.get(AIbodysize).getX()==AIsnake.get(i).getX() && (AIsnake.get(AIbodysize).getY()-25)==AIsnake.get(i).getY())
                    {
                        nottoy++;
                    }
                }
            }

            for (int i =0; i<bodysize-1; i++)
            {
                if (AIsnake.get(AIbodysize).getX()==mysnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==mysnake.get(i).getY())
                {
                    nottoy++;
                }
            }
            y=38;
        }
        else
       {
           ygo= tempyd;
           for (int i =0; i<AIbodysize-2; i++)
           {
               if (AIsnake.get(AIbodysize).getX()==AIsnake.get(i).getX() && (AIsnake.get(AIbodysize).getY()+25)==AIsnake.get(i).getY())
               {
                   nottoy++;
               }
           }
           for (int i =0; i<bodysize-1; i++)
           {
               if (AIsnake.get(AIbodysize).getX()==mysnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==mysnake.get(i).getY())
               {
                   nottoy++;
               }
           }
           y=40;
       }

        if (tempxl<=tempxr)
        {
             xgo=tempxl;
            for (int i =0; i<AIbodysize-2; i++)
            {
                if ((AIsnake.get(AIbodysize).getX()-25)==AIsnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==AIsnake.get(i).getY())
                {
                    nottox++;
                }
            }
            for (int i =0; i<bodysize-1; i++)
            {
                if (AIsnake.get(AIbodysize).getX()==mysnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==mysnake.get(i).getY())
                {
                    nottox++;
                }
            }
             x=37;
        }
        else
         {
             xgo= tempxr;
             for (int i =0; i<AIbodysize-2; i++)
             {
                 if ((AIsnake.get(AIbodysize).getX()+25)==AIsnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==AIsnake.get(i).getY())
                 {
                     nottox++;
                 }
             }
             for (int i =0; i<bodysize-1; i++)
             {
                 if (AIsnake.get(AIbodysize).getX()==mysnake.get(i).getX() && AIsnake.get(AIbodysize).getY()==mysnake.get(i).getY())
                 {
                     nottox++;
                 }
             }
             x=39;
         }

        if (ygo<xgo && yfood!=AIsnake.get(AIbodysize).getY() && nottoy==0)
        {
            moveit(y);
        }

         else if (ygo<xgo && yfood==AIsnake.get(AIbodysize).getY() && nottox==0)
         {
             moveit(x);
         }

         else if (tempyu==tempyd && nottox==0)
         {
             moveit(x);
         }

        else if (ygo>xgo && xfood!=AIsnake.get(AIbodysize).getX() && nottox==0)
        {
            moveit(x);
        }

         else if (tempxl==tempxr && xfood==AIsnake.get(AIbodysize).getX() && nottoy==0)
         {
             moveit(y);
         }

         else
         {
             if (nottoy==0 && nottox==0)
             {
             moveit(y);
             }
             else
             {
                 moveit(x);
             }
         }

     }

}

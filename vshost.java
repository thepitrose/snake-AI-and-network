package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class vshost extends JPanel implements KeyListener , Runnable
    {

    ArrayList<dots> GreenSnake= new ArrayList<dots>();
    ArrayList<dots> OrangeSnake= new ArrayList<dots>();

    private int width = 25;
    private int height = 25;
    private int oldxfood;
    private int oldyfood;


    private int xfood;
    private int yfood;

    private int myEnd=0;
    private int isEnd=0;
    private int greenSize=0;
    private int orangeSize=0;
    private int greengog= -1;
    private int orangegog= -1;
    private boolean isRun;
    private int nojump=0;
    private int rivalnojump=-1;


    private JTextField input;

    private String ip;
    private int port;
    private Scanner scanner = new Scanner(System.in);
    private Thread thread;

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private ServerSocket serverSocket;

    private boolean rival = false;
    private boolean accepted = false;
    private boolean gogogo = false;



//-------------------------------------------------------------------------- Set the panel

    public  vshost()
    {
        try
        {
            ip = InetAddress.getLocalHost().getHostAddress();
            JOptionPane.showMessageDialog(null,"Your IP is " + ip ,"IP",JOptionPane.INFORMATION_MESSAGE); //show message
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        port=0;

        while (port < 1 || port > 65535)
        {
            String sport = JOptionPane.showInputDialog("set your port , Between 1 and 65535");
             port = Integer.parseInt(sport);
        }

        if (!connect())
        {
            initializeServer();
        }

        newgame();
        thread = new Thread(this, "sanke");
        thread.start();

    }

//--------------------------------------------------------------------------  run

    public void run()
    {
        while (true)
            {
                 if (!rival && !accepted)
                  {
                   listenForServerRequest();
                  }
                else
                {
                  getRivalData();
                }
            }
    }

//--------------------------------------------------------------------------  get data form the other player

    private void getRivalData()
    {

            try
            {
                int getData = dis.readInt();

                if (getData>=0 && getData<=90)
                {
                    if(getData!=rivalnojump ||rivalnojump==-1)
                    {
                     moveit(getData);
                     rivalnojump=getData;
                     }
                }

                else if (getData>=100 && getData<=199)
                {
                    oldxfood=xfood;
                    xfood=getData-100;
                    xfood=xfood*25;
                }

                else if (getData>=200 && getData<=299)
                {
                    oldyfood=yfood;
                    yfood=getData-200;
                    yfood=yfood*25;
                    dots  xx = new dots(oldxfood,oldyfood);
                    OrangeSnake.add(xx);
                    orangeSize++;
                }

                else if (getData==-1)
                {
                    isRun=false;
                    isEnd=5;
                }


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

    }

//-------------------------------------------------------------------------- graphics

    public void paintComponent(Graphics g)
    {

        try
        {
            Thread.sleep(100);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        super.paintComponent(g);

        if (!accepted)
        {
            g.setFont(g.getFont().deriveFont(110f));
            g.drawString("Waiting for player",50,450);
        }

        else if (gogogo)
        {
            try
            {
                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            gogogo=false;
            input.addKeyListener(this);
        }


        g.setColor(Color.black);
        g.fillRect(xfood, yfood, 25, 25);


        if (GreenSnake.get(greenSize).getX()>1000)
        {
            myEnd++;
        }


        if (GreenSnake.get(greenSize).getY()<0)
        {
            myEnd++;
        }

        if (GreenSnake.get(greenSize).getY()>975)
        {
            myEnd++;
        }


        if (GreenSnake.get(greenSize).getX()<0)
        {
            myEnd++;
        }

        if (isEnd!=0)
        {
            super.paintComponent(g);
            g.setFont(g.getFont().deriveFont(110f));
            g.drawString("You Win!!!",50,450);
            this.remove(input);
        }

        else if (myEnd!=0)
        {
            theend();
            super.paintComponent(g);
            g.setFont(g.getFont().deriveFont(110f));
            g.drawString("You lose",50,450);
            this.remove(input);
        }

        else
         {
             if (greenSize >= orangeSize)
             {
                  int j = orangeSize;
                    for (int i = greenSize; i >= 0; i--)
                      {
                           g.setColor(Color.green);
                           g.fillRect(GreenSnake.get(i).getX(), GreenSnake.get(i).getY(), width, height);

                      if (j >= 0)
                      {
                        g.setColor(Color.orange);
                        g.fillRect(OrangeSnake.get(j).getX(), OrangeSnake.get(j).getY(), width, height);
                        j--;
                      }
                     }

            }
             else
                 {
                    int i = greenSize;
                    for (int j = orangeSize; j >= 0; j--)
                    {
                      g.setColor(Color.orange);
                      g.fillRect(OrangeSnake.get(j).getX(), OrangeSnake.get(j).getY(), width, height);

                      if (i >= 0) {
                        g.setColor(Color.green);
                        g.fillRect(GreenSnake.get(i).getX(), GreenSnake.get(i).getY(), width, height);
                        i--;
                      }
                    }
                 }

      if (orangegog == 37)
      {
        moveit(orangegog);
      }

      if (orangegog == 38)
      {
        moveit(orangegog);
      }

      if (orangegog == 39)
      {
        moveit(orangegog);
      }

      if (orangegog == 40)
      {
        moveit(orangegog);
      }

      if (greengog == 0)
      {
        moveit(greengog);
      }

      if (greengog == 1)
      {
        moveit(greengog);
      }

      if (greengog == 2)
      {
        moveit(greengog);
      }

      if (greengog == 3)
      {
        moveit(greengog);
      }
    }
 }



//-------------------------------------------------------------------------- End game

        public void theend () //if the game end
        {
            try
            {
                dos.writeInt(-1);
                dos.flush();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }


//-------------------------------------------------------------------------- Resize the green snake

        public void gcsize (int bx, int by) //Change the size of the snake
        {

        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= GreenSnake.size()-1;


        while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
        {
            if (temps==greenSize) // to set a new head
            {
                tempxx= GreenSnake.get(temps-1).getX();  //get the X variable of the old head
                tempyy=GreenSnake.get(temps-1).getY();  //get the Y variable of the old head

                GreenSnake.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                GreenSnake.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
            }

            else //if not the head
            {

                tempx=tempxx; //set as temp the old X variable of the point before
                tempy=tempyy; //set as temp the old Y variable of the point before

                tempxx= GreenSnake.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                tempyy=GreenSnake.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                GreenSnake.get(temps-1).setX(tempx); //set the "new" X
                GreenSnake.get(temps-1).setY(tempy); //set the "new" Y
            }
            temps--;
        }

    }

//-------------------------------------------------------------------------- Resize the Orange snake
        public void ocsize (int bx, int by) //Change the size of the snake
        {
            int tempx;
            int tempy;
            int tempxx=0;
            int tempyy=0;
            int temps= OrangeSnake.size()-1;


            while (temps>0) //set a "new head", By adding the "food point" as the new head, and update ever "snake point" to be the  "snake point" before her
            {
                if (temps==orangeSize) // to set a new head
                {
                    tempxx= OrangeSnake.get(temps-1).getX();  //get the X variable of the old head
                    tempyy=OrangeSnake.get(temps-1).getY();  //get the Y variable of the old head

                    OrangeSnake.get(temps-1).setX(bx);  //set the food X variable as the new X head variable
                    OrangeSnake.get(temps-1).setY(by);  //set the food Y variable as the new Y head variable
                }

                else //if not the head
                {

                    tempx=tempxx; //set as temp the old X variable of the point before
                    tempy=tempyy; //set as temp the old Y variable of the point before

                    tempxx= OrangeSnake.get(temps-1).getX();  //get as temp the  X variable of this point for the point after
                    tempyy=OrangeSnake.get(temps-1).getY();  //get as temp the  Y variable of this point for the point after

                    OrangeSnake.get(temps-1).setX(tempx); //set the "new" X
                    OrangeSnake.get(temps-1).setY(tempy); //set the "new" Y
                }
                temps--;
            }

        }

//--------------------------------------------------------------------------  Player Input

    @Override
    public void keyPressed(KeyEvent e)
    {

        while (!accepted)
        {
            repaint();
        }


        int keyCode = e.getKeyCode();

        try
        {
            dos.writeInt(keyCode);
            dos.flush();

            switch( keyCode )
            {
                case KeyEvent.VK_UP:
                    if (nojump==keyCode)
                    {

                    }
                    else if (greenSize>2)
                    {
                        if (greengog==1)
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
                    else if (greenSize>2)
                    {
                        if (greengog==0)
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
                    else if (greenSize>2)
                    {
                        if (greengog==3)
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
                    else if (greenSize>2)
                    {
                        if (greengog==2)
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
        catch (IOException ex)
        {
            ex.printStackTrace();
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
        int tempo =0;

        Random rand = new Random();

        int tempgx = GreenSnake.get(greenSize).getX();
        int tempgy = GreenSnake.get(greenSize).getY();
        int tempox = OrangeSnake.get(orangeSize).getX();
        int tempoy = OrangeSnake.get(orangeSize).getY();


        if(x==38)
        {
            temp = OrangeSnake.get(orangeSize).getY() - 25; //move the Pixel "Step" up
            OrangeSnake.get(orangeSize).setY(temp);		//move the Pixel "Step" up
            ocsize(tempox,tempoy);
        }

        if (x==40)
        {
            temp = OrangeSnake.get(orangeSize).getY()+ 25;   //move the Pixel "Step" down
            OrangeSnake.get(orangeSize).setY(temp);	     	//move the Pixel "Step" down
            ocsize(tempox,tempoy);
        }

        if (x==37)
        {
            temp = OrangeSnake.get(orangeSize).getX()- 25;  //move the Pixel "Step" left
            OrangeSnake.get(orangeSize).setX(temp);		 //move the Pixel "Step" left
            ocsize(tempox,tempoy);
        }

        if (x==39)
        {
            temp = OrangeSnake.get(orangeSize).getX()+ 25; //move the Pixel "Step" right
            OrangeSnake.get(orangeSize).setX(temp);		 //move the Pixel "Step" right
            ocsize(tempox,tempoy);
        }

        if (x==0)  // If pressed up
        {
            tempo = GreenSnake.get(greenSize).getY() - 25; //move the Pixel "Step" up
            GreenSnake.get(greenSize).setY(tempo);		//move the Pixel "Step" up
            gcsize(tempgx,tempgy);
        }

        if (x==1)
        {
            tempo = GreenSnake.get(greenSize).getY()+ 25;   //move the Pixel "Step" down
            GreenSnake.get(greenSize).setY(tempo);	     	//move the Pixel "Step" down
            gcsize(tempgx,tempgy);
        }

        if (x==2)
        {
            tempo = GreenSnake.get(greenSize).getX()- 25;  //move the Pixel "Step" left
            GreenSnake.get(greenSize).setX(tempo);		 //move the Pixel "Step" left
            gcsize(tempgx,tempgy);
        }

        if (x==3)
        {
            tempo = GreenSnake.get(greenSize).getX()+ 25; //move the Pixel "Step" right
            GreenSnake.get(greenSize).setX(tempo);		 //move the Pixel "Step" right
            gcsize(tempgx,tempgy);
        }

        if (x==4)   //Creates an "event" for a new game For displaying the board in a new game
        {

        }

        int okx = Math.abs(GreenSnake.get(greenSize).getX()-xfood);	// To Check with the snake's head "touched" the food in X point
        int oky = Math.abs(GreenSnake.get(greenSize).getY()-yfood);	// To Check with the snake's head "touched" the food in Y point


        if (okx==0)			//Check with the snake's head "touched" the food in X point
        {
            if (oky==0)	//Check with the snake's head "touched" the food in Y point
            {

                dots  xx = new dots(xfood,yfood); //add the food point to the snake
                GreenSnake.add(xx);					 //add the food point to the snake
                greenSize++;						//Increasing the size of the snake's body


                xfood = rand.nextInt((39 - 1) + 1);	//set a new food
                yfood = rand.nextInt((39 - 1) + 1);	//set a new food

                try
                {
                    dos.writeInt(xfood+100);
                    dos.flush();
                    dos.writeInt(yfood+200);
                    dos.flush();


                }
                catch (IOException ex)
                {
                    ex.printStackTrace();

                }

                xfood=xfood*25;						//set a new food
                yfood=yfood*25;						//set a new food

            }
        }

        if (greenSize >3)							//Check if the snake has "eaten" itself
        {
            for (int i =0; i<greenSize-2; i++)
            {
                if (GreenSnake.get(greenSize).getX()==GreenSnake.get(i).getX() && GreenSnake.get(greenSize).getY()==GreenSnake.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
                {
                    myEnd++;
                }
            }
        }

        for (int i=0; i<=OrangeSnake.size()-1;i++)
        {
            if (GreenSnake.get(greenSize).getX()==OrangeSnake.get(i).getX() && GreenSnake.get(greenSize).getY()==OrangeSnake.get(i).getY()) //Check for each point on the snake's body if another point on the snake's body touches her
            {
                myEnd++;
                break;
            }
        }


        if (myEnd!=0) // if so end the game
        {
             theend();
        }

        else
        {
            if(x==37||x==38||x==39||x==40)
            {
                orangegog=x;
            }
            if(x==0||x==1||x==2||x==3)
            {
            greengog=x;       //Remember the movement  that was pressed, for the continuity of this movement
            }

        }

        repaint();		//reprint Self-explained
    }
//======================================================= new game
    public void newgame()
    {
        myEnd=0;
        isEnd=0;
        greenSize=0;
        orangeSize=0;
        greengog= -1;
        greengog= -1;
        nojump=0;     //Prevents "jumping" with double clicking
        rivalnojump=-1;


        dots  xx = new dots(250,50);  //The starting point of the snake, the center of the screen
        GreenSnake.add(xx); //set the starting point as the snake head

        dots  rx = new dots(750,50);  //The starting point of the snake, the center of the screen
        OrangeSnake.add(rx); //set the starting point as the snake head

        xfood=500;  //Change the food to grow appropriate pixels
        yfood=250; //Change the food to grow appropriate pixels

        input = new JTextField();  //Panel settings
        input.setPreferredSize(new Dimension  (0,0));  //Panel settings
        isRun=true;

        this.add(input);  //Panel settings

    }

//======================================== set network

    private void listenForServerRequest()
    {
        Socket socket = null;
        try
        {
            socket = serverSocket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
            gogogo = true;
            repaint();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean connect()
    {
        try
        {
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    private void initializeServer()
    {
        try
        {
            serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        rival = false;
    }

}

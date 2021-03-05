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
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class vsjoin extends JPanel implements KeyListener , Runnable
{

    ArrayList<dots> GreenSnake= new ArrayList<dots>();
    ArrayList<dots> OrangeSnake= new ArrayList<dots>();

    private int width = 25;
    private int height = 25;


    private int xfood;
    private int yfood;
    private int oldxfood;
    private int oldyfood;

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

    private boolean gogogo = false;
    private boolean stopit = false;


//-------------------------------------------------------------------------- Set the panel

    public  vsjoin()
    {

        ip = JOptionPane.showInputDialog("enter the ip");

        String sport = JOptionPane.showInputDialog("enter the port");
        port = Integer.parseInt(sport);

        if (!connect())
        {
            initializeServer();
        }

        newgame();
        thread = new Thread(this, "sanke");
        thread.start();

    }

//-------------------------------------------------------------------------- run
    public void run()
    {
        while (true)
        {
            getRivalData();
        }
    }

//------------------------------------------------------------------------------  get data form the other player

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
                GreenSnake.add(xx);
                greenSize++;
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

        if (dis==null)
        {
            g.setFont(g.getFont().deriveFont(110f));
            g.drawString("could not connect ",50,450);
        }

        g.setColor(Color.black);
        g.fillRect(xfood, yfood, 25, 25);


        if(stopit)
        {
            super.paintComponent(g);
        }



        if (gogogo)
        {

            try
            {
                TimeUnit.SECONDS.sleep(3);
                repaint();
                g.setFont(g.getFont().deriveFont(110f));
                g.drawString("Waiting for player",50,450);
                repaint();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            gogogo=false;
        }


        if (OrangeSnake.get(orangeSize).getX()>1000)
        {
            myEnd++;
        }


        if (OrangeSnake.get(orangeSize).getY()<0)
        {

            myEnd++;
        }

        if (OrangeSnake.get(orangeSize).getY()>975)
        {
            myEnd++;
        }


        if (OrangeSnake.get(orangeSize).getX()<0)
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
            {
                int i = greenSize;
                for (int j = orangeSize; j >= 0; j--)
                {
                    g.setColor(Color.orange);
                    g.fillRect(OrangeSnake.get(j).getX(), OrangeSnake.get(j).getY(), width, height);

                    if (i >= 0)
                    {
                        g.setColor(Color.green);
                        g.fillRect(GreenSnake.get(i).getX(), GreenSnake.get(i).getY(), width, height);
                        i--;
                    }
                }
            }

            if(greengog==0)
            {
                moveit(greengog);
            }

            if(greengog==1)
            {
                moveit(greengog);
            }

            if(greengog==2)
            {
                moveit(greengog);
            }

            if(greengog==3)
            {
                moveit(greengog);
            }

            if(orangegog==38)
            {
                moveit(orangegog);
            }

            if(orangegog==40)
            {
                moveit(orangegog);
            }

            if(orangegog==37)
            {
                moveit(orangegog);
            }

            if(orangegog==39)
            {
                moveit(orangegog);
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


    public void gcsize (int bx, int by)
    {

        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= GreenSnake.size()-1;


        while (temps>0)
        {
            if (temps==greenSize)
            {
                tempxx= GreenSnake.get(temps-1).getX();
                tempyy=GreenSnake.get(temps-1).getY();

                GreenSnake.get(temps-1).setX(bx);
                GreenSnake.get(temps-1).setY(by);
            }

            else
            {

                tempx=tempxx;
                tempy=tempyy;

                tempxx= GreenSnake.get(temps-1).getX();
                tempyy=GreenSnake.get(temps-1).getY();

                GreenSnake.get(temps-1).setX(tempx);
                GreenSnake.get(temps-1).setY(tempy);
            }
            temps--;
        }

    }

//-------------------------------------------------------------------------- Resize the orange snake

    public void ocsize (int bx, int by)
    {
        int tempx;
        int tempy;
        int tempxx=0;
        int tempyy=0;
        int temps= OrangeSnake.size()-1;


        while (temps>0)
        {
            if (temps==orangeSize)
            {
                tempxx= OrangeSnake.get(temps-1).getX();
                tempyy=OrangeSnake.get(temps-1).getY();

                OrangeSnake.get(temps-1).setX(bx);
                OrangeSnake.get(temps-1).setY(by);
            }

            else
            {

                tempx=tempxx;
                tempy=tempyy;

                tempxx= OrangeSnake.get(temps-1).getX();
                tempyy=OrangeSnake.get(temps-1).getY();

                OrangeSnake.get(temps-1).setX(tempx);
                OrangeSnake.get(temps-1).setY(tempy);
            }
            temps--;
        }

    }

//--------------------------------------------------------------------------  Player Input

    @Override
    public void keyPressed(KeyEvent e)
    {

        int keyCode = e.getKeyCode();

        try {
            dos.writeInt(keyCode);
            dos.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        switch( keyCode )
        {
            case KeyEvent.VK_UP:
                if (nojump==keyCode)
                {

                }
                else if (orangeSize>2)
                {
                    if (orangegog==1)
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
                else if (orangeSize>2)
                {
                    if (orangegog==0)
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
                else if (orangeSize>2)
                {
                    if (orangegog==3)
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
                else if (orangeSize>2)
                {
                    if (orangegog==2)
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
        int tempo =0;

        Random rand = new Random();


        int tempgx = GreenSnake.get(greenSize).getX();
        int tempgy = GreenSnake.get(greenSize).getY();
        int tempox = OrangeSnake.get(orangeSize).getX();
        int tempoy = OrangeSnake.get(orangeSize).getY();

        if(x==0)
        {
            temp = OrangeSnake.get(orangeSize).getY() - 25;
            OrangeSnake.get(orangeSize).setY(temp);
            ocsize(tempox,tempoy);
        }

        if (x==1)
        {
            temp = OrangeSnake.get(orangeSize).getY()+ 25;
            OrangeSnake.get(orangeSize).setY(temp);
            ocsize(tempox,tempoy);
        }

        if (x==2)
        {
            temp = OrangeSnake.get(orangeSize).getX()- 25;
            OrangeSnake.get(orangeSize).setX(temp);
            ocsize(tempox,tempoy);
        }

        if (x==3)
        {
            temp = OrangeSnake.get(orangeSize).getX()+ 25;
            OrangeSnake.get(orangeSize).setX(temp);
            ocsize(tempox,tempoy);
        }

        if (x==38)
        {
            tempo = GreenSnake.get(greenSize).getY() - 25;
            GreenSnake.get(greenSize).setY(tempo);
            gcsize(tempgx,tempgy);
        }

        if (x==40)
        {
            tempo = GreenSnake.get(greenSize).getY()+ 25;
            GreenSnake.get(greenSize).setY(tempo);
            gcsize(tempgx,tempgy);
        }

        if (x==37)
        {
            tempo = GreenSnake.get(greenSize).getX()- 25;
            GreenSnake.get(greenSize).setX(tempo);
            gcsize(tempgx,tempgy);
        }

        if (x==39)
        {
            tempo = GreenSnake.get(greenSize).getX()+ 25;
            GreenSnake.get(greenSize).setX(tempo);
            gcsize(tempgx,tempgy);
        }

        if (x==4)
        {

        }


        int okx = Math.abs(OrangeSnake.get(orangeSize).getX()-xfood);
        int oky = Math.abs(OrangeSnake.get(orangeSize).getY()-yfood);

        if (okx==0)
        {
            if (oky==0)
            {

                dots  xx = new dots(xfood,yfood);
                OrangeSnake.add(xx);
                orangeSize++;


                xfood = rand.nextInt((39 - 1) + 1);
                yfood = rand.nextInt((39 - 1) + 1);

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

                xfood=xfood*25;
                yfood=yfood*25;
            }
        }

        if (orangeSize >3)
        {
            for (int i =0; i<orangeSize-2; i++)
            {
                if (OrangeSnake.get(orangeSize).getX()==OrangeSnake.get(i).getX() && OrangeSnake.get(orangeSize).getY()==OrangeSnake.get(i).getY())
                {
                    myEnd++;
                }
            }
        }

        for (int i=0; i<GreenSnake.size()-1;i++)
        {
            if (OrangeSnake.get(orangeSize).getX()==GreenSnake.get(i).getX() && OrangeSnake.get(orangeSize).getY()==GreenSnake.get(i).getY())
            {
                myEnd++;
                break;
            }
        }


        if (myEnd!=0)
        {
            theend();
        }

        else
        {
            if(x==0||x==1||x==2||x==3)
            {
                greengog=x;
            }
            if(x==37||x==38||x==39||x==40)
            {
                orangegog=x;
            }

        }

        repaint();
    }

    public void newgame()
    {
        myEnd=0;
        isEnd=0;
        greenSize=0;
        orangeSize=0;
        greengog= -1;
        greengog= -1;
        nojump=0;
        rivalnojump=-1;


        dots  xx = new dots(250,50);
        GreenSnake.add(xx);

        dots  rx = new dots(750,50);
        OrangeSnake.add(rx);

        xfood=500;
        yfood=250;

        input = new JTextField();
        input.setPreferredSize(new Dimension  (0,0));
        input.addKeyListener(this);
        isRun=true;
        this.add(input);

    }

//================================================================================= network


    private boolean connect()
    {
        try
        {
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            return false;
        }

        gogogo=true;

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

    }

}

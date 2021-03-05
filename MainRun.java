package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainRun extends JPanel
{
    private JButton solo,vsh,vsj,vsAI,vslocal;

    public MainRun()
    {
        Listener lis = new Listener();

        solo = new JButton("Single player");
        solo.setPreferredSize(new Dimension(240, 240));
        solo.setFont(solo.getFont().deriveFont(30.0f));
        solo.addActionListener(lis);
        vsh = new JButton("Vs Host");
        vsh.setPreferredSize(new Dimension(240, 240));
        vsh.setFont(vsh.getFont().deriveFont(30.0f));
        vsh.addActionListener(lis);
        vsj = new JButton("Vs Join");
        vsj.setPreferredSize(new Dimension(240, 240));
        vsj.setFont(vsj.getFont().deriveFont(30.0f));
        vsj.addActionListener(lis);
        vsAI = new JButton("Vs AI");
        vsAI.setPreferredSize(new Dimension(240, 240));
        vsAI.setFont(vsAI.getFont().deriveFont(30.0f));
        vsAI.addActionListener(lis);
        vslocal = new JButton("Vs Local");
        vslocal.setPreferredSize(new Dimension(240, 240));
        vslocal.setFont(vslocal.getFont().deriveFont(30.0f));
        vslocal.addActionListener(lis);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));

        buttonPanel.add(solo);
        buttonPanel.add(vslocal);
        buttonPanel.add(vsAI);
        buttonPanel.add(vsh);
        buttonPanel.add(vsj);

        this.add(buttonPanel,BorderLayout.CENTER);

        JFrame fr = new JFrame("Choose a game type");
        fr.add(this);
        fr.pack();
        fr.setResizable(false);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

    private class Listener implements ActionListener
    { public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==solo)
        {
            JFrame window = new JFrame("Single player");
            window.setSize(1040,1040);
            window.setResizable(false);
            Sologamepanel panel=new Sologamepanel();
            window.add(panel);
            window.setVisible(true);
        }

        if(e.getSource()==vsh)
        {
            JFrame window = new JFrame("Vs Host");
            window.setSize(1040,1040);
            window.setResizable(false);
            vshost panel=new vshost();
            window.add(panel);
            window.setVisible(true);
        }

        if(e.getSource()==vsj)
        {
            JFrame window = new JFrame("Vs Join");
            window.setSize(1040,1040);
            window.setResizable(false);
            vsjoin panel=new vsjoin();
            window.add(panel);
            window.setVisible(true);
        }

        if(e.getSource()==vsAI)
        {
            JFrame window = new JFrame("Vs AI");
            window.setSize(1040,1040);
            window.setResizable(false);
            vsAI panel=new vsAI();
            window.add(panel);
            window.setVisible(true);
        }

        if(e.getSource()==vslocal)
        {
            JFrame window = new JFrame("Vs local");
            window.setSize(1040,1040);
            window.setResizable(false);
            vslocal panel=new vslocal();
            window.add(panel);
            window.setVisible(true);
        }

    }
    }

    public static void main(String[] args)
    {
        new MainRun();
    }
}

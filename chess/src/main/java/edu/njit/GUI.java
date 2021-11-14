package edu.njit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI{
    public static LinkedList<GUIPiece> ps=new LinkedList<>();
    public static GUIPiece selectedPiece=null;
    public static String s = "";
    public static boolean moveComplete = false;
    public static GUIPiece kill = null;
    public static String whitecastle = "";
    public static String blackcastle = "";

    
    public static void mainGUI() throws ConcurrentModificationException, IOException {
      BufferedImage all=ImageIO.read(new File("animals.png"));
       final Image imgs[] = new Image[12];
       int ind=0;
       for(int y=0;y<400;y+=200) {
            for(int x=0;x<1200;x+=200) {
               imgs[ind]=all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
               ind++;
            }    
       }

    GUIPiece brook=new GUIPiece(0, 0, false, "rook", ps);
    GUIPiece bkinght=new GUIPiece(1, 0, false, "knight", ps);
    GUIPiece bbishop=new GUIPiece(2, 0, false, "bishop", ps);
    GUIPiece bqueen=new GUIPiece(3, 0, false, "queen", ps);
    GUIPiece bking=new GUIPiece(4, 0, false, "king", ps);
    GUIPiece bbishop2=new GUIPiece(5, 0, false, "bishop", ps);
    GUIPiece bkight2=new GUIPiece(6, 0, false, "knight", ps);
    GUIPiece brook2=new GUIPiece(7, 0, false, "rook", ps);
    GUIPiece bpawn1=new GUIPiece(1, 1, false, "pawn", ps);
    GUIPiece bpawn2=new GUIPiece(2, 1, false, "pawn", ps);
    GUIPiece bpawn3=new GUIPiece(3, 1, false, "pawn", ps);
    GUIPiece bpawn4=new GUIPiece(4, 1, false, "pawn", ps);
    GUIPiece bpawn5=new GUIPiece(5, 1, false, "pawn", ps);
    GUIPiece bpawn6=new GUIPiece(6, 1, false, "pawn", ps);
    GUIPiece bpawn7=new GUIPiece(7, 1, false, "pawn", ps);
    GUIPiece bpawn8=new GUIPiece(0, 1, false, "pawn", ps);
        
    GUIPiece wrook=new GUIPiece(0, 7, true, "rook", ps);
    GUIPiece wkinght=new GUIPiece(1, 7, true, "knight", ps);
    GUIPiece wbishop=new GUIPiece(2, 7, true, "bishop", ps);
    GUIPiece wqueen=new GUIPiece(3, 7, true, "queen", ps);
    GUIPiece wking=new GUIPiece(4, 7, true, "king", ps);
    GUIPiece wbishop2=new GUIPiece(5, 7, true, "bishop", ps);
    GUIPiece wkight2=new GUIPiece(6, 7, true, "knight", ps);
    GUIPiece wrook2=new GUIPiece(7, 7, true, "rook", ps);
    GUIPiece wpawn1=new GUIPiece(1, 6, true, "pawn", ps);
    GUIPiece wpawn2=new GUIPiece(2, 6, true, "pawn", ps);
    GUIPiece wpawn3=new GUIPiece(3, 6, true, "pawn", ps);
    GUIPiece wpawn4=new GUIPiece(4, 6, true, "pawn", ps);
    GUIPiece wpawn5=new GUIPiece(5, 6, true, "pawn", ps);
    GUIPiece wpawn6=new GUIPiece(6, 6, true, "pawn", ps);
    GUIPiece wpawn7=new GUIPiece(7, 6, true, "pawn", ps);
    GUIPiece wpawn8=new GUIPiece(0, 6, true, "pawn", ps);
     
    final JFrame frame = new JFrame();
    frame.setBounds(10,10,512,512);
    frame.setUndecorated(true);
    JPanel pn = new JPanel(){
        @Override
        public void paint(Graphics g){
            boolean green = true;
            Color grass = new Color(53,130,74);
            Color dirt = new Color(105,59,40);
            //Image grass = ImageIO.read(new File("grass.png"));
            //Image dirt = ImageIO.read(new File("dirt.png"));
            //vertical squares
            for(int y= 0; y < 8; y++){
               //horizontal squares
               for(int x=0; x <8; x++){
                  if(green){
                     g.setColor(grass);
                     //g.drawImage(grass,0,0,null);
                  } else {
                     g.setColor(dirt);
                //g.drawImage(dirt,0,0,null);
                  }

                  g.fillRect(x*64,y*64,64,64);
                  green=!green;
               }
               green=!green;
            }
            for (Iterator<GUIPiece> iterator = ps.iterator(); iterator.hasNext(); ) {
               GUIPiece value = iterator.next();
               int ind = 0;
               if(value.name.equalsIgnoreCase("king")) {
                  ind = 0;
                  }
               if(value.name.equalsIgnoreCase("queen")) {
                  ind = 1;
                  }
               if(value.name.equalsIgnoreCase("bishop")) {
                  ind = 2;
                  }
               if(value.name.equalsIgnoreCase("knight")) {
                  ind = 3;
                  }
               if(value.name.equalsIgnoreCase("rook")) {
                  ind = 4;
                  }
               if(value.name.equalsIgnoreCase("pawn")) {
                  ind = 5;
                  }
               if(!value.isGreen){
                  ind+=6;
               }
               g.drawImage(imgs[ind], value.xp*64, value.yp*64, this);
               if (kill != null) {
                     kill.kill();
                     kill = null;
               }
               frame.repaint();
         }

         if (whitecastle.equals("short")){
            GUIPiece.getGUIPiece(7,0).move(5,0);
            whitecastle = "";
         }
         else if (whitecastle.equals("long")){
            GUIPiece.getGUIPiece(0,0).move(3,0);
            whitecastle = "";
         }

         frame.repaint(); 

         if (blackcastle.equals("short")) {
            GUIPiece.getGUIPiece(7,7).move(5,7);
            blackcastle = "";
         }
         else if (blackcastle.equals("long")) {
            GUIPiece.getGUIPiece(0,7).move(3,7);
            blackcastle = "";
         }

         frame.repaint();
         
      }
   };
   
        frame.add(pn);
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedPiece!=null){
                    selectedPiece.x=e.getX()-32;
                    selectedPiece.y=e.getY()-32;
                    frame.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                s = "";
                selectedPiece=getGUIPiece(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int endX = e.getX()/64;
                int endY = e.getY()/64;
                selectedPiece.move(endX, endY);
                frame.repaint();
                moveComplete = true;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
       
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

        
    }

    public static GUIPiece getGUIPiece(int x,int y){
        int xp=x/64;
        int yp=y/64;
        s += xp + "" + yp;
        
        for(GUIPiece p: ps){
            if(p.xp==xp&&p.yp==yp){
                return p;
            }
        }
        return null;
    }

}




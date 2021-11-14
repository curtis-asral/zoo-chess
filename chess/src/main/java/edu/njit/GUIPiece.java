package edu.njit;

import java.util.Iterator;
import java.util.LinkedList;

public class GUIPiece {
    int xp;
    int yp;   
    int x;
    String s= "";
    int y;
    boolean isGreen;
    static LinkedList<GUIPiece> ps;
    String name;
    public GUIPiece(int xp, int yp, boolean isGreen,String n, LinkedList<GUIPiece> ps) {   
        this.xp = xp;
        this.yp = yp;
        x=xp*64;
        y=yp*64;
        this.isGreen = isGreen;
        this.ps=ps;
        name=n;
        ps.add(this);
    }

    public static GUIPiece getGUIPiece(int xp, int yp) {
        for (GUIPiece gp : ps) {
            if (gp.xp == xp && gp.yp == yp)
                return gp;
        }
        return null;
    }
    
    public void move(int xp,int yp){
        if (GUI.getGUIPiece(xp*64, yp*64)!=null)
            GUI.kill = getGUIPiece(xp, yp);
        else {
            x=this.xp*64;
            y=this.yp*64;
        }
        this.xp=xp;
        this.yp=yp;
        x=xp*64;
        y=yp*64;
    }
    
    
    public void kill(){
        ps.remove(this);
    }
}

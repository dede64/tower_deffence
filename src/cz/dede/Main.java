package cz.dede;

import acm.graphics.GCanvas;
import acm.graphics.GObject;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import cz.dede.entities.Turret;
import cz.dede.resources.TDConstants;

import java.awt.event.MouseEvent;

public class Main extends GraphicsProgram implements TDConstants {

    //instance variables
    public static double mouseX;
    public static double mouseY;
    public static GCanvas canvas;
    public static GObject lastClicked = null;
    public static boolean mouseRelease = false;
    public static Turret onClick = null;

    public static void main(String[] args) {
        new Main().start(args);
    }

    public void run(){
        canvas = getGCanvas();
        canvas.setAutoRepaintFlag(false);

        addMouseListeners();

        while (true){
            new Game().run();
        }
    }

    /**
     * method to get coordinates of the mouse
     */
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * method to get coordinates of the mouse
     */
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * method to catch mouse clicked event
     */
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        lastClicked = canvas.getElementAt(new GPoint(e.getPoint()));

    }

    /**
     * method to catch mouse released event
     */
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if(onClick!=null) {
            mouseRelease = true;
        }
    }

}

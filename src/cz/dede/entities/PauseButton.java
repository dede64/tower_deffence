package cz.dede.entities;

import acm.graphics.GPolygon;

import java.awt.*;

import static cz.dede.Main.canvas;
import static cz.dede.resources.TDConstants.*;

public class PauseButton extends Button {

    private GPolygon icon;
    private GPolygon icon2;

    public PauseButton(String text, double x, double y, double length, double height, Color color) {
        super(text, x, y, length, height, color);

        int offset = 12;

        icon = new GPolygon();
        for(int i = 0; i < PAUSE_X.length; i++) {
            icon.addVertex(PAUSE_X[i], PAUSE_Y[i]);
        }
        icon.setFilled(true);
        icon.setColor(Color.BLACK);
        canvas.add(icon, x + offset, y + 10);

        icon2 = new GPolygon();
        for(int i = 0; i < PAUSE_X.length; i++) {
            icon2.addVertex(-PAUSE_X[i], PAUSE_Y[i]);
        }
        icon2.setFilled(true);
        icon2.setColor(Color.BLACK);
        canvas.add(icon2, x + length - offset, y + 10);

        this.getSprites().add(icon);
        this.getSprites().add(icon2);
    }

    public void setVisible(boolean visible){
        if(visible){
            this.getBackground().setVisible(true);
            this.getText().setVisible(true);
            this.getIcon().setVisible(true);
            this.getIcon2().setVisible(true);
        }
        else{
            this.getBackground().setVisible(false);
            this.getText().setVisible(false);
            this.getIcon().setVisible(false);
            this.getIcon2().setVisible(false);
        }
    }

    public void delete(){
        canvas.remove(this.getBackground());
        canvas.remove(this.getText());
        canvas.remove(this.getIcon());
        canvas.remove(this.getIcon2());
    }

    public GPolygon getIcon() {
        return icon;
    }

    public void setIcon(GPolygon icon) {
        this.icon = icon;
    }

    public GPolygon getIcon2() {
        return icon2;
    }

    public void setIcon2(GPolygon icon2) {
        this.icon2 = icon2;
    }

    public void changeIcon(){

    }
}

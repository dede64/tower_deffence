package cz.dede.Entities;

import acm.graphics.GLabel;
import acm.graphics.GRect;

import java.awt.*;

import static cz.dede.Main.canvas;

public class Button{
    private GRect background;
    private GLabel text;
    private double x;
    private double y;
    private double length;
    private double height;
    private Color color;
    private ButtonEventListener buttonEventListener;



    public Button(String text, double x, double y, double length, double height, Color color) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.color = color;

        this.background = new GRect(length, height);
        this.background.setFilled(true);
        this.background.setColor(color);
        canvas.add(this.background, x, y);

        this.text = new GLabel(text);
        this.text.setFont("Impact-25");
        canvas.add(this.text, this.x + this.length / 2 - this.text.getWidth() / 2, this.y + 25);
    }

    public GRect getBackground() {
        return background;
    }

    public void setBackground(GRect background) {
        this.background = background;
    }

    public GLabel getText() {
        return text;
    }

    public void setText(GLabel text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setVisible(boolean visible){
        if(visible){
            background.setVisible(true);
            text.setVisible(true);
        }
        else{
            background.setVisible(false);
            text.setVisible(false);
        }
    }

    public boolean isVisible(){
        return background.isVisible();
    }

    public ButtonEventListener getButtonEventListener() {
        return buttonEventListener;
    }

    public void setButtonEventListener(ButtonEventListener buttonEventListener) {
        this.buttonEventListener = buttonEventListener;
    }

    public void callback(Player player){
        this.buttonEventListener.callback(player);
    }


}

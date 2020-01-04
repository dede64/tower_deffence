package cz.dede.entities;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import cz.dede.resources.TDConstants;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Main.canvas;

public abstract class SideMenu implements TDConstants {

    private GRect background;
    private GLabel title;
    private GLabel author;
    private boolean isHidden = false;

    private ArrayList<Button> buttons = new ArrayList<>();

    //constructor
    public SideMenu(Color color, String title) {
        createBackground(color);
        createTitle(title);
        addAuthor();
    }

    /**
     * method creates background of the sidemenu
     */
    private void createBackground(Color color) {
        this.background = new GRect(SIDE_MENU_WIDTH, canvas.getHeight());
        this.background.setFilled(true);
        this.background.setColor(color);
        canvas.add(this.background, canvas.getWidth()-SIDE_MENU_WIDTH, 0);
    }

    private void createTitle(String title){
        this.title = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 25, "25");
        this.title.setLabel(title);
        this.title.move(-this.title.getWidth()/2, 0);
        this.title.setColor(Color.BLACK);
    }


    /**
     * method to create author name label
     */
    private void addAuthor() {
        this.author = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH/2.0, canvas.getHeight()-10, "17");
        this.author.setLabel("© 2020 dede64");
        this.author.move(-this.author.getWidth()/2, 0);
        this.author.setColor(Color.BLACK);
    }

    /**
     * method to create label on given coordinates
     */
    protected GLabel createLabel(double x, double y, String size) {
        GLabel label = new GLabel("");
        String font = "Franklin Gothic Medium-" + size;
        label.setFont(font);
        label.setColor(Color.GRAY);
        canvas.add(label, x, y);
        return label;
    }

    abstract void hideMenu();

    abstract void showMenu();

    public GRect getBackground() {
        return background;
    }

    public void setBackground(GRect background) {
        this.background = background;
    }

    public GLabel getTitle() {
        return title;
    }

    public void setTitle(GLabel title) {
        this.title = title;
    }

    public GLabel getAuthor() {
        return author;
    }

    public void setAuthor(GLabel author) {
        this.author = author;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }
}

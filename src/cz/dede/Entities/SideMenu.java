package cz.dede.Entities;

import acm.graphics.GLabel;
import acm.graphics.GPolygon;
import acm.graphics.GRect;
import cz.dede.Main;
import cz.dede.resources.TDConstants;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Main.canvas;

/**
 * Class of the side menu
 */
public class SideMenu implements TDConstants {
    public GRect background;
    public GRect button;
    public GLabel nextWaveLabel;
    public GLabel shopLabel;
    public GLabel author;
    public GRect cancel;
    public GRect fasterButton;
    public GLabel fasterButtonLabel;
    public GPolygon shopInfoBox;

    public ArrayList<Turret> turretShop = new ArrayList<Turret>();
    public ArrayList<GLabel> turretNameLabels = new ArrayList<GLabel>();
    public ArrayList<GLabel> shopInfoLabels = new ArrayList<GLabel>();
    public ArrayList<GLabel> turretPriceLabels = new ArrayList<GLabel>();

    //constructor
    public SideMenu() {
        createBackground();
        this.shopLabel = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH/2, 25, "25");
        this.shopLabel.setLabel("SHOP");
        this.shopLabel.move(-this.shopLabel.getWidth()/2, 0);
        this.shopLabel.setColor(Color.BLACK);
        createCancel();
        addTurret("destroyer", canvas.getWidth()-SIDE_MENU_WIDTH/2, 75.0);
        addTurret("knocker", canvas.getWidth()-SIDE_MENU_WIDTH/2, 175.0);
        addTurret("sniper", canvas.getWidth()-SIDE_MENU_WIDTH/2, 275.0);
        addTurret("dome", canvas.getWidth()-SIDE_MENU_WIDTH/2, 375.0);
        addTurret("rocketer", canvas.getWidth()-SIDE_MENU_WIDTH/2, 475.0);
        addButton();
        addNextRoundLabel();
        addAuthor();
        addInfoBox();
    }

    /**
     * method creates background of the sidemenu
     */
    public void createBackground() {
        this.background = new GRect(SIDE_MENU_WIDTH, canvas.getHeight());
        this.background.setFilled(true);
        this.background.setColor(Color.GRAY);
        canvas.add(this.background, canvas.getWidth()-160, 0);
    }

    /**
     * method to create new shop item in side menu and labels for it
     */
    public void addTurret(String type, double x, double y) {
        Turret turret = new Turret(type, x, y);
        this.turretShop.add(turret);
        GLabel labelName = createLabel(x, y+35, "15");
        labelName.setLabel(turret.type);
        labelName.setColor(Color.CYAN);
        labelName.move(-labelName.getWidth()/2, 0);
        this.turretNameLabels.add(labelName);
        GLabel label = createLabel(x, y+50, "15");
        label.setLabel(turret.cost + "$");
        label.setColor(Color.BLACK);
        label.move(-label.getWidth()/2, 0);
        this.turretPriceLabels.add(label);
    }

    /**
     * method to create start button GRect and Faster speed button
     */
    public void addButton() {
        this.button = new GRect(SIDE_MENU_WIDTH-10, 30);
        this.button.setFilled(true);
        this.button.setColor(Color.GREEN);
        canvas.add(this.button, canvas.getWidth()-SIDE_MENU_WIDTH+5, canvas.getHeight()-165);

        this.fasterButton = new GRect(SIDE_MENU_WIDTH-10, 30);
        this.fasterButton.setFilled(true);
        this.fasterButton.setColor(Color.CYAN);
        canvas.add(this.fasterButton, canvas.getWidth()-SIDE_MENU_WIDTH+5, canvas.getHeight()-60);
        this.fasterButton.setVisible(false);
    }

    /**
     * method to create label for a start button and for the faster wave button
     */
    public void addNextRoundLabel() {
        this.nextWaveLabel = new GLabel("Next wave");
        this.nextWaveLabel.setFont("Impact-25");
        canvas.add(this.nextWaveLabel, canvas.getWidth()-SIDE_MENU_WIDTH/2-this.nextWaveLabel.getWidth()/2, this.button.getY()+25);

        this.fasterButtonLabel = new GLabel("Faster");
        this.fasterButtonLabel.setFont("Impact-25");
        canvas.add(this.fasterButtonLabel, canvas.getWidth()-SIDE_MENU_WIDTH/2-this.fasterButtonLabel.getWidth()/2, this.fasterButton.getY()+25);
        this.fasterButtonLabel.setVisible(false);

    }

    /**
     * method to create cancel button
     */
    public void createCancel() {
        this.cancel = new GRect(SIDE_MENU_WIDTH-10, 60);
        this.cancel.setFilled(true);
        this.cancel.setColor(Color.RED);
        this.cancel.setLocation(canvas.getWidth()-SIDE_MENU_WIDTH/2-this.cancel.getWidth()/2, canvas.getHeight()-125 );
    }

    /**
     * method to create author name label
     */
    public void addAuthor() {
        this.author = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH/2, canvas.getHeight()-10, "17");
        this.author.setLabel("© 2019 dede64");
        this.author.move(-this.author.getWidth()/2, 0);
        this.author.setColor(Color.BLACK);
    }

    /**
     * method to create a shop info box and labels on it
     */
    public void addInfoBox() {
        this.shopInfoBox = new GPolygon();
        for(int i = 0; i<INFO_BOX_X.length; i++){this.shopInfoBox.addVertex(INFO_BOX_X[i], INFO_BOX_Y[i]);}
        this.shopInfoBox.setFilled(true);
        this.shopInfoBox.setColor(Color.CYAN);
        canvas.add(this.shopInfoBox, canvas.getWidth()-SIDE_MENU_WIDTH + 50, canvas.getHeight()-60);
        this.shopInfoBox.setVisible(false);

        GLabel info1 = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH - 105, -50, "16");
        info1.setColor(Color.BLACK);
        this.shopInfoLabels.add(info1);
        GLabel info2 = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH - 105, -50, "16");
        info2.setColor(Color.BLACK);
        this.shopInfoLabels.add(info2);
        GLabel info3 = createLabel(canvas.getWidth()-SIDE_MENU_WIDTH - 105, -50, "16");
        info3.setColor(Color.BLACK);
        this.shopInfoLabels.add(info3);
        for(int i = 0; i < this.shopInfoLabels.size(); i++) {
            this.shopInfoLabels.get(i).setVisible(false);
        }
    }

    /**
     * method to create label on given coordinates
     */
    private GLabel createLabel(double x, double y, String size) {
        GLabel label = new GLabel("");
        String font = "Franklin Gothic Medium-" + size;
        label.setFont(font);
        label.setColor(Color.GRAY);
        canvas.add(label, x, y);
        return label;
    }
}

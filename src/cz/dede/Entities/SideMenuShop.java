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
public class SideMenuShop extends SideMenu implements TDConstants {
    private Button nextWaveButton;
    private Button fasterWaveButton;
    private GRect cancel;
    private GPolygon shopInfoBox;

    private ArrayList<Turret> turretShop = new ArrayList<>();
    private ArrayList<GLabel> turretNameLabels = new ArrayList<>();
    private ArrayList<GLabel> shopInfoLabels = new ArrayList<>();
    private ArrayList<GLabel> turretPriceLabels = new ArrayList<>();

    //constructor
    public SideMenuShop() {
        super(Color.GRAY, "SHOP");
        createCancel();
        addTurret("destroyer", canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 75.0);
        addTurret("knocker", canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 175.0);
        addTurret("sniper", canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 275.0);
        addTurret("dome", canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 375.0);
        addTurret("rocketer", canvas.getWidth()-SIDE_MENU_WIDTH/2.0, 475.0);
        this.nextWaveButton = new Button("Next wave!", canvas.getWidth()-SIDE_MENU_WIDTH+5, canvas.getHeight()-165, SIDE_MENU_WIDTH - 10, 30, Color.GREEN);
        this.nextWaveButton.setButtonEventListener((Player player)->{
            player.setStarted(true);
        });
        this.fasterWaveButton = new Button("Faster", canvas.getWidth()-SIDE_MENU_WIDTH+5, canvas.getHeight()-60, SIDE_MENU_WIDTH - 10, 30, Color.CYAN);
        this.fasterWaveButton.setVisible(false);
        this.fasterWaveButton.setButtonEventListener((Player player)->{
            System.out.println("asfawfafaw");
            player.setTick(FAST_TICK);
        });
        this.getButtons().add(this.nextWaveButton);
        this.getButtons().add(this.fasterWaveButton);

        addInfoBox();
    }

    /**
     * method to create new shop item in side menu and labels for it
     */
    private void addTurret(String type, double x, double y) {
        Turret turret = new Turret(type, x, y);
        this.turretShop.add(turret);
        GLabel labelName = createLabel(x, y+35, "15");
        labelName.setLabel(turret.getType());
        labelName.setColor(Color.CYAN);
        labelName.move(-labelName.getWidth()/2, 0);
        this.turretNameLabels.add(labelName);
        GLabel label = createLabel(x, y+50, "15");
        label.setLabel(turret.getCost() + "$");
        label.setColor(Color.BLACK);
        label.move(-label.getWidth()/2, 0);
        this.turretPriceLabels.add(label);
    }


    /**
     * method to create cancel button
     */
    private void createCancel() {
        this.cancel = new GRect(SIDE_MENU_WIDTH-10, 60);
        this.cancel.setFilled(true);
        this.cancel.setColor(Color.RED);
        this.cancel.setLocation(canvas.getWidth()-SIDE_MENU_WIDTH/2.0-this.cancel.getWidth()/2, canvas.getHeight()-125 );
    }

    /**
     * method to create a shop info box and labels on it
     */
    private void addInfoBox() {
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
        for (GLabel shopInfoLabel : this.shopInfoLabels) {
            shopInfoLabel.setVisible(false);
        }
    }

    @Override
    public void hideMenu() {
        if(!this.isHidden()){
            for(int i = 0; i < turretShop.size(); i++){
                turretNameLabels.get(i).setVisible(false);
                turretPriceLabels.get(i).setVisible(false);
                turretShop.get(i).getBase().setVisible(false);
                turretShop.get(i).getCanon().setVisible(false);
            }
            for(Button button : this.getButtons()){
                button.setVisible(false);
            }
            this.getTitle().setVisible(false);
            this.getBackground().setVisible(false);
            this.setHidden(true);
            this.getAuthor().setVisible(false);
        }
    }

    @Override
    public void showMenu() {
        if(this.isHidden()){
            for(int i = 0; i < turretShop.size(); i++){
                turretNameLabels.get(i).setVisible(true);
                turretPriceLabels.get(i).setVisible(true);
                turretShop.get(i).getBase().setVisible(true);
                turretShop.get(i).getCanon().setVisible(true);
            }

            for(Button button : this.getButtons()){
                button.setVisible(true);
            }
            this.getTitle().setVisible(true);
            this.getBackground().setVisible(true);
            this.setHidden(false);
            this.getAuthor().setVisible(true);
        }
    }


    public GRect getCancel() {
        return cancel;
    }

    public void setCancel(GRect cancel) {
        this.cancel = cancel;
    }

    public GPolygon getShopInfoBox() {
        return shopInfoBox;
    }

    public void setShopInfoBox(GPolygon shopInfoBox) {
        this.shopInfoBox = shopInfoBox;
    }

    public ArrayList<Turret> getTurretShop() {
        return turretShop;
    }

    public void setTurretShop(ArrayList<Turret> turretShop) {
        this.turretShop = turretShop;
    }

    public ArrayList<GLabel> getTurretNameLabels() {
        return turretNameLabels;
    }

    public void setTurretNameLabels(ArrayList<GLabel> turretNameLabels) {
        this.turretNameLabels = turretNameLabels;
    }

    public ArrayList<GLabel> getShopInfoLabels() {
        return shopInfoLabels;
    }

    public void setShopInfoLabels(ArrayList<GLabel> shopInfoLabels) {
        this.shopInfoLabels = shopInfoLabels;
    }

    public ArrayList<GLabel> getTurretPriceLabels() {
        return turretPriceLabels;
    }

    public void setTurretPriceLabels(ArrayList<GLabel> turretPriceLabels) {
        this.turretPriceLabels = turretPriceLabels;
    }

    public Button getNextWaveButton() {
        return nextWaveButton;
    }

    public void setNextWaveButton(Button nextWaveButton) {
        this.nextWaveButton = nextWaveButton;
    }

    public Button getFasterWaveButton() {
        return fasterWaveButton;
    }

    public void setFasterWaveButton(Button fasterWaveButton) {
        this.fasterWaveButton = fasterWaveButton;
    }
}

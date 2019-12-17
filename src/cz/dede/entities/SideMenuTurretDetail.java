package cz.dede.entities;

import acm.graphics.GLabel;

import java.awt.*;

import static cz.dede.Main.canvas;

public class SideMenuTurretDetail extends SideMenu {

    private Turret turretSprite;
    private Turret turret;
    private GLabel turretDMGInfo;
    private GLabel turretRangeInfo;
    private Button confirm;
    private boolean isAvailable = false;

    public SideMenuTurretDetail(Turret turret) {

        super(Color.lightGray, turret.getType().toUpperCase());

        this.turretSprite = Turret.makeTurret(turret.getType(), canvas.getWidth() - SIDE_MENU_WIDTH / 2.0, 100);
        this.turret = turret;

        this.turretDMGInfo = createLabel(canvas.getWidth() - SIDE_MENU_WIDTH , 150, "16");
        this.turretDMGInfo.setLabel("DMG: " + turret.getDmg() + " (+" + 0.2 * turret.getDmg() + ")");
        this.turretDMGInfo.setColor(Color.BLACK);
        this.turretDMGInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretDMGInfo.getWidth() / 2.0, 150);

        this.turretRangeInfo = createLabel(canvas.getWidth() - SIDE_MENU_WIDTH , 200, "16");
        this.turretRangeInfo.setLabel("Range: " + turret.getRange() + "m (+" + 0.2 * turret.getRange() + "m)");
        this.turretRangeInfo.setColor(Color.BLACK);
        this.turretRangeInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretRangeInfo.getWidth() / 2.0, 200);

        this.confirm = new Button((turret.getCost() / 2) + "$", canvas.getWidth() - SIDE_MENU_WIDTH + 40, 250, SIDE_MENU_WIDTH - 80, 30, Color.RED);
    }

    public void update(Player player){
        if (player.getMoney() >= turret.getCost() / 2){
            if(!isAvailable){
                isAvailable = true;
                this.confirm.getBackground().setColor(Color.GREEN);
//                this.confirm.setButtonEventListener((Player player)->{
//                    player.setStarted(true);
//                });
            }
        }else{
            if(isAvailable){
                isAvailable = false;
                this.confirm.getBackground().setColor(Color.RED);
            }
        }
    }

    @Override
    public void hideMenu() {
        if(!isHidden()){
            this.getTitle().setVisible(false);
            this.getBackground().setVisible(false);
            this.getAuthor().setVisible(false);
            this.setHidden(true);
            this.turretSprite.getBase().setVisible(false);
            this.turretSprite.getCanon().setVisible(false);
            this.turretRangeInfo.setVisible(false);
            this.turretDMGInfo.setVisible(false);
            confirm.setVisible(false);
        }
    }

    public void delete(){

        canvas.remove(this.getBackground());
        canvas.remove(this.getAuthor());
        canvas.remove(this.getTitle());
        canvas.remove(this.turretSprite.getBase());
        canvas.remove(this.turretSprite.getCanon());
        canvas.remove(this.turretDMGInfo);
        canvas.remove(this.turretRangeInfo);
        confirm.delete();
    }

    @Override
    public void showMenu() {
        if(isHidden()){
            this.getTitle().setVisible(true);
            this.getBackground().setVisible(true);
            this.getAuthor().setVisible(true);
            this.setHidden(false);
            this.turretSprite.getBase().setVisible(true);
            this.turretSprite.getCanon().setVisible(true);
            this.turretRangeInfo.setVisible(true);
            this.turretDMGInfo.setVisible(true);
            confirm.setVisible(true);
        }
    }
}

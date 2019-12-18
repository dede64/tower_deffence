package cz.dede.entities;

import acm.graphics.GLabel;

import java.awt.*;

import static cz.dede.Main.canvas;

public class SideMenuTurretDetail extends SideMenu {

    private Turret turretSprite;
    private Turret turret;
    private GLabel turretDMGInfo;
    private GLabel turretRangeInfo;
    private GLabel turretAttackSpeedInfo;
    private Button confirm;
    private boolean isAvailable = false; // TODO (Each turret should have own uprade pattern)

    public SideMenuTurretDetail(Turret turret) {

        super(Color.lightGray, turret.getType().toUpperCase());

        this.turretSprite = Turret.makeTurret(turret.getType(), canvas.getWidth() - SIDE_MENU_WIDTH / 2.0, 100);
        this.turret = turret;

        this.turretDMGInfo = createLabel(canvas.getWidth() - SIDE_MENU_WIDTH , 150, "16");
        this.turretDMGInfo.setLabel(getDMGInfo());
        this.turretDMGInfo.setColor(Color.BLACK);
        this.turretDMGInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretDMGInfo.getWidth() / 2.0, 180);

        this.turretRangeInfo = createLabel(canvas.getWidth() - SIDE_MENU_WIDTH , 200, "16");
        this.turretRangeInfo.setLabel(getRangeInfo());
        this.turretRangeInfo.setColor(Color.BLACK);
        this.turretRangeInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretRangeInfo.getWidth() / 2.0, 230);

        this.turretAttackSpeedInfo = createLabel(canvas.getWidth() - SIDE_MENU_WIDTH , 200, "16");
        this.turretAttackSpeedInfo.setLabel(getReloadInfo());
        this.turretAttackSpeedInfo.setColor(Color.BLACK);
        this.turretAttackSpeedInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretAttackSpeedInfo.getWidth() / 2.0, 280);

        this.confirm = new Button((turret.getCost() / 2) + "$", canvas.getWidth() - SIDE_MENU_WIDTH + 40, 310, SIDE_MENU_WIDTH - 80, 30, Color.RED);
        this.getButtons().add(this.confirm);
    }

    public void update(Player player){
        if (player.getMoney() >= turret.getCost() / 2){
            if(!isAvailable){
                isAvailable = true;
                this.confirm.getBackground().setColor(Color.GREEN);
            }
        }else{
            if(isAvailable){
                isAvailable = false;
                this.confirm.getBackground().setColor(Color.RED);
            }
        }
    }

    public void buy(Player player){
        if(player.getMoney() >= turret.getCost() /2){
            player.setMoney(player.getMoney() - turret.getCost() / 2.0);

            turret.setDmg(turret.getDmg() * 1.2);
            turret.setRange(turret.getRange() * 1.2);
            turret.setReloadTime(turret.getReloadTime() * 0.9);

            this.turretDMGInfo.setLabel(getDMGInfo());
            this.turretDMGInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretDMGInfo.getWidth() / 2.0, 180);
            this.turretRangeInfo.setLabel(getRangeInfo());
            this.turretRangeInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretRangeInfo.getWidth() / 2.0, 230);
            this.turretAttackSpeedInfo.setLabel(getReloadInfo());
            this.turretAttackSpeedInfo.setLocation(canvas.getWidth() - SIDE_MENU_WIDTH/2.0 - this.turretAttackSpeedInfo.getWidth() / 2.0, 280);

            this.turret.setCost((int) (this.turret.getCost() * 1.4));
            this.confirm.getText().setLabel(turret.getCost() / 2 + "$");
            this.turret.getBase().setColor(this.turret.getBase().getColor().darker());
            this.update(player);//TODO change positions of upgrade labels after upgrade

        }
    }

    private String getDMGInfo(){
        return String.format("DMG: %.1f (+%.1f)", turret.getDmg(), turret.getDmg() * 0.2);
    }

    private String getRangeInfo(){
        return String.format("Range: %.0fm (+%.0fm)", turret.getRange(), turret.getRange() * 0.2);
    }

    private String getReloadInfo(){
        return String.format("Reload: %.3fs (-%.3fs)", TICK / 1000.0 * turret.getReloadTime(), TICK / 1000.0 * 0.1 * turret.getReloadTime());
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
            this.turretAttackSpeedInfo.setVisible(false);
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
        canvas.remove(this.turretAttackSpeedInfo);
        confirm.delete();
        this.getButtons().remove(confirm);
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
            this.turretAttackSpeedInfo.setVisible(true);
            confirm.setVisible(true);
        }
    }

    public Turret getTurretSprite() {
        return turretSprite;
    }

    public void setTurretSprite(Turret turretSprite) {
        this.turretSprite = turretSprite;
    }

    public Turret getTurret() {
        return turret;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }

    public GLabel getTurretDMGInfo() {
        return turretDMGInfo;
    }

    public void setTurretDMGInfo(GLabel turretDMGInfo) {
        this.turretDMGInfo = turretDMGInfo;
    }

    public GLabel getTurretRangeInfo() {
        return turretRangeInfo;
    }

    public void setTurretRangeInfo(GLabel turretRangeInfo) {
        this.turretRangeInfo = turretRangeInfo;
    }

    public Button getConfirm() {
        return confirm;
    }

    public void setConfirm(Button confirm) {
        this.confirm = confirm;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

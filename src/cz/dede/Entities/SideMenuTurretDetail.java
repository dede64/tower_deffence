package cz.dede.Entities;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static cz.dede.Main.canvas;

public class SideMenuTurretDetail extends SideMenu {

    private Turret turret;

    public SideMenuTurretDetail(Turret turret) {

        super(Color.lightGray, turret.getType().toUpperCase());

        this.turret = new Turret(turret.getType(), canvas.getWidth() - SIDE_MENU_WIDTH/2, 100);
    }

    @Override
    public void hideMenu() {
        if(!isHidden()){
            this.getTitle().setVisible(false);
            this.getBackground().setVisible(false);
            this.getAuthor().setVisible(false);
            this.setHidden(true);
            this.turret.getBase().setVisible(false);
            this.turret.getCanon().setVisible(false);
        }
    }

    public void delete(){

        canvas.remove(this.getBackground());
        canvas.remove(this.getAuthor());
        canvas.remove(this.getTitle());
        canvas.remove(this.turret.getBase());
        canvas.remove(this.turret.getCanon());
    }

    @Override
    public void showMenu() {
        if(isHidden()){
            this.getTitle().setVisible(true);
            this.getBackground().setVisible(true);
            this.getAuthor().setVisible(true);
            this.setHidden(false);
        }
    }
}

package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Knocker extends Turret {

    public Knocker(String type, double x, double y) {
        super(type, x, y);

        this.type = "knocker";
        this.targetType = KNOCKER_TARGET;
        this.range = KNOCKER_RANGE;
        this.dmg = KNOCKER_DMG;
        this.reloadTime = KNOCKER_RELOAD;
        this.bulletSpeed = KNOCKER_BULLET_SPEED;
        this.cost = KNOCKER_COST;
        this.xBaseCoordinates = KNOCKER_BASE_X;
        this.yBaseCoordinates = KNOCKER_BASE_Y;
        this.xCanonCoordinates = DESTROYER_CANON_X;
        this.yCanonCoordinates = DESTROYER_CANON_Y;
        Color color = new Color(150,150,150);
        this.base = createBase(color, this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLUE, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }
}

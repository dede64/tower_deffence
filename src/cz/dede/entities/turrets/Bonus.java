package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Bonus extends Turret {
    public Bonus(String type, double x, double y) {
        super(type, x, y);

        this.type = "bonus";
        this.targetType = BONUS_TARGET;
        this.range = BONUS_RANGE;
        this.dmg = BONUS_DMG;
        this.reloadTime = BONUS_RELOAD;
        this.bulletSpeed = BONUS_BULLET_SPEED;
        this.cost = BONUS_COST;
        this.xBaseCoordinates = BONUS_BASE_X;
        this.yBaseCoordinates = BONUS_BASE_Y;
        this.xCanonCoordinates = BONUS_CANON_X;
        this.yCanonCoordinates = BONUS_CANON_Y;
        this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }
}

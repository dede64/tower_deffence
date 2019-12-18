package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Sniper extends Turret {
    public Sniper(String type, double x, double y) {
        super(type, x, y);

        this.type = "sniper";
        this.targetType = SNIPER_TARGET;
        this.range = SNIPER_RANGE;
        this.dmg = SNIPER_DMG;
        this.reloadTime = SNIPER_RELOAD;
        this.bulletSpeed = SNIPER_BULLET_SPEED;
        this.cost = SNIPER_COST;
        this.xBaseCoordinates = SNIPER_BASE_X;
        this.yBaseCoordinates = SNIPER_BASE_Y;
        this.xCanonCoordinates = SNIPER_CANON_X;
        this.yCanonCoordinates = SNIPER_CANON_Y;
        this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DAMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }
}

package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Rocketer extends Turret {

    public Rocketer(String type, double x, double y) {
        super(type, x, y);

        this.type = "rocketer";
        this.targetType = ROCKET_TARGET;
        this.range = ROCKET_RANGE;
        this.dmg = ROCKET_DMG;
        this.reloadTime = ROCKET_RELOAD;
        this.bulletSpeed = ROCKET_BULLET_SPEED;
        this.cost = ROCKET_COST;
        this.xBaseCoordinates = ROCKET_BASE_X;
        this.yBaseCoordinates = ROCKET_BASE_Y;
        this.xCanonCoordinates = ROCKET_CANON_X;
        this.yCanonCoordinates = ROCKET_CANON_Y;
        this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DAMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
        this.canonLength = 40;
        this.investedMoney = this.cost;
    }
}

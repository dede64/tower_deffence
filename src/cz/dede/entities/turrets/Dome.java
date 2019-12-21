package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Dome extends Turret {
    public Dome(String type, double x, double y) {
        super(type, x, y);

        this.type = "dome";
        this.targetType = DOME_TARGET;
        this.range = DOME_RANGE;
        this.dmg = DOME_DMG;
        this.reloadTime = DOME_RELOAD;
        this.bulletSpeed = DOME_BULLET_SPEED;
        this.cost = DOME_COST;
        this.xBaseCoordinates = DESTROYER_BASE_X;
        this.yBaseCoordinates = DESTROYER_BASE_Y;
        this.xCanonCoordinates = DESTROYER_CANON_X;
        this.yCanonCoordinates = DESTROYER_CANON_Y;
        this.base = createBase(new Color(60, 60, 150), this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DAMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
        this.canonLength = 40;
        this.investedMoney = this.cost;
    }
}

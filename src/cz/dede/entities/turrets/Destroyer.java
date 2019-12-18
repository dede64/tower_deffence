package cz.dede.entities.turrets;

import cz.dede.entities.Turret;

import java.awt.*;

public class Destroyer extends Turret {

    public Destroyer(String type, double x, double y) {
        super(type, x, y);

        this.type = "destroyer";
        this.targetType = DESTROYER_TARGET;
        this.range = DESTROYER_RANGE;
        this.dmg = DESTROYER_DMG;
        this.reloadTime = DESTROYER_RELOAD;
        this.bulletSpeed = DESTROYER_BULLET_SPEED;
        this.cost = DESTROYER_COST;
        this.xBaseCoordinates = DESTROYER_BASE_X;
        this.yBaseCoordinates = DESTROYER_BASE_Y;
        this.xCanonCoordinates = DESTROYER_CANON_X;
        this.yCanonCoordinates = DESTROYER_CANON_Y;
        this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
        this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
        this.damageRadius = DAMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }
}

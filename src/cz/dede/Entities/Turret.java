package cz.dede.Entities;

import acm.graphics.GPolygon;
import acm.graphics.*;
import acm.program.*;

import cz.dede.Main;
import cz.dede.resources.TDConstants;


import java.awt.*;

import static cz.dede.Main.canvas;

/**
 * Class of Turret object
 * */
public class Turret implements TDConstants {
    private String type;
    private String targetType;
    private double range;
    private double dmg; //TODO create getter and setter
    private int reloadTime;
    private int currentLoad;
    private double bulletSpeed;
    private double x;
    private double y;
    private double demageRadius;
    private GPolygon base;
    private GPolygon canon;
    private int cost;
    private Enemy target;
    private double lastRotation = 0;
    private double[] xBaseCoordinates;
    private double[] yBaseCoordinates;
    private double[] xCanonCoordinates;
    private double[] yCanonCoordinates;

    //constructor
    public Turret(String type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        if(this.type.equals("destroyer")) {
            makeDestroyer();
        }
        else if(this.type.equals("knocker")) {
            makeKnocker();
        }
        else if(this.type.equals("sniper")) {
            makeSniper();
        }
        else if(this.type.equals("dome")) {
            makeDome();
        }
        else if(this.type.equals("rocketer")) {
            makeRocketer();
        }
        else if(this.type.equals("bonus")) {
            makeBonus();
        }
    }

    /**
     * create destroyer canon
     */
    public void makeDestroyer() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * create knocker canon
     */
    public void makeKnocker() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * create sniper canon
     */
    public void makeSniper() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * create dome canon
     */
    public void makeDome() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * create rocket launcher
     */
    public void makeRocketer() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * create rocket bonus
     */
    public void makeBonus() {
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
        this.demageRadius = DEMAGE_RADIUS;
        this.currentLoad = this.reloadTime;
    }

    /**
     * method to create base of the turret
     */
    public GPolygon createBase(Color color, double[] xCoordinates, double[] yCoordinates) {
        GPolygon base = new GPolygon();
        for(int i = 0; i<xCoordinates.length; i++) {
            base.addVertex(xCoordinates[i], yCoordinates[i]);
        }
        base.setFilled(true);
        base.setColor(color);


        canvas.add(base, this.x, this.y);
        return base;
    }

    /**
     * method to create canon of the turret
     */
    public GPolygon createCanon(Color color, double[] xCoordinates, double[] yCoordinates) {
        GPolygon canon = new GPolygon();
        for(int i = 0; i<xCoordinates.length; i++) {
            canon.addVertex(xCoordinates[i], yCoordinates[i]);
        }
        canon.setFilled(true);
        canon.setColor(color);
        canvas.add(canon, this.x, this.y);
        return canon;
    }

    /**
     * method to move the object to location
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        this.base.setLocation(this.x, this.y);
        this.canon.setLocation(this.x, this.y);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getDmg() {
        return dmg;
    }

    public void setDmg(double dmg) {
        this.dmg = dmg;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDemageRadius() {
        return demageRadius;
    }

    public void setDemageRadius(double demageRadius) {
        this.demageRadius = demageRadius;
    }

    public GPolygon getBase() {
        return base;
    }

    public void setBase(GPolygon base) {
        this.base = base;
    }

    public GPolygon getCanon() {
        return canon;
    }

    public void setCanon(GPolygon canon) {
        this.canon = canon;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public double getLastRotation() {
        return lastRotation;
    }

    public void setLastRotation(double lastRotation) {
        this.lastRotation = lastRotation;
    }

    public double[] getxBaseCoordinates() {
        return xBaseCoordinates;
    }

    public void setxBaseCoordinates(double[] xBaseCoordinates) {
        this.xBaseCoordinates = xBaseCoordinates;
    }

    public double[] getyBaseCoordinates() {
        return yBaseCoordinates;
    }

    public void setyBaseCoordinates(double[] yBaseCoordinates) {
        this.yBaseCoordinates = yBaseCoordinates;
    }

    public double[] getxCanonCoordinates() {
        return xCanonCoordinates;
    }

    public void setxCanonCoordinates(double[] xCanonCoordinates) {
        this.xCanonCoordinates = xCanonCoordinates;
    }

    public double[] getyCanonCoordinates() {
        return yCanonCoordinates;
    }

    public void setyCanonCoordinates(double[] yCanonCoordinates) {
        this.yCanonCoordinates = yCanonCoordinates;
    }

    public void addReload(int reload){
        this.currentLoad += reload;
    }

    public void addReload(){
        this.addReload(1);
    }
}

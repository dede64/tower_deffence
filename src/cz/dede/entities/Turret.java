package cz.dede.entities;

import acm.graphics.GPolygon;

import acm.util.RandomGenerator;
import cz.dede.Main;
import cz.dede.entities.turrets.*;
import cz.dede.resources.TDConstants;
import javafx.util.Pair;


import java.awt.*;

import static cz.dede.Main.canvas;

/**
 * Class of Turret object
 * */
public class Turret implements TDConstants {
    protected String type;
    protected String targetType;
    protected double range;
    protected double dmg;
    protected double reloadTime;
    protected double currentLoad;
    protected double bulletSpeed;
    protected double x;
    protected double y;
    protected double damageRadius;
    protected GPolygon base;
    protected GPolygon canon;
    protected int cost;
    protected Enemy target;
    protected double lastRotation = 0;
    protected double[] xBaseCoordinates;
    protected double[] yBaseCoordinates;
    protected double[] xCanonCoordinates;
    protected double[] yCanonCoordinates;
    protected double canonLength = 0;

    //constructor
    public Turret(String type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public static Turret makeTurret(String type, double x, double y){
        switch (type){
            case "destroyer":
                return new Destroyer(type, x, y);
            case "knocker":
                return new Knocker(type, x, y);
            case "sniper":
                return new Sniper(type, x, y);
            case "dome":
                return new Dome(type, x, y);
            case "rocketer":
                return new Rocketer(type, x, y);
            case "bonus":
                return new Bonus(type, x, y);
            default:
                return null;
        }
    }

    public Pair<Double, Double> getCanonEnd(){
        if (this.target == null){
            return new Pair<>(this.x, this.y);
        }
        double angle = Main.getAngle(x, y, getTarget().getX(), getTarget().getY());
        double xCoord = x - Math.sin(Math.toRadians(angle)) * canonLength;
        double yCoord = y - Math.cos(Math.toRadians(angle)) * canonLength;
        return new Pair<>(xCoord, yCoord);
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

    public double getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(double reloadTime) {
        this.reloadTime = reloadTime;
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(double currentLoad) {
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

    public double getDamageRadius() {
        return damageRadius;
    }

    public void setDamageRadius(double damageRadius) {
        this.damageRadius = damageRadius;
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

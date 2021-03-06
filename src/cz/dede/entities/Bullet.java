package cz.dede.entities;

import acm.graphics.GPolygon;
import cz.dede.Game;
import cz.dede.resources.TDConstants;

import static cz.dede.Main.canvas;

/**
 * Class of Bullet object
 * */
public class Bullet implements TDConstants {
    private Enemy enemy;
    private double speed;
    private double dmg;
    private double x;
    private double y;
    private double damageRadius;
    private double lastRotation = 0;
    private GPolygon bullet;

    //constructor
    public Bullet(Enemy enemy, Turret turret, double x, double y) {
        this.enemy = enemy;
        this.x = x;
        this.y = y;
        this.dmg = turret.getDmg();
        this.speed = turret.getBulletSpeed();
        this.bullet = createBullet();
        this.damageRadius = turret.getDamageRadius();
    }

    /**
     * method to create bullet shape
     */
    public GPolygon createBullet() {
        GPolygon p = new GPolygon();
        p.addVertex(0, -5);
        p.addVertex(-5, 5);
        p.addVertex(5, 5);
        p.addVertex(0, -5); //TODO each turret should have own shape of bullet (sniper long - knocker smaller etc...)
        p.setFilled(true);
        canvas.add(p, this.x, this.y);
        return p;
    }

    /**
     * method to move the bullet - it checks in which angle the enemy is and then creates the speed (x, y)which equeals the total speed of bullet.
     * than it rotates the bullet in the direction of movement
     */
    public void move(double elapsedTime) {
        double x_diff = this.enemy.getX() - this.x;
        double y_diff = this.enemy.getY() - this.y;
        double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
        double ratio = distance/this.speed/elapsedTime;
        double x_speed = x_diff/ratio;
        double y_speed = y_diff/ratio;
        this.x += x_speed;
        this.y += y_speed;
        this.bullet.setLocation(this.x, this.y);
        double rotation = Game.getAngle(this.x, this.y, enemy.getX(), enemy.getY());
        this.bullet.rotate(-this.lastRotation);
        this.bullet.rotate(rotation);
        this.lastRotation = rotation;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void delete(){
        canvas.remove(this.bullet);
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDmg() {
        return dmg;
    }

    public void setDmg(double dmg) {
        this.dmg = dmg;
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

    public double getLastRotation() {
        return lastRotation;
    }

    public void setLastRotation(double lastRotation) {
        this.lastRotation = lastRotation;
    }

    public GPolygon getBullet() {
        return bullet;
    }

    public void setBullet(GPolygon bullet) {
        this.bullet = bullet;
    }
}

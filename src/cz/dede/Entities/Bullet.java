package cz.dede.Entities;

import acm.graphics.GPolygon;
import cz.dede.Main;
import cz.dede.resources.TDConstants;

import static cz.dede.Main.canvas;

/**
 * Class of Bullet object
 * */
public class Bullet implements TDConstants {
    public Enemy enemy;
    public double speed;
    public double dmg;
    public double x;
    public double y;
    public double demage_radius;
    public double last_rotation = 0;
    public GPolygon bullet;

    //constructor
    public Bullet(Enemy enemy, Turret turret, double x, double y) {
        this.enemy = enemy;
        this.x = x;
        this.y = y;
        this.dmg = turret.dmg;
        this.speed = turret.bulletSpeed;
        this.bullet = createBullet();
        this.demage_radius = turret.demage_radius;
    }

    /**
     * method to create bullet shape
     */
    public GPolygon createBullet() {
        GPolygon p = new GPolygon();
        p.addVertex(0, -5);
        p.addVertex(-5, 5);
        p.addVertex(5, 5);
        p.addVertex(0, -5);
        p.setFilled(true);
        canvas.add(p, this.x, this.y);
        return p;
    }

    /**
     * method to move the bullet - it checks in which angle the enemy is and then creates the speed (x, y)which equeals the total speed of bullet.
     * than it rotates the bullet in the direction of movement
     */
    public void move() {
        double x_diff = this.enemy.x - this.x;
        double y_diff = this.enemy.y - this.y;
        double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
        double ratio = distance/this.speed;
        double x_speed = x_diff/ratio;
        double y_speed = y_diff/ratio;
        this.x += x_speed;
        this.y += y_speed;
        this.bullet.setLocation(this.x, this.y);
        double rotation = Main.getAngle(0, this.x, 0, this.y);
        this.bullet.rotate(-this.last_rotation);
        this.bullet.rotate(rotation);
        this.last_rotation = rotation;
    }
}

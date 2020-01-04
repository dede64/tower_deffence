package cz.dede.entities;

import acm.graphics.GPolygon;
import acm.graphics.GRect;
import cz.dede.Game;
import cz.dede.entities.enemies.*;
import cz.dede.resources.TDConstants;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Main.canvas;

/**
 * Class of Enemy object
 * */
public class Enemy implements TDConstants {
    protected double speed;
    protected double maxHealth;
    protected double health;
    protected String type;
    protected int award;
    protected double healing;
    protected double x;
    protected double y;
    protected double lastRotation = 0;
    protected GPolygon vehicle;
    protected GRect greenHealth;
    protected GRect redHealth;
    protected ArrayList<Double> pathX;
    protected ArrayList<Double> pathY;
    protected double[] xCoordinates;
    protected double[] yCoordinates;
    protected String movement;
    protected int wave;

    //constructor
    public Enemy(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int wave_number) {
        this.wave = wave_number;
        this.pathX = copyArrayList(pathX);
        this.pathY = copyArrayList(pathY);
        this.type = type;
        this.x = this.pathX.get(0);
        this.y = this.pathY.get(0);
        this.pathX.remove(0);
        this.pathY.remove(0);
        this.movement = "ground";
        makeHealthBar();
    }

    public static Enemy createEnemy(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int wave_number){
        if(type.equals("puncher")) {
            return new Puncher(type, pathX, pathY, wave_number);
        }
        if(type.equals("puncher_speeder")) {
            return new PuncherSpeeder(type, pathX, pathY, wave_number);
        }
        if(type.equals("puncher_healer")) {
            return new PuncherHealer(type, pathX, pathY, wave_number);
        }
        if(type.equals("flyer")) {
            return new Flyer(type, pathX, pathY, wave_number);
        }
        if(type.equals("briger")) {
            return new Briger(type, pathX, pathY, wave_number);
        }
        return null;
    }

    /**
     * method to create enemy vehicle
     */
    public GPolygon createVehicle(Color color, double[] xCoordinates, double[] yCoordinates) {
        GPolygon v = new GPolygon();
        for(int i = 0; i<xCoordinates.length; i++) {
            v.addVertex(xCoordinates[i], yCoordinates[i]);
        }
        v.setFilled(true);
        v.setColor(color);
        canvas.add(v, this.x, this.y);
        return v;
    }

    /**
     * method to move the enemy to the next point of path, if it is on the end of path it destroys object and substract a life
     */
    public void move(ArrayList<Enemy> enemies, Player player, double elapsedTime) {
        double goalX = this.pathX.get(0);
        double goalY = this.pathY.get(0);
        double distanceX = goalX-this.x;
        double distanceY = goalY-this.y;
        double distance = Game.countDistance(distanceX, distanceY);
        if(distance<5) {
            if(this.pathX.size()==1) {
                GPolygon shape = this.vehicle;
                GRect greenBar = this.greenHealth;
                GRect redBar = this.redHealth;
                canvas.remove(shape);
                canvas.remove(greenBar);
                canvas.remove(redBar);
                enemies.remove(this);
                player.setLives(player.getLives() - 1);
            }
            else {
                this.pathX.remove(0);
                this.pathY.remove(0);
            }
        }
        else {
            this.x += distanceX/distance*this.speed*elapsedTime;
            this.y += distanceY/distance*this.speed*elapsedTime;
            this.vehicle.setLocation(this.x, this.y);
            this.redHealth.setLocation(this.x-15, this.y-20);
            this.greenHealth.setLocation(this.x-15, this.y-20);
            double rotation = Game.getAngle(0, distanceX, 0, distanceY);
            if(distanceX/distance*this.speed*elapsedTime>this.speed*0.9*elapsedTime) {
                rotation = -180;
            }
            this.vehicle.rotate(-this.lastRotation/2);
            this.vehicle.rotate(rotation/2);
            this.lastRotation = rotation;
        }
    }

    /**
     * method to create health bar
     */
    public void makeHealthBar() {
        this.redHealth = new GRect(30, 5);
        this.redHealth.setFilled(true);
        this.redHealth.setColor(Color.RED);
        canvas.add(this.redHealth,this.x-15, this.y-20);
        this.greenHealth = new GRect(30, 5);
        this.greenHealth.setFilled(true);
        this.greenHealth.setColor(Color.GREEN);
        canvas.add(this.greenHealth ,this.x-15, this.y-20);
    }

    /**
     * method to remove enemy object from array list
     */
    public void removeIndex(ArrayList<Enemy> enemies, int index) {
        GPolygon shape = this.vehicle;
        GRect greenBar = this.greenHealth;
        GRect redBar = this.redHealth;
        canvas.remove(shape);
        canvas.remove(greenBar);
        canvas.remove(redBar);
        enemies.remove(index);
    }

    /**
     * method to copy ArrayList without keeping the relation to the first ArrayList
     */
    public ArrayList<Double> copyArrayList(ArrayList<Double> ar){
        ArrayList<Double> newList = new ArrayList<Double>();
        for(int i = 0; i<ar.size(); i++) {
            newList.add(ar.get(i));
        }
        return newList;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public double getHealing() {
        return healing;
    }

    public void setHealing(double healing) {
        this.healing = healing;
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

    public double getLastRotation() {
        return lastRotation;
    }

    public void setLastRotation(double lastRotation) {
        this.lastRotation = lastRotation;
    }

    public GPolygon getVehicle() {
        return vehicle;
    }

    public void setVehicle(GPolygon vehicle) {
        this.vehicle = vehicle;
    }

    public GRect getGreenHealth() {
        return greenHealth;
    }

    public void setGreenHealth(GRect greenHealth) {
        this.greenHealth = greenHealth;
    }

    public GRect getRedHealth() {
        return redHealth;
    }

    public void setRedHealth(GRect redHealth) {
        this.redHealth = redHealth;
    }

    public ArrayList<Double> getPathX() {
        return pathX;
    }

    public void setPathX(ArrayList<Double> pathX) {
        this.pathX = pathX;
    }

    public ArrayList<Double> getPathY() {
        return pathY;
    }

    public void setPathY(ArrayList<Double> pathY) {
        this.pathY = pathY;
    }

    public double[] getxCoordinates() {
        return xCoordinates;
    }

    public void setxCoordinates(double[] xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public double[] getyCoordinates() {
        return yCoordinates;
    }

    public void setyCoordinates(double[] yCoordinates) {
        this.yCoordinates = yCoordinates;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void substractHealth(double demage){
        this.health -= demage;
    }

    public void heal(double healing){
        this.health += healing;
    }
}
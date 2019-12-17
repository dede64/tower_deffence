package cz.dede.entities;

import acm.graphics.GPolygon;
import acm.graphics.GRect;
import acm.util.RandomGenerator;
import cz.dede.Main;
import cz.dede.resources.TDConstants;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Main.canvas;

/**
 * Class of Enemy object
 * */
public class Enemy implements TDConstants {
    private double speed;
    private double maxHealth;
    private double health;
    private String type;
    private int award;
    private int healing;
    private double x;
    private double y;
    private double lastRotation = 0;
    private GPolygon vehicle;
    private GRect greenHealth;
    private GRect redHealth;
    private ArrayList<Double> pathX;
    private ArrayList<Double> pathY;
    private double[] xCoordinates;
    private double[] yCoordinates;
    private String movement;
    private int wave;

    //constructor
    public Enemy(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int vawe_number) {
        this.wave = vawe_number;
        this.pathX = copyArrayList(pathX);
        this.pathY = copyArrayList(pathY);
        this.type = type;
        this.x = this.pathX.get(0);
        this.y = this.pathY.get(0);
        this.pathX.remove(0);
        this.pathY.remove(0);
        this.movement = "ground";
        makeHealthBar();
        if(this.type.equals("puncher")) {
            makePuncher();
        }
        if(this.type.equals("puncher_speeder")) {
            makePuncherSpeeder();
        }
        if(this.type.equals("puncher_healer")) {
            makePuncherHealer();
        }
        if(this.type.equals("flyer")) {
            makeFlyer();
        }
        if(this.type.equals("briger")) {
            makeBriger();
        }
    }

    /**
     * create puncher Enemy
     */
    public void makePuncher() {
        this.speed = PUNCHER_SPEED;
        this.type = "puncher";
        this.maxHealth = PUNCHER_HEALTH + PUNCHER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_AWARD;
        this.healing = PUNCHER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        this.vehicle = createVehicle(Color.BLUE, this.xCoordinates, this.yCoordinates);
    }

    /**
     * create puncher speeder Enemy
     */
    public void makePuncherSpeeder() {
        this.speed = PUNCHER_SPEEDER_SPEED;
        this.type = "puncher_speeder";
        this.maxHealth = PUNCHER_SPEEDER_HEALTH+ PUNCHER_SPEEDER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_SPEEDER_AWARD;
        this.healing = PUNCHER_SPEEDER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        Color color = new Color(20, 20, 160);
        this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
    }

    /**
     * create puncher healer Enemy
     */
    public void makePuncherHealer() {
        this.speed = PUNCHER_HEALER_SPEED;
        this.type = "puncher_healer";
        this.maxHealth = PUNCHER_HEALER_HEALTH + PUNCHER_HEALER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_HEALER_AWARD;
        this.healing = PUNCHER_HEALER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        Color color = new Color(20, 160, 20);
        this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
    }

    /**
     * create puncher briger Enemy
     */
    public void makeBriger() {
        this.speed = BRIGER_SPEED;
        this.type = "briger";
        this.maxHealth = BRIGER_HEALTH + BRIGER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = BRIGER_AWARD;
        this.healing = BRIGER_HEALING;
        this.xCoordinates = BRIGER_X;
        this.yCoordinates = BRIGER_Y;
        this.vehicle = createVehicle(Color.BLACK, this.xCoordinates, this.yCoordinates);
    }

    /**
     * create flyer Enemy
     */
    public void makeFlyer() {
        RandomGenerator rg = new RandomGenerator();
        this.speed = FLYER_SPEED;
        this.type = "flyer";
        this.maxHealth = FLYER_HEALTH;
        this.health = this.maxHealth;
        this.award = FLYER_AWARD;
        this.healing = FLYER_HEALING;
        this.xCoordinates = FLYER_X;
        this.yCoordinates = FLYER_Y;
        this.movement = "air";
        this.pathX = new ArrayList<Double>();
        double x = rg.nextDouble(100, canvas.getWidth()-SIDE_MENU_WIDTH-100);
        this.pathX.add(x); this.pathX.add(x);
        this.pathY = new ArrayList<Double>();
        this.pathY.add((canvas.getHeight()+20.0));	this.pathY.add(-10.0);
        this.x = this.pathX.get(0);
        this.y = this.pathY.get(0);
        this.pathX.remove(0);
        this.pathY.remove(0);
        this.vehicle = createVehicle(Color.BLUE, this.xCoordinates, this.yCoordinates);
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
    public void move(ArrayList<Enemy> enemies, Player player) {
        double goalX = this.pathX.get(0);
        double goalY = this.pathY.get(0);
        double distanceX = goalX-this.x;
        double distanceY = goalY-this.y;
        double distance = Main.countDistance(distanceX, distanceY);
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
            this.x += distanceX/distance*this.speed;
            this.y += distanceY/distance*this.speed;
            this.vehicle.setLocation(this.x, this.y);
            this.redHealth.setLocation(this.x-15, this.y-20);
            this.greenHealth.setLocation(this.x-15, this.y-20);
            double rotation = Main.getAngle(0, distanceX, 0, distanceY);
            if(distanceX/distance*this.speed>this.speed*0.9) {
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

    public int getHealing() {
        return healing;
    }

    public void setHealing(int healing) {
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
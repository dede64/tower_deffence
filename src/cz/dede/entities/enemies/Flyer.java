package cz.dede.entities.enemies;

import acm.util.RandomGenerator;
import cz.dede.entities.Enemy;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Game.canvas;

public class Flyer extends Enemy {
    public Flyer(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int waveNumber) {
        super(type, pathX, pathY, waveNumber);

        RandomGenerator rg = new RandomGenerator();
        this.speed = FLYER_SPEED;
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
}

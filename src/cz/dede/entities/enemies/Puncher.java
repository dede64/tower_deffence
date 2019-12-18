package cz.dede.entities.enemies;

import cz.dede.entities.Enemy;

import java.awt.*;
import java.util.ArrayList;

public class Puncher extends Enemy {

    public Puncher(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int waveNumber) {
        super(type, pathX, pathY, waveNumber);

        this.speed = PUNCHER_SPEED;
        this.maxHealth = PUNCHER_HEALTH + PUNCHER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_AWARD;
        this.healing = PUNCHER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        this.vehicle = createVehicle(Color.BLUE, this.xCoordinates, this.yCoordinates);
    }
}

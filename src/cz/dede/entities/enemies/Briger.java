package cz.dede.entities.enemies;

import cz.dede.entities.Enemy;

import java.awt.*;
import java.util.ArrayList;

public class Briger extends Enemy {
    public Briger(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int waveNumber) {
        super(type, pathX, pathY, waveNumber);

        this.speed = BRIGER_SPEED;
        this.maxHealth = BRIGER_HEALTH + BRIGER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = BRIGER_AWARD;
        this.healing = BRIGER_HEALING;
        this.xCoordinates = BRIGER_X;
        this.yCoordinates = BRIGER_Y;
        this.vehicle = createVehicle(Color.BLACK, this.xCoordinates, this.yCoordinates);
    }
}

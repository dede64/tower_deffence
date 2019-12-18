package cz.dede.entities.enemies;

import cz.dede.entities.Enemy;

import java.awt.*;
import java.util.ArrayList;

public class PuncherHealer extends Enemy {
    public PuncherHealer(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int waveNumber) {
        super(type, pathX, pathY, waveNumber);

        this.speed = PUNCHER_HEALER_SPEED;
        this.maxHealth = PUNCHER_HEALER_HEALTH + PUNCHER_HEALER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_HEALER_AWARD;
        this.healing = PUNCHER_HEALER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        Color color = new Color(20, 160, 20);
        this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
    }
}

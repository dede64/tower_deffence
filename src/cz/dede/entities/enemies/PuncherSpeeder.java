package cz.dede.entities.enemies;

import cz.dede.entities.Enemy;

import java.awt.*;
import java.util.ArrayList;

public class PuncherSpeeder extends Enemy {
    public PuncherSpeeder(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int waveNumber) {
        super(type, pathX, pathY, waveNumber);

        this.speed = PUNCHER_SPEEDER_SPEED;
        this.maxHealth = PUNCHER_SPEEDER_HEALTH+ PUNCHER_SPEEDER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
        this.health = this.maxHealth;
        this.award = PUNCHER_SPEEDER_AWARD;
        this.healing = PUNCHER_SPEEDER_HEALING;
        this.xCoordinates = PUNCHER_X;
        this.yCoordinates = PUNCHER_Y;
        Color color = new Color(20, 20, 160);
        this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
    }
}

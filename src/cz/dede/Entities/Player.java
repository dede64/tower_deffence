package cz.dede.Entities;

import cz.dede.resources.TDConstants;

/**
 * Class of Player object
 * */
public class Player implements TDConstants {
    public double money;
    public int lives;
    public int killed_enemies = 0;
    public boolean started = false;
    public int wave_number = 0;
    public boolean gameOverRendered = false;
    public String name;
    public int tick = TICK;
    public double moneyBonus= 1;

    //constructor
    public Player() {
        this.money = PLAYER_MONEY;
        this.lives = PLAYER_LIVES;
    }
}
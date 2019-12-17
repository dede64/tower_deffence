package cz.dede.entities;

import cz.dede.resources.TDConstants;

/**
 * Class of Player object
 * */
public class Player implements TDConstants {
    private double money;
    private int lives;
    private int killedEnemies = 0;
    private boolean started = false;
    private int waveNumber = 0;
    private boolean gameOverRendered = false;
    private String name;
    private int tick = TICK;
    private double moneyBonus= 1;

    //constructor
    public Player() {
        this.money = PLAYER_MONEY;
        this.lives = PLAYER_LIVES;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public void setKilledEnemies(int killedEnemies) {
        this.killedEnemies = killedEnemies;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public boolean isGameOverRendered() {
        return gameOverRendered;
    }

    public void setGameOverRendered(boolean gameOverRendered) {
        this.gameOverRendered = gameOverRendered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public double getMoneyBonus() {
        return moneyBonus;
    }

    public void setMoneyBonus(double moneyBonus) {
        this.moneyBonus = moneyBonus;
    }

    public boolean getStarted(){
        return this.started;
    }

    public boolean getGameOverRendered(){
        return this.gameOverRendered;
    }
}
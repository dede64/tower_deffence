package cz.dede.entities;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import cz.dede.resources.TDConstants;

import java.awt.*;
import java.util.ArrayList;

import static cz.dede.Main.canvas;

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
    private boolean pause = false;

    private GRect gameOverBackground;
    private GLabel gameOverLabel;
    private ArrayList<Button> buttons;

    //constructor
    public Player() {
        this.money = PLAYER_MONEY;
        this.lives = PLAYER_LIVES;
        this.buttons = new ArrayList<>();
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

    void changePause(){
        this.pause = !this.pause;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void showGameOver(){
        gameOverBackground = new GRect(canvas.getWidth(), canvas.getHeight());
        gameOverBackground.setFilled(true);
        gameOverBackground.setFillColor(new Color(0xdddddddd, true));
        gameOverBackground.setLocation(0, 0);
        canvas.add(gameOverBackground);

        gameOverLabel = new GLabel("!GAME OVER!");
        gameOverLabel.setFont("Impact-40");
        gameOverLabel.setLocation((canvas.getWidth())/2.0-gameOverLabel.getWidth()/2, canvas.getHeight()/2.0-gameOverLabel.getHeight()/2);
        canvas.add(gameOverLabel);

        Button continueInEndless = new Button("Continue in endless", canvas.getWidth()/2.0 - 110, canvas.getHeight()/2.0 + 50, 220, 30, Color.GREEN);
        Button restart = new Button("Restart", canvas.getWidth()/2.0- 110, canvas.getHeight()/2.0 + 100, 220, 30, Color.RED);
        buttons.add(continueInEndless);
        buttons.add(restart);

        continueInEndless.setButtonEventListener(Player::continueInEndless);
        restart.setButtonEventListener((Player::restart));
    }

    public void hideGameOver(){
        if(gameOverBackground != null){
            canvas.remove(gameOverBackground);
            canvas.remove(gameOverLabel);
            for(Button button : buttons){
                button.delete();
            }
            gameOverBackground = null;
            gameOverLabel = null;
            buttons = new ArrayList<>();
        }
    }

    public void restart(){
        lives = PLAYER_LIVES;
        hideGameOver();
        gameOverRendered = false;
        pause = false;
    }

    public void continueInEndless(){
        hideGameOver();
        pause = false;
    }

    public GRect getGameOverBackground() {
        return gameOverBackground;
    }

    public void setGameOverBackground(GRect gameOverBackground) {
        this.gameOverBackground = gameOverBackground;
    }

    public GLabel getGameOverLabel() {
        return gameOverLabel;
    }

    public void setGameOverLabel(GLabel gameOverLabel) {
        this.gameOverLabel = gameOverLabel;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }
}
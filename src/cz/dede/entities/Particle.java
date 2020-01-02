package cz.dede.entities;

import acm.graphics.GOval;

import java.awt.*;

import static cz.dede.Game.canvas;

public class Particle {

    private GOval particle;
    private double x;
    private double y;
    private double xVelocity;
    private double yVelocity;
    private double remainingTime;
    private double totalTime;
    private Color color;

    public Particle(double x, double y, double xVelocity, double yVelocity, Color color, double duration, int particleSize) {
        particle = new GOval(x, y, particleSize, particleSize);
        particle.setFilled(true);
        particle.setFillColor(color);
        canvas.add(particle);
        this.x = x;
        this.y = y;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.remainingTime = duration;
        this.totalTime = duration;
        this.color = color;
    }

    public void update(){
        particle.setLocation(x + xVelocity, y + yVelocity);
        x += xVelocity;
        y += yVelocity;
        Color newColor = new Color(color.getRed(),color.getGreen(), color.getBlue(), (int) (255* this.remainingTime/this.totalTime));
        particle.setFillColor(newColor);
        particle.setColor(newColor);
        remainingTime -= 1;

    }

    public void delete(){
        canvas.remove(particle);

    }

    public double getRemainingTime() {
        return remainingTime;
    }
}

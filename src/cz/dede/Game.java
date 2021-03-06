package cz.dede;

import acm.graphics.*;
import acm.program.*;
import acm.util.MediaTools;
import acm.util.RandomGenerator;
import cz.dede.entities.*;
import cz.dede.entities.Button;
import cz.dede.resources.TDConstants;
import javafx.util.Pair;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import static cz.dede.Main.*;
import static java.lang.Thread.sleep;

public class Game implements TDConstants { // extends GraphicsProgram

    //instance variables
    private SideMenuTurretDetail sideMenuTurretDetail;
    /** A random number generator **/
    private RandomGenerator rg = new RandomGenerator();

    //audio file
	AudioClip ShootClip = MediaTools.loadAudioClip("res/turret.au");

    public void run() {

        //initialization
        GImage background = new GImage("res/map.png", 0, 0);
        canvas.add(background);
        Player player = new Player();
        ArrayList<Turret> turrets = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Bullet> bullets = new ArrayList<>();
        ArrayList<Double> pathX = new ArrayList<>();
        ArrayList<Double> pathY = new ArrayList<>();
        ArrayList<Particle> particles = new ArrayList<>();
        createPath(pathX, pathY);
        GOval rangeIndicator = createIndicator();
        SideMenuShop sideMenuShop = new SideMenuShop();
        GLabel score = createLabel(5, 20, "20");
        ArrayList<Integer> lastTps = new ArrayList<>();
        lastTps.add(1);

        colorShop(sideMenuShop, player);

        ArrayList<ArrayList<String>> waves = new ArrayList<ArrayList<String>>();
        waves = setWaves(waves);
        double waveCounter = 0;

        long start;
        long timeElapsed;
        long cycleLength = 1;

        long lastRender = System.currentTimeMillis();
        double repaintAfter = 1000.0 / TARGET_FPS;

        //MAIN LOOP
        while(true) {
            start = System.currentTimeMillis();

            if(player.isRestartGame()){
                canvas.removeAll();
                return;
            }

            double timeDifferenceScaled = cycleLength * player.getSpeedBoost();

            if(!player.isPause()){
                moveEnemies(enemies, player, timeDifferenceScaled);
                moveBullets(bullets, timeDifferenceScaled);
                rotateCanons(turrets, enemies);
                checkCollisions(bullets);
                shoot(turrets, bullets, particles, timeDifferenceScaled);
                updateParticles(particles, timeDifferenceScaled);
                checkHealth(enemies, player, sideMenuShop);
                healEnemies(enemies, timeDifferenceScaled);
                moveHealthBars(enemies);
                waveCounter = addEnemies(enemies, waves, waveCounter, pathX, pathY, player, timeDifferenceScaled);
                checkPlayerLives(player);
            }

            moveRangeIndicator(rangeIndicator, turrets, sideMenuShop);
            showShopInfo(sideMenuShop);
            moveOnClick();
            placeTurret(turrets, player, sideMenuShop);
            hideWaveButton(sideMenuShop, enemies, player);
            processClicks(sideMenuShop, turrets, player);


            if(System.currentTimeMillis() - lastRender > repaintAfter){
                changeScoreLabel(player, score, lastTps, (int) (1000 / (System.currentTimeMillis() - lastRender)));
                canvas.repaint();
                lastRender = System.currentTimeMillis();
            }


            // Measuring time for compensating computation losses. //TODO move to function
            timeElapsed = System.currentTimeMillis() - start;
            long msDif = timeElapsed;
            if ((long) player.getTick() - msDif < 0) msDif = player.getTick();
            try {
                sleep((int)(player.getTick() - msDif));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cycleLength = System.currentTimeMillis() - start;
            lastTps.add((int) (1000/cycleLength));
            if(lastTps.size() > AVERAGE_TPS_FROM){
                lastTps.remove(0);
            }
        }
    }

    /**
     * method to move enemies
     */
    private void moveEnemies(ArrayList<Enemy> enemies, Player player, double elapsedTime) {
        for(int i = enemies.size() -1; i >= 0; i--){
            Enemy enemy = enemies.get(i);
            enemy.move(enemies, player, elapsedTime);
        }
    }

    /**
     * method to move bullets
     */
    private void moveBullets(ArrayList<Bullet> bullets, double elapsedTime) {
        for(int i = bullets.size()-1; i>=0; i--) {
            Bullet bullet = bullets.get(i);
            if(bullet.getEnemy()==null) {
                GPolygon shape = bullet.getBullet();
                canvas.remove(shape);
                bullets.remove(i);
            }
            else if(bullet.getX()<-20||bullet.getX()>canvas.getWidth()+20||bullet.getY()<-20||bullet.getY()>canvas.getHeight()+20) {
                GPolygon shape = bullet.getBullet();
                canvas.remove(shape);
                bullets.remove(i);
            }
            else{
                bullet.move(elapsedTime);
            }
        }
    }

    /**
     * method to update particles (calls update method on all of them
     * @param particles
     */
    private void updateParticles(ArrayList<Particle> particles, double elapsedTime){
        for(int i = particles.size() -1 ; i >= 0; i--){
            Particle particle = particles.get(i);
            particle.update(elapsedTime);
            if (particle.getRemainingTime() < 0){
                particle.delete();
                particles.remove(particle);
            }
        }
    }

    /**
     * method to rotate canons to aim at the first enemy in range
     * when turret is rocketer it aims at enemy with most health
     * also it choose target for canon
     */
    private void rotateCanons(ArrayList<Turret> turrets, ArrayList<Enemy> enemies) {
        for(Turret turret : turrets) {
            if(turret.getCurrentLoad() < turret.getReloadTime() && turret.getTarget() != null){
                double angle = getAngle(turret.getX(), turret.getY(), turret.getTarget().getX(), turret.getTarget().getY());
                turret.getCanon().rotate(-turret.getLastRotation());
                turret.setLastRotation(angle);
                turret.getCanon().rotate(angle);
            }
            else if(!turret.getType().equals("rocketer")) {
                boolean found = false;
                for(Enemy enemy: enemies){
                    if(turret.getTargetType().equals(enemy.getMovement()) || turret.getTargetType().equals("air/ground")){
                        double x_diff = enemy.getX() - turret.getX();
                        double y_diff = enemy.getY() - turret.getY();
                        double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
                        if(distance <= turret.getRange()){
                            found = true;
                            turret.setTarget(enemy);
                            double angle = getAngle(turret.getX(), turret.getY(), turret.getTarget().getX(), turret.getTarget().getY());
                            turret.getCanon().rotate(-turret.getLastRotation());
                            turret.setLastRotation(angle);
                            turret.getCanon().rotate(angle);
                            break;
                        }
                    }
                }
                if(!found){
                    turret.setTarget(null);
                    turret.getCanon().rotate(-turret.getLastRotation());
                    turret.setLastRotation(0);
                }
            }
            else{
                double health = 0;
                turret.setTarget(null);
                for (Enemy enemy : enemies) {
                    if (enemy.getHealth() > health && enemy.getMovement().equals(ROCKET_TARGET)) {
                        turret.setTarget(enemy);
                        health = enemy.getHealth();
                    }
                }
                if (turret.getTarget() == null) {
                    turret.getCanon().rotate(-turret.getLastRotation());
                    turret.setLastRotation(0);
                }
                else {
                    double angle = getAngle(turret.getX(), turret.getY(), turret.getTarget().getX(), turret.getTarget().getY());
                    turret.getCanon().rotate(-turret.getLastRotation());
                    turret.setLastRotation(angle);
                    turret.getCanon().rotate(angle);
                }
            }
        }
    }

    /**
     * method to calculate degree of rotation from given points
     */
    public static double getAngle(double x1, double y1, double x2, double y2) {
        float angle = (float) Math.toDegrees(Math.atan2(x1 - x2 ,y1 - y2));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

    /**
     * method to check collisions between bullets and enemies, if yes, remove bullet and remove some health
     */
    private void checkCollisions(ArrayList<Bullet> bullets) {
        for(int b = bullets.size()-1; b>=0; b--) {
            Bullet bullet = bullets.get(b);
            double x_diff = bullet.getX() - bullet.getEnemy().getX();
            double y_diff = bullet.getY() - bullet.getEnemy().getY();
            double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
            if(distance<=bullet.getDamageRadius()) {
                bullet.getEnemy().substractHealth(bullet.getDmg());
                GPolygon shape = bullet.getBullet();
                canvas.remove(shape);
                bullets.remove(b);
            }
        }
    }

    /**
     * method to shoot from turrets, when they are reloaded
     */
    private void shoot(ArrayList<Turret> turrets, ArrayList<Bullet> bullets, ArrayList<Particle> particles, double elapsedTime) {
        for (Turret turret : turrets) {
            if (turret.getCurrentLoad() < turret.getReloadTime()) {
                turret.addReload(elapsedTime);
            } else if (turret.getTarget() != null) {
                Pair<Double, Double> canonEnd = turret.getCanonEnd();
                bullets.add(new Bullet(turret.getTarget(), turret, canonEnd.getKey(), canonEnd.getValue()));
                createParticles(particles, new Color(0xFED766), 5, canonEnd.getKey(), canonEnd.getValue(),
                        getAngle(canonEnd.getKey(), canonEnd.getValue(), turret.getTarget().getX(), turret.getTarget().getY()), 0.36, 150); // TODO each turret should have particle settings
                turret.setCurrentLoad(0);
                turret.getCanon().sendToFront();
//				ShootClip.play(); // TODO just for fun, but its buggy
            }
        }
    }


    private void createParticles(ArrayList<Particle> particles, Color color, int count, double x, double y, double angle, double speed, double duration){
        for (int i = 0; i < count; i++){
            double angleDif = rg.nextDouble(-30, 30) + angle;
            if(angleDif < 0){
                angleDif += 360;
            }
            double ySpeed = -Math.cos(Math.toRadians(angleDif)) * speed;
            double xSpeed = -Math.sin(Math.toRadians(angleDif)) * speed;
            Particle particle = new Particle(x, y, xSpeed, ySpeed, color, duration, 4);
            particles.add(particle);
        }
    }

    /**
     * method to check if enemies health isn't below zero -> death
     */
    private void checkHealth(ArrayList<Enemy> enemies, Player player, SideMenuShop sideMenuShop) {
        for(int e = enemies.size()-1; e>=0; e--) {
            Enemy enemy = enemies.get(e);
            if(enemy.getHealth()<=0) {
                GPolygon shape = enemy.getVehicle();
                GRect greenBar = enemy.getGreenHealth();
                GRect redBar = enemy.getRedHealth();
                canvas.remove(shape);
                canvas.remove(greenBar);
                canvas.remove(redBar);
                player.setMoney(player.getMoney() + enemy.getAward() * player.getMoneyBonus());
                player.setKilledEnemies(player.getKilledEnemies() + 1);
                enemies.remove(e);
                colorShop(sideMenuShop, player);
            }
        }
    }

    /**
     * method to heal enemies with healing abilities
     */
    private void healEnemies(ArrayList<Enemy> enemies, double cycleLength) {
        for(Enemy enemy: enemies){
            if(enemy.getHealing() > 0 && enemy.getHealth() < enemy.getMaxHealth()){
                enemy.heal(enemy.getHealing() * cycleLength);
            }
        }
    }

    /**
     * method to change health bars of enemies according to their health
     */
    private void moveHealthBars(ArrayList<Enemy> enemies) {
        for(Enemy enemy: enemies) {
            enemy.getGreenHealth().setSize(enemy.getHealth()/enemy.getMaxHealth()*30, enemy.getGreenHealth().getHeight());
        }
    }

//    /**
//     * method to get coordinates of the mouse
//     */
//    public void mouseMoved(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//    }
//
//    /**
//     * method to get coordinates of the mouse
//     */
//    public void mouseDragged(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//    }
//
//    /**
//     * method to catch mouse clicked event
//     */
//    public void mousePressed(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//
//        lastClicked = canvas.getElementAt(new GPoint(e.getPoint()));
//
//    }

//    /**
//     * method to catch mouse released event
//     */
//    public void mouseReleased(MouseEvent e) {
//        mouseX = e.getX();
//        mouseY = e.getY();
//        if(onClick!=null) {
//            mouseRelease = true;
//        }
//    }

    /**
     * method to initialize range indicator
     */
    private GOval createIndicator() {
        GOval rangeIndicator = new GOval(0,0);
        canvas.add(rangeIndicator);
        return rangeIndicator;
    }

    /**
     * method to move range indicator if is turret under mouse also when mouse drag a shop turret
     */
    private void moveRangeIndicator(GOval ri, ArrayList<Turret> turrets, SideMenuShop menu) {
        boolean successHover = false;
        for (Turret turret : turrets) { //TODO use method getElementAt
            if (turret.getBase().contains(mouseX, mouseY) || turret.getCanon().contains(mouseX, mouseY)) {
                successHover = true;
                ri.setSize(turret.getRange() * 2, turret.getRange() * 2);
                ri.setLocation(turret.getX() - turret.getRange(), turret.getY() - turret.getRange());
            }
        }
        if (menu.getCancel().contains(mouseX, mouseY)&&onClick!=null) {
            canvas.remove(onClick.getBase());
            canvas.remove(onClick.getCanon());
            canvas.remove(menu.getCancel());
            onClick = null;
            ri.setSize(0, 0);
        }
        if (onClick != null) {
            ri.setSize(onClick.getRange()*2, onClick.getRange()*2);
            ri.setLocation(onClick.getX() - onClick.getRange(), onClick.getY() - onClick.getRange());
            successHover = true;
        }
        if (!successHover) {
            ri.setSize(0, 0);
        }
    }

    /**
     * method to show shop info label when hover on item in shop
     */
    private void showShopInfo(SideMenuShop menu) {
        if (onClick == null) {
            for(Turret turret : menu.getTurretShop()) {//TODO use method getElementAt
                if ((turret.getBase().contains(mouseX, mouseY)||turret.getCanon().contains(mouseX, mouseY))&& turret.getCanon().isVisible() ){
                    // Move GPolygon
                    menu.getShopInfoBox().setVisible(true);
                    menu.getShopInfoBox().setLocation(menu.getShopInfoBox().getX(), turret.getBase().getY());
                    // Change labels
                    GLabel label1 = menu.getShopInfoLabels().get(0);
                    label1.setLabel(turret.getCost() + "$  " + (int)turret.getDmg() + "DMG");
                    label1.setLocation(label1.getX(), turret.getBase().getY() -20);
                    GLabel label2 = menu.getShopInfoLabels().get(1);
                    label2.setLabel(turret.getReloadTime() / 1000 + "s  " + (int)turret.getRange() + "m " + (int)(turret.getBulletSpeed()*1000) + "m/s");
                    label2.setLocation(label2.getX(), turret.getBase().getY() +10);
                    GLabel label3 = menu.getShopInfoLabels().get(2);
                    label3.setLabel("anti " + turret.getTargetType());
                    label3.setLocation(label3.getX(), turret.getBase().getY() +40);
                    for(GLabel label: menu.getShopInfoLabels()) {
                        label.setVisible(true);
                    }
                    return;
                }
            }
        }
        menu.getShopInfoBox().setVisible(false);
        menu.getShopInfoBox().sendToFront();
        for(GLabel label: menu.getShopInfoLabels()) {
            label.setVisible(false);
            label.sendToFront();
        }
    }

    /**
     * method to create label on given coordinates
     */
    private GLabel createLabel(double x, double y, String size) {
        GLabel label = new GLabel("");
        String font = "Franklin Gothic Medium-" + size;
        label.setFont(font);
        label.setColor(Color.GRAY);
        canvas.add(label, x, y);
        return label;
    }

    /**
     * method to change label with player score and lives
     */
    private void changeScoreLabel(Player player, GLabel label, ArrayList<Integer> lastTps, int fps) {
        int sumOfAll = lastTps.stream().mapToInt(value -> value).sum();
        int averageTps = sumOfAll / lastTps.size();
        label.setLabel("Money: " + (int) player.getMoney() + "$   Health: " + player.getLives() + "   Wave: " + (player.getWaveNumber() +1) +
                "   Score: " + player.getKilledEnemies() + "  TPS: " + averageTps + "  FPS: " + fps);
        if(sideMenuTurretDetail != null){
            sideMenuTurretDetail.update(player);
        }
    }


    /**
     * method to move shop item which is currently chosen on the field
     */
    private void moveOnClick() {
        if(onClick!=null) {
            onClick.setLocation(mouseX, mouseY);
        }
    }

    /**
     * method to release turret on the play field
     */
    private void placeTurret(ArrayList<Turret> turrets, Player player, SideMenuShop menu) {
        double currentX = mouseX;
        double currentY = mouseY;
        if(onClick != null && mouseRelease && placeAvailable(currentX, currentY, turrets)) {
            turrets.add(Turret.makeTurret(onClick.getType(), currentX-currentX%40+20, currentY-currentY%40+20));
            for (Turret turret : turrets) turret.getCanon().sendToFront();
            mouseRelease = false;
            canvas.remove(onClick.getBase());
            canvas.remove(onClick.getCanon());
            player.setMoney(player.getMoney() - onClick.getCost());
            canvas.remove(menu.getCancel());
            onClick = null;
            checkTurretCombination(turrets, player);
            colorShop(menu, player);
        }
        else {
            mouseRelease = false;
        }
    }

    /**
     * method to color shop items by their availability
     */
    private void colorShop(SideMenuShop menu, Player player) {
        for(int i = 0; i < menu.getTurretShop().size(); i++) {
            Turret turret = menu.getTurretShop().get(i);
            if(turret.getCost() > player.getMoney()) {
                menu.getTurretNameLabels().get(i).setColor(Color.RED);
            }
            else {
                menu.getTurretNameLabels().get(i).setColor(Color.GREEN);
            }
        }
    }

    /**
     * method checks if the place on the map is available, checks other turrets and color under him
     */
    private boolean placeAvailable(double x, double y, ArrayList<Turret> turrets){
        boolean available = true;
        for (Turret turret : turrets) {
            if (turret.getBase().contains(x - x % 40 + 20, y - y % 40 + 20)) {
                available = false;
            }
        }
        if(available && x<1240) {
            GImage background = new GImage("res/map.png", 0, 0);
            int[][] pixels = background.getPixelArray();
            if(pixels[(int)y][(int)x]!=-7864299) {
                available = false;
            }
        }
        else {
            available = false;
        }
        return available;
    }

    /**
     * method to create array list of points of enemy path
     */
    private void createPath(ArrayList<Double> pathX, ArrayList<Double> pathY) {
        double[] x = {950, 950, 355, 355, 950, 950};
        double[] y = {800, 540, 540, 220, 220, -20};
        for(int i = 0; i< x.length; i++) {
            pathX.add(x[i]);
            pathY.add(y[i]);
        }
    }

    /**
     * method to calculate the distance of two points(vectors diagonal)
     */
    public static double countDistance(double x, double y) {
        return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
    }

    /**
     * method to return array list full of array lists of enemy waves.
     */
    private ArrayList<ArrayList<String>> setWaves(ArrayList<ArrayList<String>> waves){
        waves.add(getWave(FIRST_WAVE));
        waves.add(getWave(SECOND_WAVE));
        waves.add(getWave(THIRD_WAVE));
        waves.add(getWave(FORTH_WAVE));
        waves.add(getWave(FIFTH_WAVE));
        for(int i = 0; i<100; i++) {
            waves.add(getRandomWave());
        }
        return waves;
    }

    /**
     * method to add enemies from a wave to the screen
     * when wave is bigger than 10, there is a random chance, that enemy will mutate.
     */
    private double addEnemies(ArrayList<Enemy> enemies, ArrayList<ArrayList<String>> waves, double counter, ArrayList<Double> pathX, ArrayList<Double> pathY, Player player, double cycleLength) {
        counter += cycleLength;
        if(counter > WAVE_SPACING && waves.size()>0 && player.getStarted()) { // TODO maybe each enemy should have own spacing
            ArrayList<String> wave = waves.get(0);
            enemies.add(Enemy.createEnemy(wave.get(0), pathX, pathY, player.getWaveNumber()));
            double randomBoost = rg.nextDouble(0, WAVE_RANDOM_MUTATION);
            Enemy enemy = enemies.get(enemies.size()-1);
            if(randomBoost < 1 && player.getWaveNumber() > WAVE_RANDOM_MUTATION_FIRST_WAVE && enemy.getMovement().equals("ground")) {
                enemy.setMaxHealth(enemy.getMaxHealth() * 10);
                enemy.setHealth(enemy.getMaxHealth());
                enemy.setHealing(enemy.getHealing() * 10);
                enemy.setSpeed(1);
                Color color = new Color(60, 188, 212);
                enemy.getVehicle().setColor(color);
            }
            wave.remove(0);
            if(wave.size()<1) {
                player.setStarted(false);
                waves.remove(0);
                player.setWaveNumber(player.getWaveNumber() + 1);
            }
            counter = 0;
        }
        return counter;
    }

    /**
     * Checks if player lives aren't below zero, if they are -> game over
     */
    private void checkPlayerLives(Player player){
        if(player.getLives()<=0 && !player.getGameOverRendered() ) {
            player.showGameOver();
            player.setGameOverRendered(true);
            player.setPause(true);
        }
    }

    /**
     * method to convert list to array list, here used to convert list of wave enemies
     */
    private ArrayList<String> getWave(String[] wave_array){
        return new ArrayList<>(Arrays.asList(wave_array));
    }

    /**
     * method to generate random wave
     */
    private ArrayList<String> getRandomWave(){
        ArrayList<String> wave = new ArrayList<>();
        String[] enemyChoice = {"puncher", "puncher_healer", "puncher_speeder", "flyer", "briger"};
        for(int i = 0; i<10; i++) {
            int k = rg.nextInt(0, enemyChoice.length-1);
            wave.add(enemyChoice[k]);
        }
        return wave;
    }

    /**
     * method to hide start wave button if wave has been already started
     */
    private void hideWaveButton(SideMenuShop menu, ArrayList<Enemy> enemies, Player player) {
        if(enemies.size() > 0 && menu.getNextWaveButton().getBackground().isVisible()) { //TODO not check it by this GObject propertu, its broken, when button is hidden.
            menu.getNextWaveButton().getBackground().setVisible(false);
            menu.getNextWaveButton().getText().setVisible(false);
        }
        else if(enemies.size() == 0 && !menu.getNextWaveButton().getBackground().isVisible() && !player.getStarted()) {
            menu.getNextWaveButton().getBackground().setVisible(true);
            menu.getNextWaveButton().getText().setVisible(true);
            player.setSpeedBoost(1);
        }
        if(enemies.size() > 0 && !menu.getFasterWaveButton().isVisible()) { // TODO it should be controlled by start wave or killed last enemy of wave not by number of enemies
            menu.getFasterWaveButton().setVisible(true);
        }
        else if(enemies.size() == 0 && menu.getFasterWaveButton().isVisible() && !player.getStarted()) {
            menu.getFasterWaveButton().setVisible(false);
        }
    }

    /**
     * method to check if turrets are arranged in good layout to create bonus turret
     */
    private void checkTurretCombination(ArrayList<Turret> turrets, Player player) {
        for(int i = turrets.size()-1; i>=0; i--) {
            Turret turret = turrets.get(i);
            if(turret.getType().equals("destroyer")) {
                double x = turret.getBase().getX();
                double y = turret.getBase().getY();
                Turret tTop = null;
                Turret tCorner = null;
                Turret tRight = null;
                for (Turret checkTurret : turrets) {
                    if (checkTurret.getBase().contains(x, y - 40)) {
                        tTop = checkTurret;
                    }
                    if (checkTurret.getBase().contains(x + 40, y - 40)) {
                        tCorner = checkTurret;
                    }
                    if (checkTurret.getBase().contains(x + 40, y)) {
                        tRight = checkTurret;
                    }
                }
                if(tTop != null && tCorner != null & tRight != null) {
                    if(tTop.getType().equals("destroyer") && tCorner.getType().equals("destroyer") && tRight.getType().equals("destroyer")) {
                        double investedMoney = turret.getInvestedMoney() + tTop.getInvestedMoney() + tCorner.getInvestedMoney() + tRight.getInvestedMoney();
                        turret.delete();
                        tTop.delete();
                        tCorner.delete();
                        tRight.delete();
                        turrets.remove(turret);
                        turrets.remove(tTop);
                        turrets.remove(tCorner);
                        turrets.remove(tRight);
                        Turret bonus = Turret.makeTurret("bonus", x + 20, y - 20);
                        bonus.setInvestedMoney(investedMoney);
                        turrets.add(bonus);
                        for (Turret turret1 : turrets) {
                            turret1.getCanon().sendToFront();
                        }
                        player.setMoneyBonus(player.getMoneyBonus() + BONUS_MONEY);
                        return;
                    }
                }
            }
        }
    }


    public Object getObject(GObject object, ArrayList<Turret> turrets, SideMenuShop sideMenuShop, Player player){ // TODO it should handle all events which uses mouse click event
        for(Turret turret: turrets){
            if(turret.getCanon().equals(object) || turret.getBase().equals(object)){
                return turret;
            }
        }
        for(Turret turret : sideMenuShop.getTurretShop()){
            if(turret.getCanon().equals(object) || turret.getBase().equals(object)){
                return turret;
            }
        }
        for(Button button : sideMenuShop.getButtons()){
            if(button.getSprites().contains(object)){ // TODO do this also with other objects.
                return button;
            }
        }
        if(sideMenuTurretDetail != null){
            for(Button button : sideMenuTurretDetail.getButtons()){
                if(button.getSprites().contains(object)){
                    return button;
                }
            }
        }
        for(Button button: player.getButtons()){
            if(button.getSprites().contains(object)){
                return button;
            }
        }
        return null;

    }

    private void processClicks(SideMenuShop sideMenuShop, ArrayList<Turret> turrets, Player player){
        if(lastClicked != null){
            Object obj = getObject(lastClicked, turrets, sideMenuShop, player);
            lastClicked = null;

            if(obj instanceof  Button && sideMenuTurretDetail != null){
                if(sideMenuTurretDetail.getConfirm() == obj){
                    sideMenuTurretDetail.buy(player);
                    colorShop(sideMenuShop, player);
                    return;
                }
                else if(sideMenuTurretDetail.getSell() == obj){
                    Turret turret = sideMenuTurretDetail.getTurret();
                    if(turret.getType().equals("bonus")){
                        player.setMoneyBonus(player.getMoneyBonus() - BONUS_MONEY);
                    }
                    turrets.remove(turret);
                    player.setMoney(player.getMoney() + turret.getSellValue());
                    turret.delete();
                    sideMenuTurretDetail.delete();
                    sideMenuTurretDetail = null;
                    sideMenuShop.showMenu();
                    colorShop(sideMenuShop, player);
                    return;
                }
            }

            if (obj instanceof Turret && turrets.contains(obj)){
                sideMenuShop.hideMenu();

                if(sideMenuTurretDetail != null){
                    sideMenuTurretDetail.delete();
                    sideMenuTurretDetail = null;
                }
                sideMenuTurretDetail = new SideMenuTurretDetail((Turret) obj);
                return;
            }

            if(sideMenuTurretDetail != null){
                sideMenuTurretDetail.delete();
                sideMenuTurretDetail = null; // TODO make sure all GObjects are deleted before deleting the object, also on other places
            }
            sideMenuShop.showMenu();

            if(obj instanceof Turret && sideMenuShop.getTurretShop().contains(obj)){
                if(player.getMoney() >= ((Turret) obj).getCost()){
                    onClick = Turret.makeTurret(((Turret) obj).getType(), mouseX, mouseY);
                    canvas.add(sideMenuShop.getCancel());
                }
                return;
            }

            if(obj instanceof Button){
                Button button = (Button) obj;
                button.callback(player);
            }
        }
    }
}



package cz.dede;

import acm.graphics.*;
import acm.program.*;
import acm.util.MediaTools;
import acm.util.RandomGenerator;
import cz.dede.Entities.*;
import cz.dede.resources.TDConstants;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Main extends GraphicsProgram implements TDConstants {

    //instance variables
    private double mouseX;
    private double mouseY;
    private boolean mouseClick = false;
    private boolean mouseRelease = false;
    private Turret onClick = null;
    public static GCanvas canvas;

    /** A random number generator **/
    private RandomGenerator rg = new RandomGenerator();

    public static void main(String[] args) {
        new Main().start(args);
    }

    //audio file
	AudioClip ShootClip = MediaTools.loadAudioClip("res/turret.au");

    public void run() {
        canvas = getGCanvas();
        //initialization
        GImage background = new GImage("res/map.png", 0, 0);
        add(background);
        Player player = new Player();
        ArrayList<Turret> turrets = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Bullet> bullets = new ArrayList<>();
        ArrayList<Double> pathX = new ArrayList<>();
        ArrayList<Double> pathY = new ArrayList<>();
        createPath(pathX, pathY);
        GOval rangeIndicator = createIndicator();
        SideMenu sideMenu = new SideMenu();
        GLabel score = createLabel(5, 20, "20");
        addMouseListeners();
        colorShop(sideMenu, player);

        ArrayList<ArrayList<String>> waves = new ArrayList<ArrayList<String>>();
        waves = setWaves(waves);
        int waveCounter = 0;

        long start = 0;
        long timeElapsed = 1;
        long cycleLength = 1;

        //MAIN LOOP
        while(true) {
            start = System.nanoTime();

            moveEnemies(enemies, player);
            moveBullets(bullets);
            rotateCanons(turrets, enemies);
            checkCollisions(bullets);
            shoot(turrets, bullets);
            checkHealth(enemies, player, sideMenu);
            healEnemies(enemies);
            moveHealthBars(enemies);
            moveRangeIndicator(rangeIndicator, turrets, sideMenu);
            showShopInfo(sideMenu);
            changeScoreLabel(player, score, cycleLength);
            checkClick(sideMenu, player);
            moveOnClick();
            placeTurret(turrets, player, sideMenu);
            addEnemies(enemies, waves, waveCounter, pathX, pathY, player);
            checkPlayerLives(player);
            hideWaveButton(sideMenu, enemies, player);

            waveCounter += 1;


            // Measuring time for compensating computation losses.
            timeElapsed = System.nanoTime() - start;
            long nsDif = timeElapsed;
            if ((long) player.getTick() * 1000000 - nsDif < 0) nsDif = player.getTick() * 1000000;

            try {
                sleep((int)(player.getTick() * 1000000 - nsDif)/500000, (int)(player.getTick() * 1000000 - nsDif)%500000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cycleLength = System.nanoTime() - start;
        }
    }




    /**
     * method to move enemies
     */
    private void moveEnemies(ArrayList<Enemy> enemies, Player player) {
        for(int i = enemies.size() -1; i >= 0; i--){
            Enemy enemy = enemies.get(i);
            enemy.move(enemies, player);
        }
    }

    /**
     * method to move bullets
     */
    private void moveBullets(ArrayList<Bullet> bullets) {
        for(int i = bullets.size()-1; i>=0; i--) {
            Bullet bullet = bullets.get(i);
            if(bullet.getEnemy()==null) {
                GPolygon shape = bullet.getBullet();
                remove(shape);
                bullets.remove(i);
            }
            else if(bullet.getX()<-20||bullet.getX()>getWidth()+20||bullet.getY()<-20||bullet.getY()>getHeight()+20) {
                GPolygon shape = bullet.getBullet();
                remove(shape);
                bullets.remove(i);
            }
            else{
                bullet.move();
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
            if(!turret.type.equals("rocketer")) {
                boolean found = false;
                for(Enemy enemy: enemies){
                    if(turret.targetType.equals(enemy.movement) || turret.targetType.equals("air/ground")){
                        double x_diff = enemy.x - turret.x;
                        double y_diff = enemy.y - turret.y;
                        double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
                        if(distance <= turret.range){
                            found = true;
                            turret.target = enemy;
                            double angle = getAngle(turret.x, turret.y, turret.target.x, turret.target.y);
                            turret.canon.rotate(-turret.last_rotation);
                            turret.last_rotation = angle;
                            turret.canon.rotate(angle);
                            break;
                        }
                    }
                }
                if(!found){
                    turret.target = null;
                    turret.canon.rotate(-turret.last_rotation);
                    turret.last_rotation = 0;
                }
            }
            else{
                double health = 0;
                turret.target = null;
                for (Enemy enemy : enemies) {
                    if (enemy.health > health && enemy.movement.equals(ROCKET_TARGET)) {
                        turret.target = enemy;
                        health = enemy.health;
                    }
                }
                if (turret.target == null) {
                    turret.canon.rotate(-turret.last_rotation);
                    turret.last_rotation = 0;
                }
                else {
                    double angle = getAngle(turret.x, turret.y, turret.target.x, turret.target.y);
                    turret.canon.rotate(-turret.last_rotation);
                    turret.last_rotation = angle;
                    turret.canon.rotate(angle);
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
            double x_diff = bullet.getX() - bullet.getEnemy().x;
            double y_diff = bullet.getY() - bullet.getEnemy().y;
            double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
            if(distance<=bullet.getDemageRadius()) {
                bullet.getEnemy().health -= bullet.getDmg();
                GPolygon shape = bullet.getBullet();
                remove(shape);
                bullets.remove(b);
            }
        }
    }

    /**
     * method to shoot from turrets, when they are reloaded
     */
    private void shoot(ArrayList<Turret> turrets, ArrayList<Bullet> bullets) {
        for (Turret turret : turrets) {
            if (turret.currentLoad < turret.reloadTime) {
                turret.currentLoad += 1;
            } else if (turret.target != null) {
                bullets.add(new Bullet(turret.target, turret, turret.x, turret.y));
                turret.currentLoad = 0;
//				ShootClip.play(); // TODO just for fun, but its buggy
            }
        }
    }

    /**
     * method to check if enemies health isn't below zero -> death
     */
    private void checkHealth(ArrayList<Enemy> enemies, Player player, SideMenu sideMenu) {
        for(int e = enemies.size()-1; e>=0; e--) {
            Enemy enemy = enemies.get(e);
            if(enemy.health<=0) {
                GPolygon shape = enemy.vehicle;
                GRect greenBar = enemy.greenHealth;
                GRect redBar = enemy.redHealth;
                remove(shape);
                remove(greenBar);
                remove(redBar);
                player.setMoney(player.getMoney() + enemy.award * player.getMoneyBonus());
                player.setKilledEnemies(player.getKilledEnemies() + 1);
                enemies.remove(e);
                colorShop(sideMenu, player);
            }
        }
    }

    /**
     * method to heal enemies with healing abilities
     */
    private void healEnemies(ArrayList<Enemy> enemies) {
        for(Enemy enemy: enemies){
            if(enemy.healing > 0 && enemy.health < enemy.max_health){
                enemy.health += enemy.healing;
            }
        }
    }

    /**
     * method to change health bars of enemies according to their health
     */
    private void moveHealthBars(ArrayList<Enemy> enemies) {
        for(Enemy enemy: enemies) {
            enemy.greenHealth.setSize(enemy.health/enemy.max_health*30, enemy.greenHealth.getHeight());
        }
    }

    /**
     * method to get coordinates of the mouse
     */
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * method to get coordinates of the mouse
     */
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * method to catch mouse clicked event
     */
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseClick = true;
    }

    /**
     * method to catch mouse released event
     */
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if(onClick!=null) {
            mouseRelease = true;
        }
    }

    /**
     * method to initialize range indicator
     */
    private GOval createIndicator() {
        GOval rangeIndicator = new GOval(0,0);
        add(rangeIndicator);
        return rangeIndicator;
    }

    /**
     * method to move range indicator if is turret under mouse also when mouse drag a shop turret
     */
    private void moveRangeIndicator(GOval ri, ArrayList<Turret> turrets, SideMenu menu) {
        boolean successHover = false;
        for (Turret turret : turrets) {
            if (turret.base.contains(mouseX, mouseY) || turret.canon.contains(mouseX, mouseY)) {
                successHover = true;
                ri.setSize(turret.range * 2, turret.range * 2);
                ri.setLocation(turret.x - turret.range, turret.y - turret.range);
            }
        }
        if (menu.getCancel().contains(mouseX, mouseY)&&onClick!=null) {
            remove(onClick.base);
            remove(onClick.canon);
            remove(menu.getCancel());
            onClick = null;
            ri.setSize(0, 0);
        }
        if (onClick != null) {
            ri.setSize(onClick.range*2, onClick.range*2);
            ri.setLocation(onClick.x-onClick.range, onClick.y-onClick.range);
            successHover = true;
        }
        if (!successHover) {
            ri.setSize(0, 0);
        }
    }

    /**
     * method to show shop info label when hover on item in shop
     */
    private void showShopInfo(SideMenu menu) {
        if (onClick == null) {
            for(Turret turret : menu.getTurretShop()) {
                if (turret.base.contains(mouseX, mouseY)||turret.canon.contains(mouseX, mouseY)) {
                    // Move GPolygon
                    menu.getShopInfoBox().setVisible(true);
                    menu.getShopInfoBox().setLocation(menu.getShopInfoBox().getX(), turret.base.getY());
                    // Change labels
                    GLabel label1 = menu.getShopInfoLabels().get(0);
                    label1.setLabel(turret.cost + "$  " + (int)turret.dmg + "DMG");
                    label1.setLocation(label1.getX(), turret.base.getY() -20);
                    GLabel label2 = menu.getShopInfoLabels().get(1);
                    label2.setLabel(TICK / 1000.0 * turret.reloadTime + "s  " + (int)turret.range + "m " + (int)turret.bulletSpeed + "m/s");
                    label2.setLocation(label2.getX(), turret.base.getY() +10);
                    GLabel label3 = menu.getShopInfoLabels().get(2);
                    label3.setLabel("anti " + turret.targetType);
                    label3.setLocation(label3.getX(), turret.base.getY() +40);
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
        add(label, x, y);
        return label;
    }

    /**
     * method to change label with player score and lives
     */
    private void changeScoreLabel(Player player, GLabel label, long cycleLength) {
        label.setLabel("Money: " + (int) player.getMoney() + "$   Health: " + player.getLives() + "   Wave: " + (player.getWaveNumber() +1) + "   Score: " + player.getKilledEnemies() + " FPS: " + 1000000000/cycleLength);
    }

    /**
     * method to grab item from shop, when clicked on it
     * method to check start button, to start new vawe, if the last is killed
     */
    private void checkClick(SideMenu menu, Player player) {
        if(!player.getStarted() && menu.getButton().contains(mouseX, mouseY) && mouseClick && menu.getButton().isVisible()) {
            player.setStarted(true);
            mouseClick = false;
        }
        else if(menu.getFasterButton().contains(mouseX, mouseY) && mouseClick && menu.getFasterButton().isVisible()) {
            player.setTick(2);
            mouseClick = false;
        }
        else if(mouseClick && onClick == null) {
            for(int i = 0; i< menu.getTurretShop().size(); i++) {
                if(menu.getTurretShop().get(i).base.contains(mouseX, mouseY)||menu.getTurretShop().get(i).canon.contains(mouseX, mouseY)) {
                    if(player.getMoney()>=menu.getTurretShop().get(i).cost) {
                        onClick = new Turret(menu.getTurretShop().get(i).type, mouseX, mouseY);
                        add(menu.getCancel());
                    }
                }
            }
            mouseClick = false;
        }
        else {
            mouseClick = false;
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
    private void placeTurret(ArrayList<Turret> turrets, Player player, SideMenu menu) {
        double currentX = mouseX;
        double currentY = mouseY;
        if(onClick != null && mouseRelease && placeAvailable(currentX, currentY, turrets)) {
            turrets.add(new Turret(onClick.type, currentX-currentX%40+20, currentY-currentY%40+20));
            for (Turret turret : turrets) turret.canon.sendToFront();
            mouseRelease = false;
            remove(onClick.base);
            remove(onClick.canon);
            player.setMoney(player.getMoney() - onClick.cost);
            remove(menu.getCancel());
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
    private void colorShop(SideMenu menu, Player player) {
        for(int i = 0; i < menu.getTurretShop().size(); i++) {
            Turret turret = menu.getTurretShop().get(i);
            if(turret.cost>player.getMoney()) {
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
            if (turret.base.contains(x - x % 40 + 20, y - y % 40 + 20)) {
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
        for(int i = 0; i<50; i++) {
            waves.add(getRandomWave());
        }
        return waves;
    }

    /**
     * method to add enemies from a wave to the screen
     * when wave is bigger than 10, there is a random chance, that enemy will mutate.
     */
    private void addEnemies(ArrayList<Enemy> enemies, ArrayList<ArrayList<String>> waves, int counter, ArrayList<Double> pathX, ArrayList<Double> pathY, Player player) {
        if(counter%WAVE_SPACING==0 && waves.size()>0 && player.getStarted()) {
            ArrayList<String> wave = waves.get(0);
            enemies.add(new Enemy(wave.get(0), pathX, pathY, player.getWaveNumber()));
            double randomBoost = rg.nextDouble(0, WAVE_RANDOM_MUTATION);
            Enemy enemy = enemies.get(enemies.size()-1);
            if(randomBoost < 1 && player.getWaveNumber() > WAVE_RANDOM_MUTATION_FIRST_WAVE && enemy.movement.equals("ground")) {
                enemy.max_health = enemy.max_health * 10;
                enemy.health = enemy.max_health;
                enemy.healing *= 10;
                enemy.speed = 1;
                Color color = new Color(60, 188, 212);
                enemy.vehicle.setColor(color);
            }
            wave.remove(0);
            if(wave.size()<1) {
                player.setStarted(false);
                waves.remove(0);
                player.setWaveNumber(player.getWaveNumber() + 1);
            }
        }
    }

    /**
     * Checks if player lives aren't below zero, if they are -> game over
     */
    private void checkPlayerLives(Player player){
        if(player.getLives()<=0 && !player.getGameOverRendered()) {
            GLabel gameOver = new GLabel("!GAME OVER!");
            gameOver.setFont("Impact-40");
            gameOver.setLocation((getWidth()-SIDE_MENU_WIDTH)/2.0-gameOver.getWidth()/2, getHeight()/2.0-gameOver.getHeight()/2);
            add(gameOver);
            player.setGameOverRendered(true);
//			pause(1000000000);
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
    private void hideWaveButton(SideMenu menu, ArrayList<Enemy> enemies, Player player) {
        if(enemies.size() > 0 && menu.getButton().isVisible()) {
            menu.getButton().setVisible(false);
            menu.getNextWaveLabel().setVisible(false);
        }
        else if(enemies.size() == 0 && !menu.getButton().isVisible() && !player.getStarted()) {
            menu.getButton().setVisible(true);
            menu.getNextWaveLabel().setVisible(true);
            player.setTick(TICK);
        }
        if(enemies.size() > 0 && !menu.getFasterButton().isVisible()) {
            menu.getFasterButton().setVisible(true);
            menu.getFasterButtonLabel().setVisible(true);
        }
        else if(enemies.size() == 0 && menu.getFasterButton().isVisible() && !player.getStarted()) {
            menu.getFasterButton().setVisible(false);
            menu.getFasterButtonLabel().setVisible(false);
        }
    }

    /**
     * method to check if turrets are arranged in good layout to create bonus turret
     */
    private void checkTurretCombination(ArrayList<Turret> turrets, Player player) {
        for(int i = turrets.size()-1; i>=0; i--) {
            Turret turret = turrets.get(i);
            if(turret.type.equals("destroyer")) {
                double x = turret.base.getX();
                double y = turret.base.getY();
                Turret tTop = null;
                Turret tCorner = null;
                Turret tRight = null;
                for (Turret checkTurret : turrets) {
                    if (checkTurret.base.contains(x, y - 40)) {
                        tTop = checkTurret;
                    }
                    if (checkTurret.base.contains(x + 40, y - 40)) {
                        tCorner = checkTurret;
                    }
                    if (checkTurret.base.contains(x + 40, y)) {
                        tRight = checkTurret;
                    }
                }
                if(tTop != null && tCorner != null & tRight != null) {
                    if(tTop.type.equals("destroyer") && tCorner.type.equals("destroyer") && tRight.type.equals("destroyer")) {
                        deleteTurret(turret);
                        deleteTurret(tTop);
                        deleteTurret(tCorner);
                        deleteTurret(tRight);
                        turrets.remove(turret);
                        turrets.remove(tTop);
                        turrets.remove(tCorner);
                        turrets.remove(tRight);
                        Turret bonus = new Turret("bonus", x + 20, y - 20);
                        turrets.add(bonus);
                        for (Turret turret1 : turrets) {
                            turret1.canon.sendToFront();
                        }
                        player.setMoneyBonus(player.getMoneyBonus() + BONUS_MONEY);
                        return;
                    }
                }
            }
        }
    }

    /**
     * method to delete turret shape from screen
     */
    private void deleteTurret(Turret turret) {
        remove(turret.base);
        remove(turret.canon);
    }
}



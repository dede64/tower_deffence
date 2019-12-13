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

public class Main extends GraphicsProgram implements TDConstants {

    //instance variables
    public double mouseX;
    public double mouseY;
    public boolean mouseClick = false;
    public boolean mouseRelease = false;
    public Turret onClick = null;
    public static GCanvas canvas;

    /** A random number generator **/
    public RandomGenerator rg = new RandomGenerator();

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
        ArrayList<Turret> turrets = new ArrayList<Turret>();
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        ArrayList<Double> pathX = new ArrayList<Double>();
        ArrayList<Double> pathY = new ArrayList<Double>();
        createPath(pathX, pathY);
        GOval rangeIndicator = createIndicator();
        SideMenu sideMenu = new SideMenu();
        GLabel score = createLabel(5, 20, "20");
        addMouseListeners();

        ArrayList<ArrayList<String>> waves = new ArrayList<ArrayList<String>>();
        waves = setWaves(waves);
        int waveCounter = 0;

        //MAIN LOOP
        while(true) { // TODO some methods can be called only when they are needed for example colorShop(), changeScoreLabel...
//            System.out.println("Turrets: " + turrets.size() + " Enemies: " + enemies.size() + " Bullets: " + bullets.size());
            moveEnemies(enemies, player);
            moveBullets(bullets);
            rotateCanons(turrets, enemies);
            checkCollisions(bullets);
            shoot(turrets, bullets);
            checkHealth(enemies, player);
            healEnemies(enemies);
            moveHealthBars(enemies);
            moveRangeIndicator(rangeIndicator, turrets, sideMenu);
            showShopInfo(sideMenu);
            changeScoreLabel(player, score);
            colorShop(sideMenu, player);
            checkClick(sideMenu, player);
            moveOnClick();
            placeTurret(turrets, player, sideMenu);
            addEnemies(enemies, waves, waveCounter, pathX, pathY, player);
            checkPlayerLives(player);
            hideWaveButton(sideMenu, enemies, player);

            waveCounter += 1;
            pause(player.tick);
        }
    }




    /**
     * method to move enemies
     */
    public void moveEnemies(ArrayList<Enemy> enemies, Player player) {
        for(int i = 0; i<enemies.size(); i++) {
            enemies.get(i).move(enemies, i, player);
        }
    }

    /**
     * method to move bullets
     */
    public void moveBullets(ArrayList<Bullet> bullets) {
        for(int i = bullets.size()-1; i>=0; i--) {
            Bullet bullet = bullets.get(i);
            if(bullet.enemy==null) {
                GPolygon shape = bullet.bullet;
                remove(shape);
                bullets.remove(i);
            }
            else if(bullet.x<-20||bullet.x>getWidth()+20||bullet.y<-20||bullet.y>getHeight()+20) {
                GPolygon shape = bullet.bullet;
                remove(shape);
                bullets.remove(i);
            }
            else{
                bullet.move();
            }
        }
    }

    /**
     * method to rotate canons to aim at the nearest enemies
     * when turret is rocketer it aims at enemy with most health
     * also it choose target for canon
     */
    public void rotateCanons(ArrayList<Turret> turrets, ArrayList<Enemy> enemies) {
        for(int t = 0; t< turrets.size(); t++) {
            Turret turret = turrets.get(t);
            double min = 100000;
            if(turret.type != "rocketer") {// TODO make it shorter and less complex ;)
                for (int e = 0; e < enemies.size(); e++) {
                    Enemy enemy = enemies.get(e);
                    double x_diff = enemy.x - turret.x;
                    double y_diff = enemy.y - turret.y;
                    double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
                    if(distance<min && turret.targetType==enemy.movement) {
                        min = distance;
                        turret.target = enemy;
                    }
                    else if(distance<min && turret.targetType=="air/ground") {
                        min = distance;
                        turret.target = enemy;
                    }
                }
                if(min<=turret.range){
                    double angle = getAngle(turret.x, turret.y, turret.target.x, turret.target.y);
                    turret.canon.rotate(-turret.last_rotation);
                    turret.last_rotation = angle;
                    turret.canon.rotate(angle);
                }
                else {
                    turret.target = null;
                    turret.canon.rotate(-turret.last_rotation);
                    turret.last_rotation = 0;
                }
            }
            else if(turret.type == "rocketer") {
                double health = 0;
                turret.target = null;
                for (int e = 0; e < enemies.size(); e++) {
                    Enemy enemy = enemies.get(e);
                    if(enemy.health>health && enemy.movement == ROCKET_TARGET) {
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
    public void checkCollisions(ArrayList<Bullet> bullets) {
        for(int b = bullets.size()-1; b>=0; b--) {
            Bullet bullet = bullets.get(b);
            double x_diff = bullet.x - bullet.enemy.x;
            double y_diff = bullet.y - bullet.enemy.y;
            double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
            if(distance<=bullet.demage_radius) {
                bullet.enemy.health -= bullet.dmg;
                GPolygon shape = bullet.bullet;
                remove(shape);
                bullets.remove(b);
            }
        }
    }

    /**
     * method to shoot from turrets, when they are reloaded
     */
    public void shoot(ArrayList<Turret> turrets, ArrayList<Bullet> bullets) {
        for(int i = 0; i < turrets.size(); i++) {
            Turret turret = turrets.get(i);
            if(turret.currentLoad<turret.reloadTime) {
                turret.currentLoad += 1;
            }
            else if(turret.target!=null){
                bullets.add(new Bullet(turret.target, turret, turret.x, turret.y));
                turret.currentLoad = 0;
//				ShootClip.play(); // TODO just for fun, but its buggy
            }
        }
    }

    /**
     * method to check if enemies health isn't below zero -> death
     */
    public void checkHealth(ArrayList<Enemy> enemies, Player player) {
        for(int e = enemies.size()-1; e>=0; e--) {
            Enemy enemy = enemies.get(e);
            if(enemy.health<=0) {
                GPolygon shape = enemy.vehicle;
                GRect greenBar = enemy.greenHealth;
                GRect redBar = enemy.redHealth;
                remove(shape);
                remove(greenBar);
                remove(redBar);
                player.money += enemy.award * player.moneyBonus;
                player.killed_enemies += 1;
                enemies.remove(e);
            }
        }
    }

    /**
     * method to heal enemies with healing abilities
     */
    public void healEnemies(ArrayList<Enemy> enemies) {
        for(int e = enemies.size()-1; e>=0; e--) {
            Enemy enemy = enemies.get(e);
            if(enemy.health<enemy.max_health) {
                enemy.health += enemy.healing;
            }
        }
    }

    /**
     * method to change health bars of enemies according to their health
     */
    public void moveHealthBars(ArrayList<Enemy> enemies) {
        for(int e = enemies.size()-1; e>=0; e--) {
            Enemy enemy = enemies.get(e);
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
    public GOval createIndicator() {
        GOval rangeIndicator = new GOval(0,0);
        add(rangeIndicator);
        return rangeIndicator;
    }

    /**
     * method to move range indicator if is turret under mouse also when mouse drag a shop turret
     */
    public void moveRangeIndicator(GOval ri, ArrayList<Turret> turrets, SideMenu menu) {
        boolean successHover = false;
        for(int i = 0; i<turrets.size(); i++) {
            Turret turret = turrets.get(i);
            if (turret.base.contains(mouseX, mouseY)||turret.canon.contains(mouseX, mouseY)) {
                successHover = true;
                ri.setSize(turret.range*2, turret.range*2);
                ri.setLocation(turret.x-turret.range, turret.y-turret.range);
            }
        }
        if (menu.cancel.contains(mouseX, mouseY)&&onClick!=null) {
            remove(onClick.base);
            remove(onClick.canon);
            remove(menu.cancel);
            onClick = null;
            ri.setSize(0, 0);
        }
        if (onClick != null) {
            ri.setSize(onClick.range*2, onClick.range*2);
            ri.setLocation(onClick.x-onClick.range, onClick.y-onClick.range);
            successHover = true;
        }
        if (successHover==false) {
            ri.setSize(0, 0);
        }
    }

    /**
     * method to show shop info label when hover on item in shop
     */
    public void showShopInfo(SideMenu menu) {
        if (onClick == null) {
            for(int i = 0; i<menu.turretShop.size(); i++) {
                Turret turret = menu.turretShop.get(i);
                if (turret.base.contains(mouseX, mouseY)||turret.canon.contains(mouseX, mouseY)) {
                    // Move GPolygon
                    menu.shopInfoBox.setVisible(true);
                    menu.shopInfoBox.setLocation(menu.shopInfoBox.getX(), turret.base.getY());
                    // Change labels
                    GLabel label1 = menu.shopInfoLabels.get(0);
                    label1.setLabel(turret.cost + "$  " + (int)turret.dmg + "DMG");
                    label1.setLocation(label1.getX(), turret.base.getY() -20);
                    GLabel label2 = menu.shopInfoLabels.get(1);
                    label2.setLabel(TICK / 1000.0 * turret.reloadTime + "s  " + (int)turret.range + "m " + (int)turret.bulletSpeed + "m/s");
                    label2.setLocation(label2.getX(), turret.base.getY() +10);
                    GLabel label3 = menu.shopInfoLabels.get(2);
                    label3.setLabel("anti " + turret.targetType);
                    label3.setLocation(label3.getX(), turret.base.getY() +40);
                    for(int k = 0; k < menu.shopInfoLabels.size(); k++) {
                        menu.shopInfoLabels.get(k).setVisible(true);
                    }
                    return;
                }
            }
        }
        menu.shopInfoBox.setVisible(false);
        menu.shopInfoBox.sendToFront();
        for(int i = 0; i < menu.shopInfoLabels.size(); i++) {
            menu.shopInfoLabels.get(i).setVisible(false);
            menu.shopInfoLabels.get(i).sendToFront();
        }
    }

    /**
     * method to create label on given coordinates
     */
    public GLabel createLabel(double x, double y, String size) {
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
    public void changeScoreLabel(Player player, GLabel label) {
        label.setLabel("Money: " + player.money + "$   Health: " + player.lives + "   Wave: " + (player.wave_number +1) + "   Score: " + player.killed_enemies);
    }

    /**
     * method to grab item from shop, when clicked on it
     * method to check start button, to start new vawe, if the last is killed
     */
    public void checkClick(SideMenu menu, Player player) {
        if(player.started==false && menu.button.contains(mouseX, mouseY) && mouseClick == true && menu.button.isVisible() == true) {
            player.started=true;
            mouseClick = false;
        }
        else if(menu.fasterButton.contains(mouseX, mouseY) && mouseClick == true && menu.fasterButton.isVisible() == true) {
            player.tick = 2;
            mouseClick = false;
        }
        else if(mouseClick == true && onClick == null) {
            for(int i = 0; i< menu.turretShop.size(); i++) {
                if(menu.turretShop.get(i).base.contains(mouseX, mouseY)||menu.turretShop.get(i).canon.contains(mouseX, mouseY)) {
                    if(player.money>=menu.turretShop.get(i).cost) {
                        onClick = new Turret(menu.turretShop.get(i).type, mouseX, mouseY);
                        add(menu.cancel);
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
    public void moveOnClick() {
        if(onClick!=null) {
            onClick.setLocation(mouseX, mouseY);
        }
    }

    /**
     * method to release turret on the play field
     */
    public void placeTurret(ArrayList<Turret> turrets, Player player, SideMenu menu) {
        if(onClick != null && mouseRelease == true && placeAvailable(mouseX, mouseY, turrets)==true) {
            turrets.add(new Turret(onClick.type, mouseX-mouseX%40+20, mouseY-mouseY%40+20));
            for(int i = 0; i<turrets.size(); i++) {
                turrets.get(i).canon.sendToFront();
            }
            mouseRelease = false;
            remove(onClick.base);
            remove(onClick.canon);
            player.money-=onClick.cost;
            remove(menu.cancel);
            onClick = null;
            checkTurretCombination(turrets, player);
        }
        else {
            mouseRelease = false;
        }
    }

    /**
     * method to color shop items by their availability
     */
    public void colorShop(SideMenu menu, Player player) {
        for(int i = 0; i<menu.turretShop.size(); i++) {
            Turret turret = menu.turretShop.get(i);
            if(turret.cost>player.money) {
                menu.turretNameLabels.get(i).setColor(Color.RED);
            }
            else {
                menu.turretNameLabels.get(i).setColor(Color.GREEN);
            }
        }
    }

    /**
     * method checks if the place on the map is available, checks other turrets and color under him
     */
    public boolean placeAvailable(double x, double y, ArrayList<Turret> turrets){
        boolean available = true;
        for(int i = 0; i<turrets.size(); i++) {
            if (turrets.get(i).base.contains(x-x%40+20, y-y%40+20)){
                available = false;
            }
        }
        if(available == true&&x<1240) {
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
    public void createPath(ArrayList<Double> pathX, ArrayList<Double> pathY) {
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
        double diagonal = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
        return diagonal;
    }

    /**
     * method to return array list full of array lists of enemy waves.
     */
    public ArrayList<ArrayList<String>> setWaves(ArrayList<ArrayList<String>> waves){
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
    public void addEnemies(ArrayList<Enemy> enemies, ArrayList<ArrayList<String>> waves, int counter, ArrayList<Double> pathX, ArrayList<Double> pathY, Player player) {
        if(counter%WAVE_SPACING==0 && waves.size()>0 && player.started == true) {
            ArrayList<String> wave = waves.get(0);
            enemies.add(new Enemy(wave.get(0), pathX, pathY, player.wave_number));
            double randomBoost = rg.nextDouble(0, WAVE_RANDOM_MUTATION);
            Enemy enemy = enemies.get(enemies.size()-1);
            if(randomBoost < 1 && player.wave_number > WAVE_RANDOM_MUTATION_FIRST_WAVE && enemy.movement == "ground") {
                enemy.max_health = enemy.max_health * 10;
                enemy.health = enemy.max_health;
                enemy.healing *= 10;
                enemy.speed = 1;
                Color color = new Color(60, 188, 212);
                enemy.vehicle.setColor(color);
            }
            wave.remove(0);
            if(wave.size()<1) {
                player.started = false;
                waves.remove(0);
                player.wave_number+=1;
            }
        }
    }

    /**
     * Checks if player lives aren't below zero, if they are -> game over
     */
    public void checkPlayerLives(Player player){
        if(player.lives<=0 && player.gameOverRendered==false) {
            GLabel gameOver = new GLabel("!GAME OVER!");
            gameOver.setFont("Impact-40");
            gameOver.setLocation((getWidth()-SIDE_MENU_WIDTH)/2-gameOver.getWidth()/2, getHeight()/2-gameOver.getHeight()/2);
            add(gameOver);
            player.gameOverRendered=true;
//			pause(1000000000);
        }
    }

    /**
     * method to convert list to array list, here used to convert list of wave enemies
     */
    public ArrayList<String> getWave(String[] wave_array){
        ArrayList<String> wave = new ArrayList<>();
        for(int i = 0; i<wave_array.length; i++) {
            wave.add(wave_array[i]);
        }
        return wave;
    }

    /**
     * method to generate random wave
     */
    public ArrayList<String> getRandomWave(){
        ArrayList<String> wave = new ArrayList<String>();
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
    public void hideWaveButton(SideMenu menu, ArrayList<Enemy> enemies, Player player) {
        if(enemies.size() > 0 && menu.button.isVisible()) {
            menu.button.setVisible(false);
            menu.nextWaveLabel.setVisible(false);
        }
        else if(enemies.size() == 0 && !menu.button.isVisible() && !player.started) {
            menu.button.setVisible(true);
            menu.nextWaveLabel.setVisible(true);
            player.tick = TICK;
        }
        if(enemies.size() > 0 && !menu.fasterButton.isVisible()) {
            menu.fasterButton.setVisible(true);
            menu.fasterButtonLabel.setVisible(true);
        }
        else if(enemies.size() == 0 && menu.fasterButton.isVisible() && !player.started) {
            menu.fasterButton.setVisible(false);
            menu.fasterButtonLabel.setVisible(false);
        }
    }

    /**
     * method to check if turrets are arranged in good layout to create bonus turret
     */
    public void checkTurretCombination(ArrayList<Turret> turrets, Player player) {
        for(int i = turrets.size()-1; i>=0; i--) {
            Turret turret = turrets.get(i);
            if(turret.type == "destroyer") {
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
                        for(int k = 0; k<turrets.size(); k++) {
                            turrets.get(k).canon.sendToFront();
                        }
                        player.moneyBonus += BONUS_MONEY;
                        return;
                    }
                }
            }
        }
    }

    /**
     * method to delete turret shape from screen
     */
    public void deleteTurret(Turret turret) {
        remove(turret.base);
        remove(turret.canon);
    }
}



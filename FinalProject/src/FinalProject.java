import acm.graphics.*;
import acm.program.*;
import acm.util.MediaTools;
import acm.util.RandomGenerator;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.axes.WalkerFactory;


public class FinalProject extends GraphicsProgram implements FinalProjectConstants{

	//instance variables
	private double mouseX;
	private double mouseY;
	private boolean mouseClick = false;
	private boolean mouseRelease = false;
	private Turret onClick = null;
	
	/** A random number generator **/
	private RandomGenerator rg = new RandomGenerator();
	
	public static void main(String[] args) {
		new FinalProject().start(args);
		}
	
	//audio file
//	AudioClip ShootClip = MediaTools.loadAudioClip("res/turret.au");	
	
	public void run() {
		//initialization
		GImage background = new GImage("map.png", 0, 0);
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
		while(true) {
			moveEnemies(enemies, player);
			moveBullets(bullets);
			rotateCanons(turrets, enemies);
			checkCollisions(bullets);
			shoot(turrets, bullets);
			checkHealth(enemies, player);
			healEnemies(enemies);
			moveHealthBars(enemies);
			moveRangeIndicator(rangeIndicator, turrets, sideMenu);
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
	 * Class of Turret object
	 * */
	private class Turret{
		String type;
		String targetType;
		double range;
		double dmg;
		int reloadTime;
		int currentLoad;
		double bulletSpeed;
		double x;
		double y;
		double demage_radius;
		GPolygon base;
		GPolygon canon;
		int cost;
		Enemy target;
		double last_rotation = 0;
		double[] xBaseCoordinates;
		double[] yBaseCoordinates;
		double[] xCanonCoordinates;
		double[] yCanonCoordinates;

		//constructor
		private Turret(String type, double x, double y) {
			this.type = type;
			this.x = x;
			this.y = y;
			if(this.type.equals("destroyer")) {
				makeDestroyer();
			}
			else if(this.type.equals("knocker")) {
				makeKnocker();
			}
			else if(this.type.equals("sniper")) {
				makeSniper();
			}
			else if(this.type.equals("dome")) {
				makeDome();
			}
		}

		/**
		 * create destroyer canon
		 */
		private void makeDestroyer() {
			this.type = "destroyer";
			this.targetType = DESTROYER_TARGET;
			this.range = DESTROYER_RANGE;
			this.dmg = DESTROYER_DMG;
			this.reloadTime = DESTROYER_RELOAD;
			this.bulletSpeed = DESTROYER_BULLET_SPEED;
			this.cost = DESTROYER_COST;
			this.xBaseCoordinates = DESTROYER_BASE_X;
			this.yBaseCoordinates = DESTROYER_BASE_Y;
			this.xCanonCoordinates = DESTROYER_CANON_X;
			this.yCanonCoordinates = DESTROYER_CANON_Y;
			this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
			this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
			this.demage_radius = DEMAGE_RADIUS;
			this.currentLoad = this.reloadTime;
		}

		/**
		 * create knocker canon
		 */
		private void makeKnocker() {
			this.type = "knocker";
			this.targetType = KNOCKER_TARGET;
			this.range = KNOCKER_RANGE;
			this.dmg = KNOCKER_DMG;
			this.reloadTime = KNOCKER_RELOAD;
			this.bulletSpeed = KNOCKER_BULLET_SPEED;
			this.cost = KNOCKER_COST;
			this.xBaseCoordinates = KNOCKER_BASE_X;
			this.yBaseCoordinates = KNOCKER_BASE_Y;
			this.xCanonCoordinates = DESTROYER_CANON_X;
			this.yCanonCoordinates = DESTROYER_CANON_Y;
			Color color = new Color(150,150,150);
			this.base = createBase(color, this.xBaseCoordinates, this.yBaseCoordinates);
			this.canon = createCanon(Color.BLUE, this.xCanonCoordinates, this.yCanonCoordinates);
			this.demage_radius = DEMAGE_RADIUS;
			this.currentLoad = this.reloadTime;
		}
		
		/**
		 * create sniper canon
		 */
		private void makeSniper() {
			this.type = "sniper";
			this.targetType = SNIPER_TARGET;
			this.range = SNIPER_RANGE;
			this.dmg = SNIPER_DMG;
			this.reloadTime = SNIPER_RELOAD;
			this.bulletSpeed = SNIPER_BULLET_SPEED;
			this.cost = SNIPER_COST;
			this.xBaseCoordinates = SNIPER_BASE_X;
			this.yBaseCoordinates = SNIPER_BASE_Y;
			this.xCanonCoordinates = SNIPER_CANON_X;
			this.yCanonCoordinates = SNIPER_CANON_Y;
			this.base = createBase(Color.DARK_GRAY, this.xBaseCoordinates, this.yBaseCoordinates);
			this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
			this.demage_radius = DEMAGE_RADIUS;
			this.currentLoad = this.reloadTime;
		}
		
		/**
		 * create dome canon
		 */
		private void makeDome() {
			this.type = "dome";
			this.targetType = DOME_TARGET;
			this.range = DOME_RANGE;
			this.dmg = DOME_DMG;
			this.reloadTime = DOME_RELOAD;
			this.bulletSpeed = DOME_BULLET_SPEED;
			this.cost = DOME_COST;
			this.xBaseCoordinates = DESTROYER_BASE_X;
			this.yBaseCoordinates = DESTROYER_BASE_Y;
			this.xCanonCoordinates = DESTROYER_CANON_X;
			this.yCanonCoordinates = DESTROYER_CANON_Y;
			this.base = createBase(new Color(60, 60, 150), this.xBaseCoordinates, this.yBaseCoordinates);
			this.canon = createCanon(Color.BLACK, this.xCanonCoordinates, this.yCanonCoordinates);
			this.demage_radius = DEMAGE_RADIUS;
			this.currentLoad = this.reloadTime;
		}

		/**
		 * method to create base of the turret
		 */
		private GPolygon createBase(Color color, double[] xCoordinates, double[] yCoordinates) {
			GPolygon base = new GPolygon();
			for(int i = 0; i<xCoordinates.length; i++) {
				base.addVertex(xCoordinates[i], yCoordinates[i]);
			}
			base.setFilled(true);
			base.setColor(color);
			add(base, this.x, this.y);
			return base;
		}

		/**
		 * method to create canon of the turret
		 */
		private GPolygon createCanon(Color color, double[] xCoordinates, double[] yCoordinates) {
			GPolygon canon = new GPolygon();
			for(int i = 0; i<xCoordinates.length; i++) {
				canon.addVertex(xCoordinates[i], yCoordinates[i]);
			}
			canon.setFilled(true);
			canon.setColor(color);
			add(canon, this.x, this.y);
			return canon;
		}

		/**
		 * method to move the object to location
		 */
		private void setLocation(double x, double y) {
			this.x = x;
			this.y = y;
			this.base.setLocation(this.x, this.y);
			this.canon.setLocation(this.x, this.y);
		}
	}

	/**
	 * Class of Enemy object
	 * */	
	private class Enemy{
		double speed;
		double max_health;
		double health;
		String type;
		int award;
		int healing;
		double x;
		double y;
		double lastRotation = 0;
		GPolygon vehicle;
		GRect greenHealth;
		GRect redHealth;
		ArrayList<Double> pathX;
		ArrayList<Double> pathY;
		double[] xCoordinates;
		double[] yCoordinates;
		String movement;
		int wave;

		//constructor
		private Enemy(String type, ArrayList<Double> pathX, ArrayList<Double> pathY, int vawe_number) {
			this.wave = vawe_number;
			this.pathX = copyArrayList(pathX);
			this.pathY = copyArrayList(pathY);
			this.type = type;
			this.x = this.pathX.get(0);
			this.y = this.pathY.get(0);
			this.pathX.remove(0);
			this.pathY.remove(0);
			this.movement = "ground";
			makeHealthBar();
			if(this.type.equals("puncher")) {
				makePuncher();
			}
			if(this.type.equals("puncher_speeder")) {
				makePuncherSpeeder();
			}
			if(this.type.equals("puncher_healer")) {
				makePuncherHealer();
			}
			if(this.type.equals("flyer")) {
				makeFlyer();
			}
			if(this.type.equals("briger")) {
				makeBriger();
			}
		}

		/**
		 * create puncher Enemy
		 */
		private void makePuncher() {
			this.speed = PUNCHER_SPEED;
			this.type = "puncher";
			this.max_health = PUNCHER_HEALTH + PUNCHER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
			this.health = this.max_health;
			this.award = PUNCHER_AWARD;
			this.healing = PUNCHER_HEALING;
			this.xCoordinates = PUNCHER_X;
			this.yCoordinates = PUNCHER_Y;
			this.vehicle = createVehicle(Color.BLUE, this.xCoordinates, this.yCoordinates);
		}

		/**
		 * create puncher speeder Enemy
		 */
		private void makePuncherSpeeder() {
			this.speed = PUNCHER_SPEEDER_SPEED;
			this.type = "puncher_speeder";
			this.max_health = PUNCHER_SPEEDER_HEALTH+ PUNCHER_SPEEDER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
			this.health = this.max_health;
			this.award = PUNCHER_SPEEDER_AWARD;
			this.healing = PUNCHER_SPEEDER_HEALING;
			this.xCoordinates = PUNCHER_X;
			this.yCoordinates = PUNCHER_Y;
			Color color = new Color(20, 20, 160);
			this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
		}
		
		/**
		 * create puncher healer Enemy
		 */
		private void makePuncherHealer() {
			this.speed = PUNCHER_HEALER_SPEED;
			this.type = "puncher_healer";
			this.max_health = PUNCHER_HEALER_HEALTH + PUNCHER_HEALER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
			this.health = this.max_health;
			this.award = PUNCHER_HEALER_AWARD;
			this.healing = PUNCHER_HEALER_HEALING;
			this.xCoordinates = PUNCHER_X;
			this.yCoordinates = PUNCHER_Y;
			Color color = new Color(20, 160, 20);
			this.vehicle = createVehicle(color, this.xCoordinates, this.yCoordinates);
		}
		
		/**
		 * create puncher briger Enemy
		 */
		private void makeBriger() {
			this.speed = BRIGER_SPEED;
			this.type = "briger";
			this.max_health = BRIGER_HEALTH + BRIGER_HEALTH*this.wave*WAVE_HEALTH_CONSTANT;
			this.health = this.max_health;
			this.award = BRIGER_AWARD;
			this.healing = BRIGER_HEALING;
			this.xCoordinates = BRIGER_X;
			this.yCoordinates = BRIGER_Y;
			this.vehicle = createVehicle(Color.BLACK, this.xCoordinates, this.yCoordinates);
		}
		
		/**
		 * create flyer Enemy
		 */
		private void makeFlyer() {
			this.speed = FLYER_SPEED;
			this.type = "flyer";
			this.max_health = FLYER_HEALTH;
			this.health = this.max_health;
			this.award = FLYER_AWARD;
			this.healing = FLYER_HEALING;
			this.xCoordinates = FLYER_X;
			this.yCoordinates = FLYER_Y;
			this.movement = "air";
			this.pathX = new ArrayList<Double>();
			double x = rg.nextDouble(100, getWidth()-SIDE_MENU_WIDTH-100);
			this.pathX.add(x); this.pathX.add(x);
			this.pathY = new ArrayList<Double>();
			this.pathY.add((getHeight()+20.0));	this.pathY.add(-10.0);
			this.x = this.pathX.get(0);
			this.y = this.pathY.get(0);
			this.pathX.remove(0);
			this.pathY.remove(0);
			this.vehicle = createVehicle(Color.BLUE, this.xCoordinates, this.yCoordinates);
		}

		/**
		 * method to create enemy vehicle
		 */
		private GPolygon createVehicle(Color color, double[] xCoordinates, double[] yCoordinates) {
			GPolygon v = new GPolygon();
			for(int i = 0; i<xCoordinates.length; i++) {
				v.addVertex(xCoordinates[i], yCoordinates[i]);
			}
			v.setFilled(true);
			v.setColor(color);
			add(v, this.x, this.y);
			return v;			
		}

		/**
		 * method to move the enemy to the next point of path, if it is on the end of path it destroys object and substract a life
		 */
		private void move(ArrayList<Enemy> enemies, int index, Player player) {
			double goalX = this.pathX.get(0);
			double goalY = this.pathY.get(0);
			double distanceX = goalX-this.x;
			double distanceY = goalY-this.y;
			double distance = countDistance(distanceX, distanceY);
			if(distance<5) {
				if(this.pathX.size()==1) {
					removeIndex(enemies, index);
					player.lives-=1;
				}
				else {
					this.pathX.remove(0);
					this.pathY.remove(0);
				}							
			}
			else {
				this.x += distanceX/distance*this.speed;
				this.y += distanceY/distance*this.speed;
				this.vehicle.setLocation(this.x, this.y);
				this.redHealth.setLocation(this.x-15, this.y-20);
				this.greenHealth.setLocation(this.x-15, this.y-20);
				double rotation = getAngle(0, distanceX, 0, distanceY);
				if(distanceX/distance*this.speed>this.speed*0.9) {
					rotation = -180;
				}
				this.vehicle.rotate(-this.lastRotation/2);
				this.vehicle.rotate(rotation/2);
				this.lastRotation = rotation;
			}
		}

		/**
		 * method to create health bar
		 */
		private void makeHealthBar() {
			this.redHealth = new GRect(30, 5);
			this.redHealth.setFilled(true);
			this.redHealth.setColor(Color.RED);
			add(this.redHealth,this.x-15, this.y-20);
			this.greenHealth = new GRect(30, 5);
			this.greenHealth.setFilled(true);
			this.greenHealth.setColor(Color.GREEN);
			add(this.greenHealth ,this.x-15, this.y-20);
		}

		/**
		 * method to remove enemy object from array list
		 */
		private void removeIndex(ArrayList<Enemy> enemies, int index) {
			GPolygon shape = this.vehicle;
			GRect greenBar = this.greenHealth;
			GRect redBar = this.redHealth;
			remove(shape);
			remove(greenBar);
			remove(redBar);
			enemies.remove(index);
		}

		/**
		 * method to copy ArrayList without keeping the relation to the first ArrayList
		 */
		private ArrayList<Double> copyArrayList(ArrayList<Double> ar){
			ArrayList<Double> newList = new ArrayList<Double>();
			for(int i = 0; i<ar.size(); i++) {
				newList.add(ar.get(i));
			}
			return newList;
		}
	}

	/**
	 * Class of Bullet object
	 * */	
	private class Bullet{
		Enemy enemy;
		double speed;
		double dmg;
		double x;
		double y;
		double demage_radius;
		double last_rotation = 0;
		GPolygon bullet;

		//constructor
		private Bullet(Enemy enemy, Turret turret, double x, double y) {
			this.enemy = enemy;
			this.x = x;
			this.y = y;
			this.dmg = turret.dmg;
			this.speed = turret.bulletSpeed;
			this.bullet = createBullet();
			this.demage_radius = turret.demage_radius;
		}

		/**
		 * method to create bullet shape
		 */
		private GPolygon createBullet() {
			GPolygon p = new GPolygon();
			p.addVertex(0, -5);
			p.addVertex(-5, 5);
			p.addVertex(5, 5);
			p.addVertex(0, -5);
			p.setFilled(true);
			add(p, this.x, this.y);
			return p;
		}		

		/**
		 * method to move the bullet - it checks in which angle the enemy is and then creates the speed (x, y)which equeals the total speed of bullet.
		 * than it rotates the bullet in the direction of movement
		 */		
		private void move() {
			double x_diff = this.enemy.x - this.x;
			double y_diff = this.enemy.y - this.y;
			double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
			double ratio = distance/this.speed;
			double x_speed = x_diff/ratio;
			double y_speed = y_diff/ratio;
			this.x += x_speed;
			this.y += y_speed;
			this.bullet.setLocation(this.x, this.y);
			double rotation = getAngle(0, this.x, 0, this.y);
			this.bullet.rotate(-this.last_rotation);
			this.bullet.rotate(rotation);
			this.last_rotation = rotation;
		}
	}

	/**
	 * Class of Player object
	 * */	
	private class Player{
		int money;
		int lives;
		int killed_enemies = 0;
		boolean started = false;
		int wave_number = 0;
		boolean gameOverRendered = false;
		String name;
		int tick = TICK;

		//constructor
		private Player() {
			this.money = PLAYER_MONEY;
			this.lives = PLAYER_LIVES;
		}
	}

	/**
	 * Class of the side menu
	 */
	private class SideMenu{
		GRect background;
		GRect button;
		GLabel nextWaveLabel;
		GLabel shopLabel;
		GLabel author;
		GRect cancel;
		GRect fasterButton;
		GLabel fasterButtonLabel;
		
		ArrayList<Turret> turretShop = new ArrayList<Turret>();
		ArrayList<GLabel> turretNameLabels = new ArrayList<GLabel>();
		ArrayList<GLabel> turretPriceLabels = new ArrayList<GLabel>();
		ArrayList<GLabel> turretDMGLabels = new ArrayList<GLabel>();
		ArrayList<GLabel> turretRangeLabels = new ArrayList<GLabel>();
		ArrayList<GLabel> turretTargetLabels = new ArrayList<GLabel>();

		//constructor
		private SideMenu() {
			createBackground();
			this.shopLabel = createLabel(getWidth()-SIDE_MENU_WIDTH/2, 25, "25");
			this.shopLabel.setLabel("SHOP");
			this.shopLabel.move(-this.shopLabel.getWidth()/2, 0);
			this.shopLabel.setColor(Color.BLACK);
			createCancel();
			addTurret("destroyer", getWidth()-SIDE_MENU_WIDTH/2, 75.0);
			addTurret("knocker", getWidth()-SIDE_MENU_WIDTH/2, 215.0);
			addTurret("sniper", getWidth()-SIDE_MENU_WIDTH/2, 355.0);
			addTurret("dome", getWidth()-SIDE_MENU_WIDTH/2, 500.0);
			addButton();
			addNextRoundLabel();
			addAuthor();
		}

		/**
		 * method creates background of the sidemenu
		 */
		private void createBackground() {
			this.background = new GRect(SIDE_MENU_WIDTH, getHeight());
			this.background.setFilled(true);
			this.background.setColor(Color.GRAY);
			add(this.background, getWidth()-160, 0);
		}

		/**
		 * method to create new shop item in side menu and labels for it
		 */
		private void addTurret(String type, double x, double y) {
			Turret turret = new Turret(type, x, y);
			this.turretShop.add(turret);
			GLabel labelName = createLabel(x, y+35, "15");
			labelName.setLabel(turret.type);
			labelName.setColor(Color.CYAN);
			labelName.move(-labelName.getWidth()/2, 0);
			this.turretNameLabels.add(labelName);
			GLabel label = createLabel(x, y+50, "15");
			label.setLabel("Price: " + turret.cost + "$");
			label.setColor(Color.BLACK);
			label.move(-label.getWidth()/2, 0);
			this.turretPriceLabels.add(label);
			GLabel labelDmg = createLabel(x, y+65, "15");
			labelDmg.setLabel("DMG: " + turret.dmg);
			labelDmg.setColor(Color.BLACK);
			labelDmg.move(-labelDmg.getWidth()/2, 0);
			this.turretDMGLabels.add(labelDmg);
			GLabel labelRange = createLabel(x, y+80, "15");
			labelRange.setLabel("Range: " + turret.range);
			labelRange.setColor(Color.BLACK);
			labelRange.move(-labelRange.getWidth()/2, 0);
			this.turretRangeLabels.add(labelRange);
			GLabel labelTarget = createLabel(x, y+95, "15");
			labelTarget.setLabel("Target: " + turret.targetType);
			labelTarget.setColor(Color.BLACK);
			labelTarget.move(-labelTarget.getWidth()/2, 0);
			this.turretTargetLabels.add(labelTarget);
		}

		/**
		 * method to create start button GRect and Faster speed button
		 */
		private void addButton() {
			this.button = new GRect(SIDE_MENU_WIDTH-10, 30);
			this.button.setFilled(true);
			this.button.setColor(Color.GREEN);
			add(this.button, getWidth()-SIDE_MENU_WIDTH+5, getHeight()-165);
			
			this.fasterButton = new GRect(SIDE_MENU_WIDTH-10, 30);
			this.fasterButton.setFilled(true);
			this.fasterButton.setColor(Color.CYAN);
			add(this.fasterButton, getWidth()-SIDE_MENU_WIDTH+5, getHeight()-60);
			this.fasterButton.setVisible(false);
		}

		/**
		 * method to create label for a start button and for the faster wave button
		 */
		private void addNextRoundLabel() {
			this.nextWaveLabel = new GLabel("Next wave");
			this.nextWaveLabel.setFont("Impact-25");
			add(this.nextWaveLabel, getWidth()-SIDE_MENU_WIDTH/2-this.nextWaveLabel.getWidth()/2, this.button.getY()+25);
			
			this.fasterButtonLabel = new GLabel("Faster");
			this.fasterButtonLabel.setFont("Impact-25");
			add(this.fasterButtonLabel, getWidth()-SIDE_MENU_WIDTH/2-this.fasterButtonLabel.getWidth()/2, this.fasterButton.getY()+25);
			this.fasterButtonLabel.setVisible(false);

		}
		
		/**
		 * method to create cancel button
		 */
		private void createCancel() {
			this.cancel = new GRect(SIDE_MENU_WIDTH-10, 60);
			this.cancel.setFilled(true);
			this.cancel.setColor(Color.RED);
			this.cancel.setLocation(getWidth()-SIDE_MENU_WIDTH/2-this.cancel.getWidth()/2, getHeight()-125 );
		}
		
		/**
		 * method to create author name label
		 */
		private void addAuthor() {
			this.author = createLabel(getWidth()-SIDE_MENU_WIDTH/2, getHeight()-10, "17");
			this.author.setLabel("© 2018 dede64");
			this.author.move(-this.author.getWidth()/2, 0);
			this.author.setColor(Color.BLACK);
		}
	}

	/**
	 * method to move enemies
	 */
	private void moveEnemies(ArrayList<Enemy> enemies, Player player) {
		for(int i = 0; i<enemies.size(); i++) {
			enemies.get(i).move(enemies, i, player);
		}
	}

	/**
	 * method to move bullets
	 */
	private void moveBullets(ArrayList<Bullet> bullets) {
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
	 */
	private void rotateCanons(ArrayList<Turret> turrets, ArrayList<Enemy> enemies) {
		for(int t = 0; t< turrets.size(); t++) {
			Turret turret = turrets.get(t);
			double min = 100000;
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
	}

	/**
	 * method to calculate degree of rotation from given points
	 */
	private double getAngle(double x1, double y1, double x2, double y2) {
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
	private void shoot(ArrayList<Turret> turrets, ArrayList<Bullet> bullets) {
		for(int i = 0; i < turrets.size(); i++) {
			Turret turret = turrets.get(i);
			if(turret.currentLoad<turret.reloadTime) {
				turret.currentLoad += 1;
			}
			else if(turret.target!=null){
				bullets.add(new Bullet(turret.target, turret, turret.x, turret.y));
				turret.currentLoad = 0;
//				ShootClip.play();
			}
		}
	}

	/**
	 * method to check if enemies health isn't below zero -> death
	 */
	private void checkHealth(ArrayList<Enemy> enemies, Player player) {
		for(int e = enemies.size()-1; e>=0; e--) {
			Enemy enemy = enemies.get(e);
			if(enemy.health<=0) {
				GPolygon shape = enemy.vehicle;
				GRect greenBar = enemy.greenHealth;
				GRect redBar = enemy.redHealth;
				remove(shape);
				remove(greenBar);
				remove(redBar);
				player.money += enemy.award;
				player.killed_enemies += 1;
				enemies.remove(e);
			}
		}
	}

	/**
	 * method to heal enemies with healing abilities
	 */
	private void healEnemies(ArrayList<Enemy> enemies) {
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
	private void moveHealthBars(ArrayList<Enemy> enemies) {
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
	 * method to create label on given coordinates
	 */
	private GLabel createLabel(double x, double y, String size) {
		GLabel label = new GLabel("");
		String font = "Papyrus-" + size;
		label.setFont(font);
		label.setColor(Color.GRAY);
		add(label, x, y);
		return label;		
	}

	/**
	 * method to change label with player score and lives
	 */
	private void changeScoreLabel(Player player, GLabel label) {
		label.setLabel("Money: " + player.money + "$   Health: " + player.lives + "   Wave: " + (player.wave_number +1));
	}

	/**
	 * method to grab item from shop, when clicked on it
	 * method to check start button, to start new vawe, if the last is killed
	 */
	private void checkClick(SideMenu menu, Player player) {
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
	private void moveOnClick() {
		if(onClick!=null) {
			onClick.setLocation(mouseX, mouseY);
		}
	}

	/**
	 * method to release turret on the play field
	 */
	private void placeTurret(ArrayList<Turret> turrets, Player player, SideMenu menu) {
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
		}
		else {
			mouseRelease = false;
		}
	}

	/**
	 * method to color shop items by their availability
	 */
	private void colorShop(SideMenu menu, Player player) {
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
	private boolean placeAvailable(double x, double y, ArrayList<Turret> turrets){
		boolean available = true;
		for(int i = 0; i<turrets.size(); i++) {
			if (turrets.get(i).base.contains(x, y)){
				available = false;
			}
		}
		if(available == true&&x<1240) {
			GImage background = new GImage("map.png", 0, 0);
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
	private double countDistance(double x, double y) {
		double diagonal = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		return diagonal;
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
	 */
	private void addEnemies(ArrayList<Enemy> enemies, ArrayList<ArrayList<String>> waves, int counter, ArrayList<Double> pathX, ArrayList<Double> pathY, Player player) {
		if(counter%WAVE_SPACING==0 && waves.size()>0 && player.started == true) {
			ArrayList<String> wave = waves.get(0);
			enemies.add(new Enemy(wave.get(0), pathX, pathY, player.wave_number));
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
	private void checkPlayerLives(Player player){
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
	private ArrayList<String> getWave(String[] wave_array){
		ArrayList<String> wave = new ArrayList<>();
		for(int i = 0; i<wave_array.length; i++) {
			wave.add(wave_array[i]);
		}
		return wave;
	}
	
	/**
	 * method to generate random wave
	 */
	private ArrayList<String> getRandomWave(){
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
	private void hideWaveButton(SideMenu menu, ArrayList<Enemy> enemies, Player player) {
		if(enemies.size() > 0 && menu.button.isVisible() == true) {
			menu.button.setVisible(false);
			menu.nextWaveLabel.setVisible(false);
		}
		else if(enemies.size() == 0 && menu.button.isVisible() == false) {
			menu.button.setVisible(true);
			menu.nextWaveLabel.setVisible(true);
			player.tick = TICK;
		}
		if(enemies.size() > 0 && menu.fasterButton.isVisible() == false) {
			menu.fasterButton.setVisible(true);
			menu.fasterButtonLabel.setVisible(true);
		}
		else if(enemies.size() == 0 && menu.fasterButton.isVisible() == true) {
			menu.fasterButton.setVisible(false);
			menu.fasterButtonLabel.setVisible(false);
		}
	}
}


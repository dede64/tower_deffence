package cz.dede.resources;

public interface TDConstants {
    /**
     * Width and height of application window, in pixels.
     * These should be used when setting up the initial size of the game,
     * but in later calculations you should use getWidth() and getHeight()
     * rather than these constants for accurate size information.
     */
    int APPLICATION_WIDTH = 1450;
    int APPLICATION_HEIGHT = 800;

    /**
     * Dimensions of game board (usually the same), in pixels
     */
    int BOARD_WIDTH = APPLICATION_WIDTH;
    int BOARD_HEIGHT = APPLICATION_HEIGHT;

    int TICK = 1; // TODO remove this
    int AVERAGE_TPS_FROM = 40; // how many cycles are averaged into tps number
    int TARGET_FPS = 120;

    //--------------------------------TURRETS-------------------------------------------

    /**
     * Constants of destroyer turret
     */
    String DESTROYER_TARGET = "ground";
    double DESTROYER_RANGE = 250;
    double DESTROYER_DMG = 8;
    double DESTROYER_RELOAD = 250; //ms
    double DESTROYER_BULLET_SPEED = 0.6; // px/ms
    int DESTROYER_COST = 50;
    double[] DESTROYER_BASE_X = {0, -20, -20, 20, 20, 0};
    double[] DESTROYER_BASE_Y = {-20, -20, 20, 20, -20, -20};
    double[] DESTROYER_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    double[] DESTROYER_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};
    


    /**
     * Constants of sniper turret
     */
    String SNIPER_TARGET = "air/ground";
    double SNIPER_RANGE = 450;
    double SNIPER_DMG = 100;
    double SNIPER_RELOAD = 2500; //ms
    double SNIPER_BULLET_SPEED = 1.2;
    int SNIPER_COST = 140;
    double[] SNIPER_BASE_X = {0, -10, -10, -20, -20, -15, -15, -20, -20, -10, -10, 10, 10, 20, 20, 15, 15, 20, 20, 10, 10, 0};
    double[] SNIPER_BASE_Y = {-15, -15, -20, -20, -10, -10, 10, 10, 20, 20, 15, 15, 20, 20, 10, 10, -10, -10, -20, -20, -15, -15};
    double[] SNIPER_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    double[] SNIPER_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};


    double DAMAGE_RADIUS = 5;

    /**
     * Constants of knocker turret
     */
    double[] KNOCKER_BASE_X = {-10, -20, -20, -10, 10, 20, 20, 10};
    double[] KNOCKER_BASE_Y = {-20, -10, 10, 20, 20, 10, -10, -20};
    String KNOCKER_TARGET = "ground";
    double KNOCKER_RANGE = 125;
    double KNOCKER_DMG = 15;
    double KNOCKER_RELOAD = 100; //ms
    double KNOCKER_BULLET_SPEED = 0.96;
    int KNOCKER_COST = 115;

    /**
     * Constants of dome
     */
    String DOME_TARGET = "air";
    double DOME_RANGE = 300;
    double DOME_DMG = 8;
    double DOME_RELOAD = 120; //ms
    double DOME_BULLET_SPEED = 0.96;
    int DOME_COST = 100;

    /**
     * Constants of rocket launcher
     */
    String ROCKET_TARGET = "ground";
    double ROCKET_RANGE = 2000;
    double ROCKET_DMG = 2000;
    double ROCKET_RELOAD = 7500; //ms
    double ROCKET_BULLET_SPEED = 0.18;
    int ROCKET_COST = 1500;
    double[] ROCKET_BASE_X = {0, 0, -10, -20, -10, -20, -10, 0, 10, 20, 10, 20, 10, 0, 0};
    double[] ROCKET_BASE_Y = {0, -10, -20, -20, 0, 20, 20, 10, 20, 20, 0, -20, -20, -10, 0};
    double[] ROCKET_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    double[] ROCKET_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};

    /**
     * Constants of bonus
     */
    String BONUS_TARGET = "ground";
    double BONUS_RANGE = 280;
    double BONUS_DMG = 35;
    double BONUS_RELOAD = 250; //ms
    double BONUS_BULLET_SPEED = 0.6;
    int BONUS_COST = 200;
    double[] BONUS_BASE_X = {
            0, 0, -20, -20, -30, -40, -30, -40, -40, -30, -40, -30, -20, -20,
            20, 20, 30, 40, 30, 40, 40, 30, 40, 30, 20, 20, 0, 0
    };
    double[] BONUS_BASE_Y = {
            0, -40, -40, -30, -40, -30, -20, -20, 20, 20, 30, 40, 30, 40,
            40, 30, 40, 30, 20, 20, -20, -20, -30, -40, -30, -40, -40, 0
    };
    double[] BONUS_CANON_X = {0, 0, -10, -5, -15, -15, 15, 15, 5, 10, 0, 0};
    double[] BONUS_CANON_Y = {0, -60, -60, -15, -15, 15, 15, -15, -15, -60, -60, 0};
    double BONUS_MONEY = 0.1;

    //---------------------------------------------ENEMIES---------------------------------------------------------

    /**
     * Constants of puncher speeder Enemy
     */
    int PUNCHER_SPEEDER_AWARD = 30;
    int PUNCHER_SPEEDER_HEALING = 0;
    int PUNCHER_SPEEDER_HEALTH = 150;
    double PUNCHER_SPEEDER_SPEED = 0.42; // px/ms

    /**
     * Constants of puncher healer Enemy
     */
    int PUNCHER_HEALER_AWARD = 45;
    double PUNCHER_HEALER_HEALING = 0.12;
    int PUNCHER_HEALER_HEALTH = 250;
    double PUNCHER_HEALER_SPEED = 0.24;

    /**
     * Constants of puncher Enemy
     */
    int PUNCHER_AWARD = 20;
    int PUNCHER_HEALING = 0;
    int PUNCHER_HEALTH = 140;
    double PUNCHER_SPEED = 0.24;
    double[] PUNCHER_X = {0, -5, -10, -7, -7, -3, -3, 3, 3, 7, 7, 10, 5, 0};
    double[] PUNCHER_Y = {-10, -10, 10, 10, 13, 13, 10, 10, 13, 13, 10, 10, -10, -10};

    /**
     * Constants of flyer Enemy
     */
    int FLYER_AWARD = 20;
    int FLYER_HEALING = 0;
    int FLYER_HEALTH = 150;
    double FLYER_SPEED = 0.24;
    double[] FLYER_X = {0,-10, 0, 10, 0};
    double[] FLYER_Y = {-10, 0, 10, 0, -10};

    /**
     * Constants of briger Enemy
     */
    int BRIGER_AWARD = 200;
    int BRIGER_HEALING = 0;
    int BRIGER_HEALTH = 950;
    double BRIGER_SPEED = 0.24;
    double[] BRIGER_X = {0, -7, -15, -10, -10, -5, -5, 5, 5, 10, 10, 15, 7, 0};
    double[] BRIGER_Y = {-15, -15, 15, 15, 20, 20, 15, 15, 20, 20, 15, 15, -15, -15};

    /**
     * Constants of player object
     */
    int PLAYER_MONEY = 150;
    int PLAYER_LIVES = 10;

    /**
     * Constants of side menu
     */
    int SIDE_MENU_WIDTH = 210;
    int INFO_BOX_WIDTH = 240;
    int INFO_BOX_HEIGHT = 100;
    double[] INFO_BOX_Y = {0, 10, 50, 50, -50, -50, -10, 0};
    double[] INFO_BOX_X = {0, -20, -20, -180, -180, -20, -20, 0};

    double[] PAUSE_X = {0, 0, 18, 18, 0};
    double[] PAUSE_Y = {0, 40, 40, 0, 0};

    /**
     * Constants of waves
     */
    int WAVE_SPACING = 1250; //ms
    double WAVE_HEALTH_CONSTANT = 0.05; //constant tells how much to increase enemy health each wave
    int WAVE_RANDOM_MUTATION = 10;
    int WAVE_RANDOM_MUTATION_FIRST_WAVE = 6;
    String[] FIRST_WAVE = {"puncher", "puncher", "puncher", "puncher", "puncher", "puncher"};
    String[] SECOND_WAVE = {"puncher", "puncher", "puncher_speeder", "puncher", "puncher", "puncher"};
    String[] THIRD_WAVE = {"puncher_speeder", "puncher_speeder", "puncher_speeder", "puncher", "puncher", "puncher_speeder"};
    String[] FORTH_WAVE = {"puncher", "puncher","puncher_healer","puncher", "puncher","puncher_speeder"};
    String[] FIFTH_WAVE = {"puncher", "flyer","flyer","puncher", "puncher","puncher_speeder", "flyer", "briger"};


}
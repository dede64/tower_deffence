package cz.dede.resources;

public interface TDConstants {
    /**
     * Width and height of application window, in pixels.
     * These should be used when setting up the initial size of the game,
     * but in later calculations you should use getWidth() and getHeight()
     * rather than these constants for accurate size information.
     */
    public static final int APPLICATION_WIDTH = 1450;
    public static final int APPLICATION_HEIGHT = 800;

    /**
     * Dimensions of game board (usually the same), in pixels
     */
    public static final int BOARD_WIDTH = APPLICATION_WIDTH;
    public static final int BOARD_HEIGHT = APPLICATION_HEIGHT;

    public static final int TICK = 8;
    public static final int FAST_TICK = 2;
    public static final int AVERAGE_FPS = 40;

    //--------------------------------TURRETS-------------------------------------------

    /**
     * Constants of destroyer turret
     */
    public static final String DESTROYER_TARGET = "ground";
    public static final double DESTROYER_RANGE = 250;
    public static final double DESTROYER_DMG = 8;
    public static final int DESTROYER_RELOAD = 30;
    public static final double DESTROYER_BULLET_SPEED = 5;
    public static final int DESTROYER_COST = 50;
    public static final double[] DESTROYER_BASE_X = {0, -20, -20, 20, 20, 0};
    public static final double[] DESTROYER_BASE_Y = {-20, -20, 20, 20, -20, -20};
    public static final double[] DESTROYER_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    public static final double[] DESTROYER_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};


    /**
     * Constants of sniper turret
     */
    public static final String SNIPER_TARGET = "air/ground";
    public static final double SNIPER_RANGE = 450;
    public static final double SNIPER_DMG = 100;
    public static final int SNIPER_RELOAD = 300;
    public static final double SNIPER_BULLET_SPEED = 10;
    public static final int SNIPER_COST = 140;
    public static final double[] SNIPER_BASE_X = {0, -10, -10, -20, -20, -15, -15, -20, -20, -10, -10, 10, 10, 20, 20, 15, 15, 20, 20, 10, 10, 0};
    public static final double[] SNIPER_BASE_Y = {-15, -15, -20, -20, -10, -10, 10, 10, 20, 20, 15, 15, 20, 20, 10, 10, -10, -10, -20, -20, -15, -15};
    public static final double[] SNIPER_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    public static final double[] SNIPER_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};


    public static final double DAMAGE_RADIUS = 5;

    /**
     * Constants of knocker turret
     */
    public static final double[] KNOCKER_BASE_X = {-10, -20, -20, -10, 10, 20, 20, 10};
    public static final double[] KNOCKER_BASE_Y = {-20, -10, 10, 20, 20, 10, -10, -20};
    public static final String KNOCKER_TARGET = "ground";
    public static final double KNOCKER_RANGE = 125;
    public static final double KNOCKER_DMG = 15;
    public static final int KNOCKER_RELOAD = 12;
    public static final double KNOCKER_BULLET_SPEED = 8;
    public static final int KNOCKER_COST = 115;

    /**
     * Constants of dome
     */
    public static final String DOME_TARGET = "air";
    public static final double DOME_RANGE = 300;
    public static final double DOME_DMG = 8;
    public static final int DOME_RELOAD = 14;
    public static final double DOME_BULLET_SPEED = 8;
    public static final int DOME_COST = 100;

    /**
     * Constants of rocket launcher
     */
    public static final String ROCKET_TARGET = "ground";
    public static final double ROCKET_RANGE = 2000;
    public static final double ROCKET_DMG = 2000;
    public static final int ROCKET_RELOAD = 900;
    public static final double ROCKET_BULLET_SPEED = 1.5;
    public static final int ROCKET_COST = 1500;
    public static final double[] ROCKET_BASE_X = {0, 0, -10, -20, -10, -20, -10, 0, 10, 20, 10, 20, 10, 0, 0};
    public static final double[] ROCKET_BASE_Y = {0, -10, -20, -20, 0, 20, 20, 10, 20, 20, 0, -20, -20, -10, 0};
    public static final double[] ROCKET_CANON_X = {0, -5, -5, -10, -10, 10, 10, 5, 5, 0};
    public static final double[] ROCKET_CANON_Y = {-40, -40, -10, -10, 10, 10, -10, -10, -40, -40};

    /**
     * Constants of bonus
     */
    public static final String BONUS_TARGET = "ground";
    public static final double BONUS_RANGE = 280;
    public static final double BONUS_DMG = 35;
    public static final int BONUS_RELOAD = 30;
    public static final double BONUS_BULLET_SPEED = 5;
    public static final int BONUS_COST = 1500;
    public static final double[] BONUS_BASE_X = {
            0, 0, -20, -20, -30, -40, -30, -40, -40, -30, -40, -30, -20, -20,
            20, 20, 30, 40, 30, 40, 40, 30, 40, 30, 20, 20, 0, 0
    };
    public static final double[] BONUS_BASE_Y = {
            0, -40, -40, -30, -40, -30, -20, -20, 20, 20, 30, 40, 30, 40,
            40, 30, 40, 30, 20, 20, -20, -20, -30, -40, -30, -40, -40, 0
    };
    public static final double[] BONUS_CANON_X = {0, 0, -10, -5, -15, -15, 15, 15, 5, 10, 0, 0};
    public static final double[] BONUS_CANON_Y = {0, -60, -60, -15, -15, 15, 15, -15, -15, -60, -60, 0};
    public static final double BONUS_MONEY = 0.1;

    //---------------------------------------------ENEMIES---------------------------------------------------------

    /**
     * Constants of puncher speeder Enemy
     */
    public static final int PUNCHER_SPEEDER_AWARD = 30;
    public static final int PUNCHER_SPEEDER_HEALING = 0;
    public static final int PUNCHER_SPEEDER_HEALTH = 150;
    public static final double PUNCHER_SPEEDER_SPEED = 3.5;

    /**
     * Constants of puncher healer Enemy
     */
    public static final int PUNCHER_HEALER_AWARD = 45;
    public static final int PUNCHER_HEALER_HEALING = 1;
    public static final int PUNCHER_HEALER_HEALTH = 250;
    public static final double PUNCHER_HEALER_SPEED = 2;

    /**
     * Constants of puncher Enemy
     */
    public static final int PUNCHER_AWARD = 20;
    public static final int PUNCHER_HEALING = 0;
    public static final int PUNCHER_HEALTH = 140;
    public static final double PUNCHER_SPEED = 2;
    public static final double[] PUNCHER_X = {0, -5, -10, -7, -7, -3, -3, 3, 3, 7, 7, 10, 5, 0};
    public static final double[] PUNCHER_Y = {-10, -10, 10, 10, 13, 13, 10, 10, 13, 13, 10, 10, -10, -10};

    /**
     * Constants of flyer Enemy
     */
    public static final int FLYER_AWARD = 20;
    public static final int FLYER_HEALING = 0;
    public static final int FLYER_HEALTH = 150;
    public static final double FLYER_SPEED = 2;
    public static final double[] FLYER_X = {0,-10, 0, 10, 0};
    public static final double[] FLYER_Y = {-10, 0, 10, 0, -10};

    /**
     * Constants of briger Enemy
     */
    public static final int BRIGER_AWARD = 200;
    public static final int BRIGER_HEALING = 0;
    public static final int BRIGER_HEALTH = 950;
    public static final double BRIGER_SPEED = 2;
    public static final double[] BRIGER_X = {0, -7, -15, -10, -10, -5, -5, 5, 5, 10, 10, 15, 7, 0};
    public static final double[] BRIGER_Y = {-15, -15, 15, 15, 20, 20, 15, 15, 20, 20, 15, 15, -15, -15};

    /**
     * Constants of player object
     */
    public static final int PLAYER_MONEY = 150;
    public static final int PLAYER_LIVES = 10;

    /**
     * Constants of side menu
     */
    public static final int SIDE_MENU_WIDTH = 210;
    public static final int INFO_BOX_WIDTH = 240;
    public static final int INFO_BOX_HEIGHT = 100;
    public static final double[] INFO_BOX_Y = {0, 10, 50, 50, -50, -50, -10, 0};
    public static final double[] INFO_BOX_X = {0, -20, -20, -160, -160, -20, -20, 0};

    /**
     * Constants of waves
     */
    public static final int WAVE_SPACING = 150; //150
    public static final double WAVE_HEALTH_CONSTANT = 0.05; //constant tells how much to increase enemy health each wave
    public static final int WAVE_RANDOM_MUTATION = 10;
    public static final int WAVE_RANDOM_MUTATION_FIRST_WAVE = 6;
    public static final String[] FIRST_WAVE = {"puncher", "puncher", "puncher", "puncher", "puncher", "puncher"};
    public static final String[] SECOND_WAVE = {"puncher", "puncher", "puncher_speeder", "puncher", "puncher", "puncher"};
    public static final String[] THIRD_WAVE = {"puncher_speeder", "puncher_speeder", "puncher_speeder", "puncher", "puncher", "puncher_speeder"};
    public static final String[] FORTH_WAVE = {"puncher", "puncher","puncher_healer","puncher", "puncher","puncher_speeder"};
    public static final String[] FIFTH_WAVE = {"puncher", "flyer","flyer","puncher", "puncher","puncher_speeder", "flyer", "briger"};


}
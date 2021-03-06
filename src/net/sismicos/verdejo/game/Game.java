package net.sismicos.verdejo.game;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.event.Event;
import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.sky.Sky;
import net.sismicos.verdejo.game.tree.TreeBranch;
import net.sismicos.verdejo.game.dirt.Dirt;
import net.sismicos.verdejo.game.grass.Grass;
import net.sismicos.verdejo.game.ui.TriangleUp;
import net.sismicos.verdejo.game.ui.TriangleDown;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.game.ui.UpperBar;
import net.sismicos.verdejo.game.ui.upgrade.UpgradeBranch;
import net.sismicos.verdejo.game.weather.WeatherMachine;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public final class Game {
	// CONSTANTS
	
	// resolution
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	
	// requested frames per second
	private static final int REQUESTED_FPS = 60;
	
	// upper and lower views dimensions
	public static final Rectanglef UPPER_VIEW = new Rectanglef(0, 0, WIDTH, 700);
	public static final Rectanglef LOWER_VIEW = new Rectanglef(0, 500, WIDTH, 700);
	
	// VARIABLES
	
	// amount of water and salt
	private static int water = 0; 
	private static int salt = 0;
	private static final int MAX_WATER = 10;
	private static final int MAX_SALT = 10;
	
	// rate of rain drop generation in rain drops per second
	private static float num_drops = 0f;
	
	// maximum value for rain
	public static final float RAIN_MAX = 250f;
	
	// system for weather variability
	private static WeatherMachine weather = new WeatherMachine();
	
	// time of last call to getDelta()
	private static long last_frame_time = 0L;
	
    // to calculate the FPS
    private static int current_frame = 0;
    private static int current_fps = 0;
    private static long last_fps_time = 0L;
    
    // exit flag
    private static boolean exit_requested = false;
    
    // camera position
    private static Vector3f camera_position = new Vector3f(0f, 0f, 0f);
    
    // camera going down or up flag
    private static boolean going_down = false;
    
    // camera information
    private final class Camera {
    	public static final int MAX_Y = 0;
    	public static final int MIN_Y = -500;
    	public static final float INCREMENT = 800f/1000f;
    }
    
    // pause variables
    private static boolean is_active = true;
    private static Rectanglef pause_rect = new Rectanglef(0f, 0f, WIDTH,
			1200f);
    private static Vector4f pause_color = new Vector4f(0f, 0f, 0f, .4f);
    
    // camera-dependant components
    private static ArrayList<Component> components = null;
    
    // camera-independant components
    private static ArrayList<UIComponent> ui = null;
    
    // tree representation
    private static final Vector2f TREE_INITIAL_POSITION =
    		new Vector2f(200f, 600f);
    private static TreeBranch tree = new TreeBranch(TREE_INITIAL_POSITION);
    
    // tree upgrade UI component
    private static UpgradeBranch upgrade = new UpgradeBranch();
    
    // amount of resources to fork and elongate a branch
    public static final int water_to_elongate = 2;
    public static final int salt_to_elongate = 1;
    public static final int water_to_fork = 1;
    public static final int salt_to_fork = 2;
    
	// private constructor
	private Game () {}
	
	/**
	 * Initialize the game.
	 */
	public static void init()
	{
		// initialize logger
		Logger.setDebugLevel(2);
		
		// initialize graphics
		initOpenGL();
		
		// initialize FPS
		getDelta();
		last_fps_time = getTime();
		
		// initialize weather
		weather.init();
		
		// fill up the initial tree
//		TreeBranch tree1 = new TreeBranch();
//		tree1.addBranch();
//		tree1.addBranch();
//		tree1.addBranch();
//		TreeBranch tree2 = new TreeBranch();
//		tree2.addBranch();
//		tree2.addBranch();
//		TreeBranch tree3 = new TreeBranch();
//		tree3.addBranch();
//		TreeBranch tree31 = new TreeBranch();
//		tree31.addBranch();
//		tree31.addBranch();
//		tree3.addBranch(tree31);
//		tree.addBranch(tree1);
//		tree.addBranch(tree2);
//		tree.addBranch(tree3);
		
		// initialize the component lists
		components = new ArrayList<Component>();
		ui = new ArrayList<UIComponent>();
		
		// build the components
		ui.add(tree);
		ui.add(upgrade);
		components.add(new Grass());
		components.add(new Sky());
		components.add(new Dirt());
		ui.add(new TriangleUp());
		ui.add(new TriangleDown());
		components.add(new UpperBar());
		
		// initialize the components
		Iterator<Component> it = components.iterator();
		while(it.hasNext()) {
			it.next().init();
		}
		
		Iterator<UIComponent> ui_it = ui.iterator();
		while(ui_it.hasNext()) {
			ui_it.next().init();
		}
	}
	
	public static void initOpenGL()
	{
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL.glClearColor(ColorDispatcher.FAIL_COLOR);
        
        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        
        // enable textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        // initialize OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0., WIDTH, HEIGHT, 0., -50., 50.);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	/**
	 * Game loop. 
	 */
	public static void start()
	{
		while(!exit_requested && !Display.isCloseRequested())
		{
			Display.update();
			
			if(Display.isActive())
			{
				if(!is_active) {
					is_active = true;
					getDelta();
				}
				else {
					int delta = getDelta();
					update(delta);
					if(!Event.isCollisionView()) {
						render();
					}
				}
			}
			else
			{
				is_active = false;
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e) {}
				if(Display.isVisible())
				{
					render();
					renderPause();
				}
			}
			
			Display.sync(REQUESTED_FPS);
		}
		Display.destroy();
	}
	
	public static void exit()
	{
		exit_requested = true;
	}
	
	/**
	 * Game update function.
	 * @param delta Number of ms since the last frame.
	 */
	public static void update(int delta)
	{
		// iterators
		Iterator<Component> it = null;
		Iterator<UIComponent> ui_it = null;
		
		// update the FPS counter
		updateFPS();
		
		// update the weather system
		weather.update(delta);
				
		// make all UI invisible and dispose the ones inactive
		for(int i=ui.size()-1; i>=0; --i) {
			if(ui.get(i).isDisposable()) {
				ui.remove(i);
			}
			else {
				ui.get(i).onMouseOff();
			}
		}
		
		// update the input events
		Event.update(delta);
		
		// update tree level
		tree.calculateBranchLevel();
		
		// update the components
		it = components.iterator();
		while(it.hasNext()) {
			it.next().update(delta);
		}
		ui_it = ui.iterator();
		while(ui_it.hasNext()) {
			ui_it.next().update(delta);
		}
		
		// update camera
		if(going_down) {
			if(camera_position.y > -500) {
				camera_position.y -= delta*Camera.INCREMENT;
			}
			else {
				camera_position.y = Camera.MIN_Y;
			}
		}
		else {
			if(camera_position.y < 0) {
				camera_position.y += delta*Camera.INCREMENT;
			}
			else {
				camera_position.y = Camera.MAX_Y;
			}
		}
	}
	
	public static void render()
	{	
		// clear canvas
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		// reset matrices
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// camera translation
		GL.glTranslatef(camera_position);
		
		// camera-relative components
		Iterator<Component> it = components.iterator();
		Component comp = null;
		while(it.hasNext()) {
			comp = it.next();
			if(!comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		Iterator<UIComponent> ui_it = ui.iterator();
		while(ui_it.hasNext()) {
			comp = ui_it.next();
			if(!comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		
		// reset matrices for UI
		GL11.glLoadIdentity();
		
		// position-absolute components
		it = components.iterator();
		while(it.hasNext()) {
			comp = it.next();
			if(comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		ui_it = ui.iterator();
		while(ui_it.hasNext()) {
			comp = ui_it.next();
			if(comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
	}
	
	public static void renderPause()
	{
		// background
		GL.glDrawRectangle(pause_rect, 10f, pause_color);
		
		// sad face to make the player guilty (:-D)
		Vector3f pos = new Vector3f(140f, 250f, 11f);
		Rectanglef eye = new Rectanglef(0f, 0f, 20f, 20f);
		Vector4f face_color = new Vector4f(1f, 1f, 1f, .25f);
		GL.glDrawRectangle(eye, pos, face_color);
		pos.x += 100f;
		GL.glDrawRectangle(eye, pos, face_color);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(face_color);
			GL11.glVertex3f(155f, 400f, 11f);
			GL11.glVertex3f(200f, 300f, 11f);
			GL11.glVertex3f(200f, 340f, 11f);
			GL11.glVertex3f(175f, 400f, 11f);
			
			GL11.glVertex3f(245f, 400f, 11f);
			GL11.glVertex3f(200f, 300f, 11f);
			GL11.glVertex3f(200f, 340f, 11f);
			GL11.glVertex3f(225f, 400f, 11f);
		GL11.glEnd();
		
		// sad face border
		Vector4f border_color = new Vector4f(0f, 0f, 0f, .5f);
		Rectanglef border = new Rectanglef(80f, 200f, 240f, 240f);
		GL.glDrawRectangle(border, 11f, border_color);
	}
	
	public static void renderCollisionRects() {
		// disable lighting and textures
		GL11.glDisable(GL11.GL_TEXTURE);
		
		// render Components
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// camera translation
		GL.glTranslatef(Game.getCameraPos());
		
		// camera-relative components
		UIComponent comp = null;
		for(int i=0; i<ui.size(); ++i) {
			comp = ui.get(i);
			if(!comp.isPositionAbsolute() && comp.isVisible()) {
				comp.renderCollisionRect();
			}
		}
		
		// reset matrices for UI
		GL11.glLoadIdentity();
		
		// position-absolute components
		for(int i=0; i<ui.size(); ++i) {
			comp = ui.get(i);
			if(comp.isPositionAbsolute() && comp.isVisible()) {
				comp.renderCollisionRect();
			}
		}
		
		// enable lighting and textures
		GL11.glEnable(GL11.GL_TEXTURE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	/**
	 * Changes position of the branch upgrade button.
	 * @param position New position for the button.
	 */
	public static void moveUpgradeBranch(Vector2f position) {
		upgrade.setPosition(position);
	}
	
	/**
	 * Shows/Hides the branch upgrade button.
	 */
	public static void showUpgradeBranch() {
		upgrade.show();
	}
	public static void hideUpgradeBranch() {
		upgrade.hide();
	}
	
	/**
	 * Checks if the upgrade UI must be seen according to whether the player has
	 * clicked the tree or the upgrade itself.
	 * @return Whether the upgrade UI must be seen or nor.
	 */
	public static boolean checkUpgradeMustBeSeen() {
		boolean must = true;
		if(!tree.isClicked() && !upgrade.isClicked()) {
			must = false;
			hideUpgradeBranch();
		}
		return must;
	}
	
	/**
	 * Sets the branch to be upgraded.
	 * @param branch Branch to be upgraded.
	 */
	public static void setBranchToUpgrade(TreeBranch branch) {
		upgrade.setBranch(branch);
	}
	
	/**
	 * Move the camera down (to see the root)
	 */
	public static void moveDown() {
		going_down = true;
	}
	
	/**
	 * Query if the camera is looking down.
	 * @return Whether the camera is looking down or not.
	 */
	public static boolean isDown() {
		return going_down;
	}
	
	/**
	 * Move the camera up (to see the canopy)
	 */
	public static void moveUp() {
		going_down = false;
	}
	
	/**
	 * Query if the camera is looking up.
	 * @return Whether the camera is looking up or not.
	 */
	public static boolean isUp() {
		return !isDown();
	}
	
	/**
	 * Get the array of UI Component.
	 * @return Array of UI Component.
	 */
	public static ArrayList<UIComponent> getUIComps() {
		return ui;
	}
	
	/**
	 * Initializes and adds to the UIComponent collection the given object.
	 * @param comp UIComponent to be initialized and added.
	 */
	public static void addUIComponent(UIComponent comp) {
		comp.init();
		ui.add(comp);
	}
	
	/**
	 * Get the current camera position.
	 * @return Current camera position.
	 */
	public static Vector3f getCameraPos() {
		return new Vector3f(camera_position);
	}
	
	public static int getFPS() {
        return current_fps;
	}
	
	/**
	 * Gets the current amount of water. 
	 * @return Current amount of water.
	 */
	public static int getWater() {
		return water;
	}
	/**
	 * Gets the current amount of salt. 
	 * @return Current amount of salt.
	 */
	public static int getSalt() {
		return salt;
	}
	/**
	 * Increases by one the amount of water.
	 */
	public static void increaseWater() {
		if(water < MAX_WATER) {
			water++;
		}
	}
	/**
	 * Increases by one the amount of salt.
	 */
	public static void increaseSalt() {
		if(salt < MAX_SALT) {
			salt++;
		}
	}
	/**
	 * Decreases the amount of water by the given amount.
	 * @param amount
	 * @return True if there is enough water, false otherwise.
	 */
	public static void decreaseWater(int amount) {
		if(water >= amount) {
			water -= amount;
		}
	}
	/**
	 * Decreases the amount of salt by the given amount.
	 * @param amount
	 * @return True if there is enough salt, false otherwise.
	 */
	public static void decreaseSalt(int amount) {
		if(salt >= amount) {
			salt -= amount;
		}
	}
	
	/**
	 * Checks if player has enough resources to elongate a branch.
	 * @return Whether the player can elongate a branch or not.
	 */
	public static boolean canElongate() {
		return (water >= water_to_elongate && salt >= salt_to_elongate);
	}
	
	/**
	 * Checks if player has enough resources to fork a branch.
	 * @return Whether the player can fork a branch or not.
	 */
	public static boolean canFork() {
		return (water >= water_to_fork && salt >= salt_to_fork);
	}
	
	/**
	 * Get the current rain drop rate.
	 * @return Current rain drop rate
	 */
	public static float getRainRate() {
		return num_drops;
	}
	
	/**
	 * Set the rain drop rate.
	 * @param rain_rate Desired rain rate.
	 */
	public static void setRainRate(float rain_rate) {
		num_drops = Math.min(Math.max(rain_rate, 0), RAIN_MAX);
	}
	
	/**
	 * Update the frames per second counter.    
	 */
	public static void updateFPS() {
		if(getTime() - last_fps_time > 1000) {
			current_fps = current_frame;
	        current_frame = 0;
	        last_fps_time += 1000;
	        Logger.printInfo("Current FPS: " + current_fps + ".");
		}
		current_frame++;
	}
	
	/**
	 * Get the number of ms since the last call to getDelta().
	 * @return Number of ms since the last call.
	 */
	private static int getDelta()
	{
		long time = getTime();
		int delta = (int) (time - last_frame_time);
		last_frame_time = time;
		return delta;
	}
	
	/**
	 * Get the current time in ms.
	 * @return Current timestamp with ms resolution.
	 */
	private static long getTime()
	{
		return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
	}
}

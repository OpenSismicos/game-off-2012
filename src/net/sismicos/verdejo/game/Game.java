package net.sismicos.verdejo.game;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import net.sismicos.verdejo.event.Event;
import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.sky.BasicSky;
import net.sismicos.verdejo.game.dirt.BasicDirt;
import net.sismicos.verdejo.game.grass.BasicGrass;
import net.sismicos.verdejo.game.ui.TriangleUp;
import net.sismicos.verdejo.game.ui.TriangleDown;
import net.sismicos.verdejo.logger.Logger;
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
    private static boolean going_down = true;
    
    // camera information
    private final class Camera {
    	public static final int MAX_Y = 0;
    	public static final int MIN_Y = -500;
    	public static final float INCREMENT = 800f/1000f;
    }
    
    // camera-dependant components
    private static ArrayList<Component> components = null;
    
    // camera-independant components
    private static ArrayList<Component> ui = null;
    
	// private constructor
	private Game () {}
	
	/**
	 * Initialize the game.
	 */
	public static void init()
	{
		initOpenGL();
		
		getDelta();
		last_fps_time = getTime();
		
		// initialize the component lists
		components = new ArrayList<Component>();
		ui = new ArrayList<Component>();
		
		// build the components
		components.add(new BasicSky());
		components.add(new BasicGrass());
		components.add(new BasicDirt());
		ui.add(new TriangleUp());
		ui.add(new TriangleDown());
		
		// initialize the components
		Iterator<Component> it = components.iterator();
		while(it.hasNext()) {
			it.next().init();
		}
		
		it = ui.iterator();
		while(it.hasNext()) {
			it.next().init();
		}
	}
	
	public static void initOpenGL()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(.259f, .294f, .529f, 0f);
        
        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                
        // initialize OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0., WIDTH, HEIGHT, 0., 1., -1.);
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
				int delta = getDelta();
				update(delta);
				render();
			}
			else
			{
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
		// update the FPS counter
		updateFPS();
		
		// update the input events
		Event.update(delta);
		
		// update the components
		Iterator<Component> it = components.iterator();
		while(it.hasNext()) {
			it.next().update(delta);
		}
		it = ui.iterator();
		while(it.hasNext()) {
			it.next().update(delta);
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
		GL.glTranslatef((Vector3f) camera_position);
		
		// camera-relative components
		Iterator<Component> it = components.iterator();
		Component comp = null;
		while(it.hasNext()) {
			comp = it.next();
			if(!comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		it = ui.iterator();
		while(it.hasNext()) {
			comp = it.next();
			if(!comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		
		// reset matrices for UI
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// position-absolute components
		it = components.iterator();
		while(it.hasNext()) {
			comp = it.next();
			if(comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
		it = ui.iterator();
		while(it.hasNext()) {
			comp = it.next();
			if(comp.isPositionAbsolute() && comp.isVisible()) {
				comp.render();
			}
		}
	}
	
	public static void renderPause()
	{
		// grass
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0/256f, 0/256f, 0/256f, .4f);
		GL11.glVertex3f(-1f, -1f, -1f);
		GL11.glColor4f(0/256f, 0/256f, 0/256f, .4f);
		GL11.glVertex3f(WIDTH+1f, -1f, -1f);
		GL11.glColor4f(0/256f, 0/256f, 0/256f, .4f);
		GL11.glVertex3f(WIDTH+1f, 1201f, -1f);
		GL11.glColor4f(0/256f, 0/256f, 0/256f, .4f);
		GL11.glVertex3f(-1f, 1201f, -1f);
		GL11.glEnd();
	}
	
	public static void moveDown() {
		going_down = true;
	}
	
	public static void moveUp() {
		going_down = false;
	}
	
	/**
	 * Get the array of UI Component.
	 * @return Array of UI Component.
	 */
	public static ArrayList<Component> getUIComps() {
		return ui;
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

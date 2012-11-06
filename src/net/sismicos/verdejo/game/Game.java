package net.sismicos.verdejo.game;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.sismicos.verdejo.logger.Logger;

public final class Game {
	
	public static enum View
	{
		CANOPY,
		ROOTS
	}
	
	// resolution
	private static final int WIDTH = 400;
	private static final int HEIGHT = 700;
	
	// requested frames per second
	private static final int REQUESTED_FPS = 60;
	
	// time of last call to getDelta()
	private static long last_frame_time = 0L;
	
    // to calculate the FPS
    private static int current_frame = 0;
    private static int current_fps = 0;
    private static long last_fps_time = 0L;
    
    // exit flag
    private static boolean exit_requested = false;
	
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
				if(Display.isVisible() && Display.isDirty())
				{
					render();
				}
				renderPause();
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
		updateFPS();
	}
	
	public static void render()
	{	
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		// sky
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(18/256f, 112/256f, 160/256f, 1f);
		GL11.glVertex3f(-1f, -1f, 0f);
		GL11.glColor4f(18/256f, 112/256f, 160/256f, 1f);
		GL11.glVertex3f(WIDTH+1f, -1f, 0f);
		GL11.glColor4f(18/256f, 112/256f, 160/256f, 1f);
		GL11.glVertex3f(WIDTH+1f, 600f, 0f);
		GL11.glColor4f(18/256f, 112/256f, 160/256f, 1f);
		GL11.glVertex3f(-1f, 600f, 0f);
		GL11.glEnd();
		
		// dirt
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(64/256f, 59/256f, 59/256f, 1f);
		GL11.glVertex3f(-1f, 600f, -1f);
		GL11.glColor4f(64/256f, 59/256f, 59/256f, 1f);
		GL11.glVertex3f(WIDTH+1f, 600f, -1f);
		GL11.glColor4f(64/256f, 59/256f, 59/256f, 1f);
		GL11.glVertex3f(WIDTH+1f, 1200f, -1f);
		GL11.glColor4f(64/256f, 59/256f, 59/256f, 1f);
		GL11.glVertex3f(-1f, 1200f, -1f);
		GL11.glEnd();
		
		// grass
	}
	
	public static void renderPause()
	{
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

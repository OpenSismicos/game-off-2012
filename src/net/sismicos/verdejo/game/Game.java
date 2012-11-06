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
	
	private static final int REQUESTED_FPS = 60;
	
	// time of last call to getDelta()
	private static long last_frame_time = 0L;
	
    // to calculate the FPS
    private static int current_frame = 0;
    private static int current_fps = 0;
    private static long last_fps_time = 0L;
	
	// private constructor
	private Game () {}
	
	/**
	 * Initialize the game.
	 */
	public static void init()
	{
		getDelta();
		last_fps_time = getTime();
	}
	
	/**
	 * Game loop. 
	 */
	public static void start()
	{
		while(!Display.isCloseRequested())
		{
			Display.update();
			
			if(Display.isActive())
			{
				int delta = getDelta();
				update(delta);
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
					int delta = getDelta();
					update(delta);
				}
			}
			
			Display.sync(REQUESTED_FPS);
		}
	}
	
	/**
	 * Game update function.
	 * @param delta Number of ms since the last frame.
	 */
	public static void update(int delta)
	{
		updateFPS();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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

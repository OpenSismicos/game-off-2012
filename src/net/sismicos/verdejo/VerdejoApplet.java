package net.sismicos.verdejo;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.BorderLayout;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import net.sismicos.verdejo.game.Game;

public class VerdejoApplet extends Applet {
	Canvas display_parent;
	Thread game_thread;
	boolean running = false;
	
	public void startLWJGL()
	{
		game_thread = new Thread()
		{
			public void run()
			{
				running = true;
				try
				{
					Display.setParent(display_parent);
					Display.create();
					Display.setVSyncEnabled(true);
					initLWJGL();
				} catch (LWJGLException e)
				{
					e.printStackTrace();
				}
				startGameLoop();
			}
		};
		game_thread.start();
	}
	
	private void initLWJGL()
	{
		Game.init();
	}
	
	private void stopLWJGL()
	{
		running = false;
		Game.exit();
		try
		{
			game_thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void start() {}
	public void stop() {}

	public void destroy()
	{
		remove(display_parent);
		super.destroy();
	}
	
	public void init()
	{
		setLayout(new BorderLayout());
		try
		{
			display_parent = new Canvas()
			{
				public final void addNotify()
				{
					super.addNotify();
					startLWJGL();
				}
				
				public final void removeNotify()
				{
					stopLWJGL();
					super.removeNotify();
				}
			};
			
			display_parent.setSize(getWidth(), getHeight());
			add(display_parent);
			display_parent.setFocusable(true);
			display_parent.requestFocus();
			display_parent.setIgnoreRepaint(true);
			setVisible(true);
		}
		catch (Exception e)
		{
			System.err.println(e);
			throw new RuntimeException("Unable to create display");
		}
	}
	
	public void startGameLoop()
	{
		Game.start();
	}

}

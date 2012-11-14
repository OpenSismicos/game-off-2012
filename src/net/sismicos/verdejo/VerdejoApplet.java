package net.sismicos.verdejo;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.lwjgl.opengl.Display;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.logger.Logger;

public class VerdejoApplet extends Applet implements Runnable {
	/**
	 * Serial version UID to make Eclipse stop complaining about it.
	 */
	private static final long serialVersionUID = 8542785583823084649L;
	
	Canvas canvas;
	Thread game_thread;
	boolean running = false;
	
	@Override
	public void destroy() {
		running = false;
		Game.exit();
		if(game_thread != null) {
			try {
				game_thread.join();
			}
			catch(InterruptedException e) {
				Logger.printErr(e.getMessage());
			}
			game_thread = null;
		}
		if(canvas != null) {
			remove(canvas);
			canvas = null;
		}
	}
	
	@Override
	public void init() {
		if(game_thread != null) {
			return;
		}
		
		running = false;
		
		setIgnoreRepaint(true);
		setLayout(new BorderLayout());
		
		canvas = new Canvas();
		canvas.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				canvas.requestFocus();
				canvas.requestFocusInWindow();
			}
		});
		
		canvas.setFocusable(true);
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));
		canvas.setSize(getWidth(), getHeight());
		add(canvas, BorderLayout.CENTER);
		canvas.setVisible(true);
		canvas.requestFocus();
		canvas.requestFocusInWindow();
		
		game_thread = new Thread(this);
		running = true;
		game_thread.start();
	}
	
	@Override
	public void run() {
		try {
			Display.setParent(canvas);
			Display.create();
			Display.setVSyncEnabled(true);
			Game.init();
			Game.start();
		}
		catch (Exception e) {
			Logger.printErr(e.getMessage());
		}
	}
	

	@Override
	public void start() {}
	
	@Override
	public void stop() {}
}

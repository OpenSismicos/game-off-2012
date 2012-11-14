package net.sismicos.verdejo.event;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.GL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class Event {
	private static boolean collision_view = false;
	
	public static void update(float delta) {
		Vector2f pos = null;
		UIComponent comp = null;
		
		// check Mouse events
		while(Mouse.next()) {
			// Mouse button 0 up event
			if(Mouse.getEventButton() == 0 &&
					Mouse.getEventButtonState() == false) {
				
				Logger.printDebug("MouseUp on Button 0 @ (" + Mouse.getX() +
						", " + Mouse.getY() + ")");
				
				// pick object
				pos = new Vector2f((float) Mouse.getX(),
						(float) Mouse.getY());
				comp = GL.glPickObject(pos, Game.getUIComps());
				
				// click the component (if there is one)
				if(comp != null && comp.isClickable() && comp.isVisible()) {
					comp.click();
				}
			}
		}
		
		// check key events
		while(Keyboard.next()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_C &&
					!Keyboard.getEventKeyState()) {
				collision_view = !collision_view;
			}
		}
		
		// check Mouse position
		pos = new Vector2f((float) Mouse.getX(), (float) Mouse.getY());
		comp = GL.glPickObject(pos, Game.getUIComps());
		
		// show the component (if there is one)
		if(comp != null) {
			comp.onMouseOver();
		}
	}
	
	public static boolean isCollisionView() {
		return collision_view;
	}
}
 	
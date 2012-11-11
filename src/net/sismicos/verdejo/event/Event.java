package net.sismicos.verdejo.event;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.GL;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class Event {	
	public static void update(float delta) {
		while(Mouse.next()) {
			// Mouse button 0 up event
			if(Mouse.getEventButton() == 0 &&
					Mouse.getEventButtonState() == false) {
				
				Logger.printDebug("MouseUp on Button 0 @ (" + Mouse.getX() +
						", " + Mouse.getY() + ")");
				
				// pick object
				Vector2f pos = new Vector2f((float) Mouse.getX(),
						(float) Mouse.getY());
				Component comp = GL.glPickObject(pos, Game.getUIComps());
				
				// click the component (if there is one)
				if(comp != null) {
					comp.click();
				}
			}
		}
	}
}
 	
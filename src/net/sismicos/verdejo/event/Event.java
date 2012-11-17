package net.sismicos.verdejo.event;

import java.util.Iterator;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Event {
	private static boolean collision_view = false;
	
	public static void update(float delta) {
		Vector2f pos = 
				new Vector2f((float) Mouse.getX(), (float) Mouse.getY());
		Vector3f comp_color = GL.glPickColor(pos);
		
		// check Mouse events
		while(Mouse.next()) {
			// Mouse button 0 up event
			if(Mouse.getEventButton() == 0 &&
					Mouse.getEventButtonState() == false) {
				
				// unclick all elements
				Iterator<UIComponent> it = Game.getUIComps().iterator();
				while(it.hasNext()) {
					UIComponent comp = it.next();
						comp.unclick();
				}
				
				Logger.printDebug("MouseUp on Button 0 @ (" + Mouse.getX() +
						", " + Mouse.getY() + ")", 2);
				
				// ONLY FOR DEBUG PURPOSES
				//comp_color = GL.glPickColor(pos);
				
				// click the component (if there is one)
				if(!ColorDispatcher.compareColors(comp_color, 
						ColorDispatcher.FAIL_COLOR)) {
					it = Game.getUIComps().iterator();
					while(it.hasNext()) {
						UIComponent comp = it.next();
						if(comp.isClickable() && comp.isVisible()) {							
							// then, click if it is the case
							comp.click(comp_color);
						}
					}
				}
				
				// if no branch nor upgrade has been clicked, hide upgrade
				Game.checkUpgradeMustBeSeen();
			}
		}
		
		// check key events
		while(Keyboard.next()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_C &&
					!Keyboard.getEventKeyState()) {
				collision_view = !collision_view;
			}
		}
		
		// show the component (if there is one)
		if(!ColorDispatcher.compareColors(comp_color,
				ColorDispatcher.FAIL_COLOR)) {
			Iterator<UIComponent> it = Game.getUIComps().iterator();
			while(it.hasNext()) {
				UIComponent comp = it.next();
				if(comp.isClickable() && comp.isVisible()) {
					comp.onMouseOver(comp_color);
				}
			}
		}
	}
	
	public static boolean isCollisionView() {
		return collision_view;
	}
}
 	
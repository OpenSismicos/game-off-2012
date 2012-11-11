package net.sismicos.verdejo.util;

import java.util.ArrayList;
import java.nio.IntBuffer;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
	
public final class GL {
	
	/**
	 * Equivalent to gl.glColor3f() but for Vector3f input arguments. 
	 * @param color
	 */
	public static void glColor3f(Vector3f color) {
		GL11.glColor3f(color.x, color.y, color.z);
	}
	
	/**
	 * Equivalent to gl.glColor4f() but for Vector4f input arguments. 
	 * @param color
	 */
	public static void glColor4f(Vector4f color) {
		GL11.glColor4f(color.x, color.y, color.z, color.w);
	}
	
	/**
	 * Equivalent to gl.glTranslatef() but for Vector3f input arguments.
	 * @param position
	 */
	public static void glTranslatef(Vector3f position) {
		GL11.glTranslatef(position.x, position.y, position.z);
	}
	
	/**
	 * Pick object under the given screen position.
	 * @param pos Vector with the screen position.
	 * @param comps List of Component to search for the picked object.
	 * @return Component representing the picked object.
	 * 
	 * Based on the code on:
	 * http://stackoverflow.com/questions/10809021/lwjgl-3d-picking
	 */
	public static UIComponent glPickObject(Vector2f pos,
			ArrayList<UIComponent> comps) {
		// disable lighting and textures
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE);
		
		// render Components
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// camera translation
		GL.glTranslatef((Vector3f) Game.getCameraPos());
		
		// camera-relative components
		Component comp = null;
		Vector3f color = new Vector3f(0f, 0f, 0f);
		for(int i=0; i<comps.size(); ++i) {
			comp = comps.get(i);
			if(!comp.isPositionAbsolute()) {
				color.x = (i % 256)/256f;
				color.y = Math.min((int)(i/256f), 255)/256f;
				color.z = Math.min((int)(i/256f/256f), 255)/256f;
				comp.renderCollisionRect(color);
			}
		}
		
		// reset matrices for UI
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// position-absolute components
		for(int i=0; i<comps.size(); ++i) {
			comp = comps.get(i);
			if(comp.isPositionAbsolute()) {
				color.x = (i % 256)/256f;
				color.y = Math.min((int)(i/256f), 255)/256f;
				color.z = Math.min((int)(i/256f/256f), 255)/256f;
				comp.renderCollisionRect(color);
			}
		}	
		
		// get selected pixel color
		IntBuffer pixel = BufferUtils.createIntBuffer(3);
		GL11.glReadPixels((int) Math.floor(pos.x), (int) Math.floor(pos.y),
				1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixel);
		
		// return the selected Component
		int index = ((byte)pixel.get(2))*256*256 + ((byte)pixel.get(1))*256 +
				((byte)pixel.get(0));
		if(index < comps.size()) {
			return comps.get(index);
		}
		else {
			return null;
		}
	}
}

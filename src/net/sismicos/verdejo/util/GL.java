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
	 * Draws a rectangle filled with the given color and with the given depth.
	 * @param rect Rectangle coordinates.
	 * @param depth Depth to place the rectangle in.
	 * @param color Color to fill the rectangle with.
	 */
	public static void glDrawRectangle(Rectanglef rect, float depth,
			Vector4f color) {
		// draw the rectangle
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX(), rect.getY(), depth);
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY(), depth);
			GL11.glVertex3f(rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight(), depth);
			GL11.glVertex3f(rect.getX(), rect.getY() + rect.getHeight(), depth);
		GL11.glEnd();
	}
	
	/**
	 * Draws a rectangle filled with the given color and with the given depth.
	 * @param rect Rectangle coordinates.
	 * @param depth Depth to place the rectangle in.
	 * @param color Color to fill the rectangle with.
	 */
	public static void glDrawRectangle(Rectanglef rect, float depth,
			Vector3f color) {
		Vector4f color4 = new Vector4f(color.x, color.y, color.z, 1f);
		glDrawRectangle(rect, depth, color4);
	}
	
	/**
	 * Draws a rectangle filled with the given color in the given position.
	 * @param rect Rectangle coordinates.
	 * @param position Position to place the rectangle.
	 * @param color Color to fill the rectangle with.
	 */
	public static void glDrawRectangle(Rectanglef rect, Vector3f position,
			Vector4f color) {
		// save the MODELVIEW matrix for later
		GL11.glPushMatrix();
		
		// translate to its position
		GL11.glLoadIdentity();
		GL.glTranslatef(position);
		
		// draw the rectangle
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex2f(rect.getX(), rect.getY());
			GL11.glVertex2f(rect.getX() + rect.getWidth(), rect.getY());
			GL11.glVertex2f(rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight());
			GL11.glVertex2f(rect.getX(), rect.getY() + rect.getHeight());
		GL11.glEnd();
		
		// restore the MODELVIEW matrix
		GL11.glPopMatrix();
	}
	
	/**
	 * Draws a circle with given radius filled with the given color in the given
	 * position. The circle is made from the given number of triangles. 
	 * @param position Where to place the circle.
	 * @param radius Radius of the circle in pixels.
	 * @param segments Number of triangles to make the circle.
	 * @param color Color to fill the triangle with.
	 */
	public static void glDrawCircle(Vector3f position, float radius, 
			int segments, Vector4f color) {
		
		// get the external border
		Vector2f[] border_points = new Vector2f[segments];
		float angle = 0;
		for(int i=0; i<segments; ++i) {
			angle = (float) ((float)i/(float) segments*2f*Math.PI);
			border_points[i] = new Vector2f((float) (-radius*Math.cos(angle)),
					(float) (radius*Math.sin(angle)));
		}
		
		// save the MODELVIEW matrix for later
		GL11.glPushMatrix();
		
		// translate to its position
		GL.glTranslatef(position);
		
		// draw the circle
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			GL.glColor4f(color);
			GL11.glVertex2f(0f, 0f);
			for(int i=0; i<segments; ++i) {
				GL11.glVertex2f(border_points[i].x, border_points[i].y);
			}
			GL11.glVertex2f(border_points[0].x, border_points[0].y);
		GL11.glEnd();
		
		// restore the MODELVIEW matrix
		GL11.glPopMatrix();
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
		GL11.glDisable(GL11.GL_TEXTURE);
		
		// render Components
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// camera translation
		GL.glTranslatef(Game.getCameraPos());
		
		// camera-relative components
		Component comp = null;
		Vector3f color = new Vector3f(0f, 0f, 0f);
		for(int i=0; i<comps.size(); ++i) {
			comp = comps.get(i);
			if(!comp.isPositionAbsolute()) {
				color.x = (i % 256)/255f;
				color.y = Math.min((int)Math.floor(i/256f), 255)/255f;
				color.z = Math.min((int)Math.floor(i/256f/256f), 255)/255f;
				comp.renderCollisionRect(color);
			}
		}
		
		// reset matrices for UI
		GL11.glLoadIdentity();
		
		// position-absolute components
		for(int i=0; i<comps.size(); ++i) {
			comp = comps.get(i);
			if(comp.isPositionAbsolute()) {
				color.x = (i % 256)/255f;
				color.y = Math.min((int)Math.floor(i/256f), 255)/255f;
				color.z = Math.min((int)Math.floor(i/256f/256f), 255)/255f;
				comp.renderCollisionRect(color);
			}
		}	
		
		// get selected pixel color
		IntBuffer pixel = BufferUtils.createIntBuffer(3);
		GL11.glReadPixels((int) Math.floor(pos.x), (int) Math.floor(pos.y),
				1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixel);
		
		// enable lighting and textures
		GL11.glEnable(GL11.GL_TEXTURE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// return the selected Component
		int index = ((byte)pixel.get(2))*256*256 + ((byte)pixel.get(1))*256 +
				((byte)pixel.get(0));
		if(index < comps.size() && index >= 0) {
			return comps.get(index);
		}
		else {
			return null;
		}
	}
}

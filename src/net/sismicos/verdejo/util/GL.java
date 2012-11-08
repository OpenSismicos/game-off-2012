package net.sismicos.verdejo.util;

import org.lwjgl.opengl.GL11;
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
}

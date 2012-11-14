package net.sismicos.verdejo.game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public abstract class Component {
	
	// collision rectangle
	private Rectanglef collision_rect = new Rectanglef(0f, 0f, 0f, 0f);
	
	/**
	 * Initialize the component.
	 */
	public abstract void init();
	
	/**
	 * Update the logic of the Component.
	 * @param delta Ms elapsed since the last update.
	 */
	public abstract void update(int delta);
	
	/**
	 * Render the Component.
	 */
	public abstract void render();
	
	/**
	 * Determines if the Component moves with the camera.
	 * @return Whether or not the Component moves with the camera. 
	 */
	public abstract boolean isPositionAbsolute();
	
	/**
	 * Determines if the Component is visible.
	 * @return Whether or not the Component is visible.
	 */
	public abstract boolean isVisible();
	
	/**
	 * Determines if the Component responds to clicks.
	 * @return Whether or not the Component responds to click.
	 */
	public abstract boolean isClickable();
	
	/**
	 * Render the collision rectangle with the given color.
	 * @param color
	 */
	public void renderCollisionRect(Vector3f color) {
		GL.glDrawRectangle(collision_rect, 0f, color);
	}
	
	/**
	 * Get/Set the collision rectangle of the Component.
	 */
	public Rectanglef getCollisionRect() {
		return new Rectanglef(collision_rect);
	}
	public void setCollisionRect(Rectanglef rect) {
		collision_rect = rect;
	}
}

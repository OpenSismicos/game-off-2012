package net.sismicos.verdejo.game;

public abstract class Component {
	
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
}

package net.sismicos.verdejo.game;

public abstract class Component {
	// depth in the scene
	private float depth = 0f;
	
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
	 * Get/Set the depth of the component in the scene.
	 */
	public float getDepth() {
		return depth;
	}
	public void setDepth(float depth) {
		this.depth = depth;
	}
}

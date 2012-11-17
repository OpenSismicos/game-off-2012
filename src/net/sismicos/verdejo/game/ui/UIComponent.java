package net.sismicos.verdejo.game.ui;

import org.lwjgl.util.vector.Vector3f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public abstract class UIComponent extends Component {

	// collision rectangle and color
	private Rectanglef collision_rect = new Rectanglef(0f, 0f, 0f, 0f);
	private Vector3f collision_color = ColorDispatcher.FAIL_COLOR;
	
	// visibility flag
	private boolean visible = false;
	
	// flag to check if the UI component has been clicked this frame
	protected boolean is_clicked = false;
	
	/**
	 * Must be called at the end of the update of its subclasses.
	 */
	@Override
	public void update(int delta) {
		is_clicked = false;
	}
	
	/**
	 * Called when the mouse is over the Component. Provides the mouse position 
	 * on the screen.
	 */
	public void onMouseOver(Vector3f color) {
		if(ColorDispatcher.compareColors(color, collision_color)) {
			onMouseOver();
		}
	}
	
	/**
	 * Called when the mouse is over the UIComponent.
	 */
	public abstract void onMouseOver();
	
	/**
	 * Called when the mouse is off the UIComponent.
	 */
	public abstract void onMouseOff();
	
	/**
	 * Hide the Component.
	 */
	public void hide() {
		visible = false;
	}
	/**
	 * Show the Component.
	 */
	public void show() {
		visible = true;
	}
	/**
	 * Check if the Component is visible.
	 * @return Whether the Component is visible or not.
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Determines if the Component responds to clicks.
	 * @return Whether or not the Component responds to click.
	 */
	public abstract boolean isClickable();
	
	/**
	 * Checker if the propagating click is for us.
	 */
	public boolean click(Vector3f color) { 
		if(ColorDispatcher.compareColors(color, collision_color)) {
			is_clicked = true;
			click();
			return true;
		}
		return false;
	}
	
	/**
	 * Action to be performed when clicked.
	 */
	public abstract void click();
	
	/**
	 * Action to be performed when unclicked.
	 */
	public abstract void unclick();
	
	/**
	 * Gets if the UI element has been clicked in this frame.
	 * @return Whether this UI element has been clicked this frame.
	 */
	public boolean isClicked() {
		return is_clicked;
	}
	
	/**
	 * Check if the component is ready for disposal.
	 * @return Whether the component is ready for disposal or not.
	 */
	public abstract boolean isDisposable();
	
	/**
	 * Render the collision rectangle with the given color.
	 * @param color
	 */
	public void renderCollisionRect() {
		GL.glDrawRectangle(collision_rect, 0f, collision_color);
	}
	
	/**
	 * Get/Set the collision rectangle of the UIComponent.
	 */
	public Rectanglef getCollisionRect() {
		return new Rectanglef(collision_rect);
	}
	public void setCollisionRect(Rectanglef rect) {
		collision_rect = rect;
	}
	
	/**
	 * Get/Set the collision color of the UIComponent.
	 */
	public Vector3f getCollisionColor() {
		return new Vector3f(collision_color);
	}
	public void setCollisionColor(Vector3f color) {
		collision_color = new Vector3f(color);
	}
}

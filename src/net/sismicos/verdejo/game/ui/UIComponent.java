package net.sismicos.verdejo.game.ui;

import org.lwjgl.util.vector.Vector2f;

import net.sismicos.verdejo.game.Component;

public abstract class UIComponent extends Component {
	
	// visibility flag
	private boolean visible = false;
	
	// flag to check if the mouse is over
	private boolean mouse_over = false;
	
	// transparency of the component
	private float alpha = 1f;
	
	// transparency increment (amount per second)
	private float alpha_inc = 1f/3f;
	
	@Override
	public void update(int delta) {
		if(mouse_over && alpha < 1f) {
			alpha = Math.min(alpha + alpha_inc*delta/1000f, 1f);
		}
		else if(!mouse_over && alpha > 0f) {
			alpha = Math.max(alpha - alpha_inc*delta/1000f, 0f);
		}
	}
	
	/**
	 * Called when the mouse is over the Component.
	 */
	public void onMouseOver() {
		mouse_over = true;
	}
	/**
	 * Called when the mouse is off the Component.
	 */
	public void onMouseOff() {
		mouse_over = false;
	}
	
	/**
	 * Called when the mouse is over the Component. Provides the mouse position 
	 * on the screen. By default calls the zero-argument onMouseOver() method.
	 */
	public void onMouseOver(Vector2f pos) {
		onMouseOver();
	}
	/**
	 * Called when the mouse is off the Component. Provides the mouse position 
	 * on the screen. By default calls the zero-argument onMouseOver() method.
	 */
	public void onMouseOff(Vector2f pos) {
		onMouseOff();
	}
	
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
	 * Action to perform when clicked. By default does nothing.
	 */
	public void click() {}
	
	/**
	 * Action to perform when clicked. Provides the click position on the 
	 * screen. By default calls the zero-argument click method.
	 */
	public void click(Vector2f position) { 
		click();
	}
	
	/**
	 * Check if the component is ready for disposal.
	 * @return Whether the component is ready for disposal or not.
	 */
	public abstract boolean isDisposable();
	
	/**
	 * Get/Set the current transparency value.
	 */
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	/**
	 * Get/Set the current transparency increment. The increment is in units of
	 * amount of transparency per second. A value of 1f goes from completely 
	 * visible to completely invisible in 1 second. 
	 */
	public float getAlphaInc() {
		return alpha_inc;
	}
	public void setAlphaInc(float alpha_inc) {
		this.alpha_inc = alpha_inc;
	}
}

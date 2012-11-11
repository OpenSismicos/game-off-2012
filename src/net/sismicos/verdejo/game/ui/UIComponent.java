package net.sismicos.verdejo.game.ui;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.logger.Logger;

public abstract class UIComponent extends Component {

	// visibility flag
	private boolean visible = false;
	
	// transparency of the component
	private float alpha = 1f;
	
	// transparency increment (amount per second)
	private float alpha_inc = 1f/3f;
	
	@Override
	public void update(int delta) {
		if(isVisible() && alpha < 1f) {
			alpha = Math.min(alpha + alpha_inc*delta/1000f, 1f);
		}
		else if(!isVisible() && alpha > 0f) {
			alpha = Math.max(alpha - alpha_inc*delta/1000f, 0f);
		}
	}
	
	/**
	 * Make the UI Component visible.
	 */
	public void show() {
		visible = true;
	}
	
	/**
	 * Make the UI Component invisible.
	 */
	public void hide() {
		visible = false;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
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

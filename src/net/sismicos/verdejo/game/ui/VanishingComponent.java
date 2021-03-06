package net.sismicos.verdejo.game.ui;

public abstract class VanishingComponent extends UIComponent {
	
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
	
	@Override
	public void onMouseOff() {
		mouse_over = false;
	}
	
	@Override
	public void onMouseOver() {
		mouse_over = true;
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

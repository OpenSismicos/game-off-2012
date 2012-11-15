package net.sismicos.verdejo.util;

public class Trapezoidf {
	private float x, y, bw, hw, h;
	
	/**
	 * Constructor from its bottom-center position, base and head width, and
	 * height.
	 * @param x
	 * @param y
	 * @param base_width
	 * @param head_width
	 * @param height
	 */
	public Trapezoidf(float x, float y, float base_width, float head_width,
			float height) {
		this.x = x;
		this.y = y;
		this.bw = base_width;
		this.hw = head_width;
		this.h = height;
	}
	
	/**
	 * Constructor from a LWJGL Rectangle.
	 * @param rect LWJGL Rectangle.
	 */
	public Trapezoidf(Trapezoidf trap) {
		setTrapezoid(trap);
	}
	
	// GETTERS AND SETTERS
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getBaseWidth() {
		return bw;
	}
	public void setBaseWidth(float width) {
		this.bw = width;
	}
	public float getHeadWidth() {
		return hw;
	}
	public void setHeadWidth(float width) {
		this.hw = width;
	}
	public float getHeight() {
		return h;
	}
	public void setHeight(float height) {
		this.h = height;
	}
	
	/**
	 * Set this trapezoid from another one.
	 * @param trap The other trapezoid.
	 */
	public void setTrapezoid(Trapezoidf trap) {
		this.x = trap.getX();
		this.y = trap.getY();
		this.bw = trap.getBaseWidth();
		this.hw = trap.getHeadWidth();
		this.h = trap.getHeight();
	}
}

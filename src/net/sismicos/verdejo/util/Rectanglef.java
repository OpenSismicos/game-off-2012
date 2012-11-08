package net.sismicos.verdejo.util;

import org.lwjgl.util.Rectangle;

public class Rectanglef {
	private float x, y, w, h;
	
	/**
	 * Constructor from its top-left position, width, and height.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Rectanglef(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
	
	/**
	 * Constructor from a LWJGL Rectangle.
	 * @param rect LWJGL Rectangle.
	 */
	public Rectanglef(Rectangle rect) {
		setRectangle(rect);
	}
	
	/**
	 * Constructor from another Rectanglef.
	 * @param rect Other Rectanglef.
	 */
	public Rectanglef(Rectanglef rect) {
		setRectangle(rect);
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
	public float getWidth() {
		return w;
	}
	public void setWidth(float width) {
		this.w = width;
	}
	public float getHeight() {
		return h;
	}
	public void setHeight(float height) {
		this.h = height;
	}
	
	/**
	 * Get a LWJGL compatible Rectangle. 
	 * @return A LWJLG compatible Rectangle.
	 */
	public Rectangle getRectangle() {
		return new Rectangle((int) x, (int) y, (int) w, (int) h);
	}
	/**
	 * Set this rectangle from a LWJGL compatible one.
	 * @param rect LWJGL compatible Rectangle.
	 */
	public void setRectangle(Rectangle rect) {
		this.x = (float) rect.getX();
		this.y = (float) rect.getY();
		this.w = (float) rect.getWidth();
		this.h = (float) rect.getHeight();
	}
	
	/**
	 * Set this rectangle from another one.
	 * @param rect The other rectangle.
	 */
	public void setRectangle(Rectanglef rect) {
		this.x = rect.getX();
		this.y = rect.getY();
		this.w = rect.getWidth();
		this.h = rect.getHeight();
	}
}

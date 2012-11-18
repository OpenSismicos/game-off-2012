package net.sismicos.verdejo.game.ui.upgrade;

import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class ForkButton extends UIComponent {	
	// background geometry and color
	private static final Rectanglef rect = new Rectanglef(0f, 0f, 92f, 16f);
	private static final Vector4f rect_color = new Vector4f(19f/255f, 
			121f/255f, 5f/255f, 1f);
	private static final float rect_depth = 13f;

	@Override
	public void init() {
		// get a non-volatile collision color
		setCollisionColor(ColorDispatcher.reserveColor());
	}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL.glDrawRectangle(rect, rect_depth, rect_color);
	}
	
	@Override
	public void renderCollisionRect() {
		GL.glDrawRectangle(rect, rect_depth, getCollisionColor());
	}

	@Override
	public void click() {
		Logger.printDebug("Fork button has been clicked.");
	}

	@Override
	public void unclick() {}
	
	@Override
	public void onMouseOver() {}

	@Override
	public void onMouseOff() {}

	@Override
	public boolean isClickable() {
		return true;
	}
	
	@Override
	public boolean isDisposable() {
		return false;
	}

	@Override
	public boolean isPositionAbsolute() {
		return false;
	}

}

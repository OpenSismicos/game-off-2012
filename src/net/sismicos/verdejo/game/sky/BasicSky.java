package net.sismicos.verdejo.game.sky;

import net.sismicos.verdejo.game.Component;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.Rectanglef;

public class BasicSky extends Component {
		
	// color
	private final Vector4f color = new Vector4f(18/255f, 112/255f, 160/255f, 1f);
	
	// position
	private Rectanglef rect = new Rectanglef(Game.UPPER_VIEW);
	private final float DEPTH = 1f; 
	
	// constructor
	public BasicSky() {
		setDepth(DEPTH);
	}
	
	@Override
	public void init() {
		rect.setHeight(rect.getHeight() - 150);
	}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX(), rect.getY(), getDepth());
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY(), getDepth());
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), getDepth());
			GL11.glVertex3f(rect.getX(), rect.getY() + rect.getHeight(), getDepth());
		GL11.glEnd();
	}
	
	@Override
	public boolean isPositionAbsolute() {
		return false;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isClickable() {
		return true;
	}
}

package net.sismicos.verdejo.game.dirt;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class BasicDirt extends Component {

	// color
	private final Vector4f color = new Vector4f(64/256f, 59/256f, 59/256f, 1f);

	// position
	private Rectanglef rect = new Rectanglef(Game.LOWER_VIEW);
	private final float DEPTH = -1f; 
	
	// constructor
	public BasicDirt() {
		setDepth(DEPTH);
	}
	
	@Override
	public void init() {
		rect.setY(rect.getY() + 150);
		rect.setHeight(rect.getHeight() - 150);
	}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX(), rect.getY(), getDepth());
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY(), getDepth());
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), getDepth());
			GL.glColor4f(color);
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
}

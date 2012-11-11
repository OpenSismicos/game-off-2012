package net.sismicos.verdejo.game.grass;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;

public class BasicGrass extends Component {

	// color
	private final Vector4f color = new Vector4f(87/256f, 194/256f, 95/256f, 1f);
	
	// position
	private Rectangle rect = new Rectangle(0, 550, Game.WIDTH, 100);
	private final float DEPTH = 0f; 
	
	// constructor
	public BasicGrass() {
		setDepth(DEPTH);
	}
	
	@Override
	public void init() {}
	
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

package net.sismicos.verdejo.game.grass;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;

public class Grass extends Component {

	// color
	private final Vector4f fore_color = new Vector4f(27/256f, 183/256f, 6/256f, 1f);
	private final Vector4f back_color = new Vector4f(19/256f, 121/256f, 5/256f, 1f);
	
	// position
	private Rectangle fore_rect = new Rectangle(0, 590, Game.WIDTH, 90);
	private Rectangle back_rect = new Rectangle(0, 550, Game.WIDTH, 100);
	private final float FORE_DEPTH = 5f; 
	private final float BACK_DEPTH = 1f;
	
	// constructor
	public Grass() {}
	
	@Override
	public void init() {}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(back_color);
			GL11.glVertex3f(back_rect.getX(), back_rect.getY(), BACK_DEPTH);
			GL11.glVertex3f(back_rect.getX() + back_rect.getWidth(), 
					back_rect.getY(), BACK_DEPTH);
			GL11.glVertex3f(back_rect.getX() + back_rect.getWidth(),
					back_rect.getY() + back_rect.getHeight(), BACK_DEPTH);
			GL11.glVertex3f(back_rect.getX(), 
					back_rect.getY() + back_rect.getHeight(), BACK_DEPTH);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(fore_color);
			GL11.glVertex3f(fore_rect.getX(), fore_rect.getY(), FORE_DEPTH);
			GL11.glVertex3f(fore_rect.getX() + fore_rect.getWidth(), 
					fore_rect.getY(), FORE_DEPTH);
			GL11.glVertex3f(fore_rect.getX() + fore_rect.getWidth(),
					fore_rect.getY() + fore_rect.getHeight(), FORE_DEPTH);
			GL11.glVertex3f(fore_rect.getX(),
					fore_rect.getY() + fore_rect.getHeight(), FORE_DEPTH);
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

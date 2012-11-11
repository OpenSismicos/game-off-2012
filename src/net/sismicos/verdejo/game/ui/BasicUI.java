package net.sismicos.verdejo.game.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;

public class BasicUI extends Component {
	private Vector4f color = new Vector4f(242/255f, 132/255f, 13/255f, 1f);
	private final float DEPTH = 1f;
	private final float SIDE = 20f;

	@Override
	public void init() {}

	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f, 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f + SIDE/2f, 5f + SIDE, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f - SIDE/2f, 5f + SIDE, DEPTH);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f, Game.HEIGHT - 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f + SIDE/2f, Game.HEIGHT - (5f + SIDE), DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f - SIDE/2f, Game.HEIGHT - (5f + SIDE), DEPTH);
		GL11.glEnd();
	}
	
	@Override
	public boolean isPositionAbsolute() {
		return true;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
}

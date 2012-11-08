package net.sismicos.verdejo.game.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;

public class BasicUI extends Component {
	private Vector4f color = new Vector4f(185/255f, 211/255f, 89/255f, 1f);
	private final float DEPTH = 1f;

	@Override
	public void init() {}

	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(5f, 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(25f, 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(25f, 25f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(5f, 25f, DEPTH);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(5f, Game.HEIGHT - 25f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(5f, Game.HEIGHT - 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH - 5f, Game.HEIGHT - 5f, DEPTH);
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH - 5f, Game.HEIGHT - 25f, DEPTH);
		GL11.glEnd();
	}

}

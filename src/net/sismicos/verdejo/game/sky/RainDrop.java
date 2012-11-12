package net.sismicos.verdejo.game.sky;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;

public class RainDrop extends Component {
	// starting height for the drop (off the screen)
	private static final float STARTING_HEIGHT = -5f;
	private static final float STARTING_HEIGHT_VAR = 100f;
	
	// flag to check if the drop is to be disposed
	private boolean to_be_disposed = false;
	
	// head position
	private Vector2f position = new Vector2f(0f, 0f);
	
	// velocity in pixels per second
	private Vector2f velocity = new Vector2f(0f, 300f);
	
	// length in pixels
	private float length = 10f;
	
	// colors
	//private static final Vector4f head_color = new Vector4f(18/255f, 112/255f, 
	//		160/255f, 1f);
	//private static final Vector4f tail_color = new Vector4f(18/255f, 112/255f, 
	//		160/255f, 1f);
	private static final Vector4f head_color = new Vector4f(0f, 0f, 0f, .25f);
	private static final Vector4f tail_color = new Vector4f(0f, 0f, 0f, .25f);
	
	// geometry
	private static final float HEAD_WIDTH = 4f;
	private static final float DEPTH = 2f;
	private static final float BOTTOM_POSITION = 800f;

	@Override
	public void init() {
		// randomize initial position
		position.set((float) Math.random()*Game.WIDTH, 
				STARTING_HEIGHT - (float)Math.random()*STARTING_HEIGHT_VAR);
	}

	@Override
	public void update(int delta) {
		if(position.y > BOTTOM_POSITION) {
			to_be_disposed = true;
		}
		else {
			Vector2f inst_velocity = new Vector2f(velocity);
			inst_velocity.scale((float) delta/1000f);
			Vector2f.add(position, inst_velocity, position);
		}
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(head_color);
			GL11.glVertex3f(position.x, position.y, DEPTH);
			GL11.glVertex3f(position.x + HEAD_WIDTH, position.y, DEPTH);
			GL.glColor4f(tail_color);
			GL11.glVertex3f(position.x + HEAD_WIDTH, position.y - length, 
					DEPTH);
			GL11.glVertex3f(position.x, position.y - length, DEPTH);
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
		return false;
	}

	/**
	 * Checks whether the RainDrop is to be disposed.
	 * @return
	 */
	public boolean isDisposable() {
		return to_be_disposed;
	}
}

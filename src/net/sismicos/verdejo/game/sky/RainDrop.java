package net.sismicos.verdejo.game.sky;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class RainDrop extends Component {
	// geometry of the rain valid positions
	private static final float STARTING_HEIGHT = -5f;
	private static final float STARTING_HEIGHT_VAR = 100f;
	private static final float BOTTOM_POSITION = 800f;
	
	// flag to check if the drop is to be disposed
	private boolean to_be_disposed = false;
	
	// head position
	private Vector2f position = new Vector2f(0f, 0f);
	
	// velocity in pixels per second
	private Vector2f velocity = new Vector2f(0f, 300f);
	
	// rain drop geometry
	private static final Rectanglef rect = new Rectanglef(0f, 0f, 4f, 10f);
	private static final float depth = 2f;
	
	// rain drop color
	private static final Vector4f color = new Vector4f(0f, 0f, 0f, .25f);
	

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
		Vector3f draw_position = new Vector3f(position.x, position.y, depth);
		GL.glDrawRectangle(rect, draw_position, color);
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

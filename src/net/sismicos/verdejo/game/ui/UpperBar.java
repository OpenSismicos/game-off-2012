package net.sismicos.verdejo.game.ui;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class UpperBar extends Component {
	
	private static final Vector4f color = new Vector4f(0f, 0f, 0f, .5f);
	private static final Rectanglef rect = new Rectanglef(5f, 5f, 
			Game.WIDTH-10f, 20f);
	private static final float DEPTH = 5f;
	
	private static final Vector4f water_color = new Vector4f(27/255f, 132/255f,
			186/255f, 1f);
	private static final Vector3f water_pos = new Vector3f(15f, 15f, 6f);

	private static final Vector4f salt_color = new Vector4f(1f, 1f, 1f, .5f);
	private static final Vector3f salt_pos = new Vector3f(75f, 15f, 6f);
	
	@Override
	public void init() {}

	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		// panel background
		GL.glDrawRectangle(rect, DEPTH, color);
		
		// water symbol
		GL.glDrawCircle(water_pos, 5f, 10, water_color);
		
		// salt symbol
		GL.glDrawCircle(salt_pos, 5f, 4, salt_color);
	}

	@Override
	public boolean isPositionAbsolute() {
		return true;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isClickable() {
		return false;
	}

}

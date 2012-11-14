package net.sismicos.verdejo.game.grass;

import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class Grass extends Component {

	// color
	private static final Vector4f fore_color = new Vector4f(27/256f, 183/256f,
			6/256f, 1f);
	private static final Vector4f back_color = new Vector4f(19/256f, 121/256f,
			5/256f, 1f);
	
	// geometry
	private static final Rectanglef fore_rect = new Rectanglef(0f, 590f,
			Game.WIDTH, 90f);
	private static final Rectanglef back_rect = new Rectanglef(0f, 550f,
			Game.WIDTH, 100f);
	private static final float fore_depth = 5f; 
	private static final float back_depth = 1f;
	
	// constructor
	public Grass() {}
	
	@Override
	public void init() {}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		// back grass
		GL.glDrawRectangle(back_rect, back_depth, back_color);
		
		// fore grass
		GL.glDrawRectangle(fore_rect, fore_depth, fore_color);
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

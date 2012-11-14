package net.sismicos.verdejo.game.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class TriangleUp extends VanishingComponent {
	private Vector4f color = new Vector4f(242/255f, 132/255f, 13/255f, 1f);
	
	private final float DEPTH = 9f;
	private final float SIDE = 20f;
	private final float VPAD = 10f;
	
	@Override
	public void init() {
		// get a non-volatile collision color
		setCollisionColor(ColorDispatcher.reserveColor());
		
		setCollisionRect(new Rectanglef(0f, VPAD, Game.WIDTH,	SIDE));
		setAlphaInc(1f/.5f);
		setAlpha(0f);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		if(Game.isUp()) {
			hide();
		}
		else {
			show();
		}
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_TRIANGLES);
			color.w = getAlpha();
			GL.glColor4f(color);
			GL11.glVertex3f(Game.WIDTH/2f, VPAD, DEPTH);
			GL11.glVertex3f(Game.WIDTH/2f + SIDE/2f, VPAD + SIDE, DEPTH);
			GL11.glVertex3f(Game.WIDTH/2f - SIDE/2f, VPAD + SIDE, DEPTH);
		GL11.glEnd();	
	}

	@Override
	public boolean isPositionAbsolute() {
		return true;
	}

	@Override
	public boolean isClickable() {
		return true;
	}
	
	@Override
	public void click() {
		Game.moveUp();
	}
	
	@Override
	public void unclick() {}
	
	@Override
	public boolean isDisposable() {
		return false;
	}
}

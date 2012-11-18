package net.sismicos.verdejo.game.ui.upgrade;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.tree.TreeBranch;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class ElongateButton extends UIComponent {
	
	// background geometry and color
	private static final Rectanglef rect = new Rectanglef(0f, 0f, 92f, 16f);
	private static final Vector4f rect_color = new Vector4f(27/255f, 132/255f, 
			186/255f, 1f);
	private static final float rect_depth = 13f;
	
	// branch to be upgraded
	private TreeBranch branch = null;
	
	// branch to draw
	private TreeBranch logo_branch = new TreeBranch();
	private Vector3f logo_position = new Vector3f(5f, 8f, 9.5f);
	private static final float logo_branch_length = 80f;
	
	// color when unable to perform action
	private static final Vector4f deactivated_color = new Vector4f(0f, 0f, 0f, 
			.75f);

	@Override
	public void init() {
		// get a non-volatile collision color
		setCollisionColor(ColorDispatcher.reserveColor());
		
		// prepare branch
		logo_branch.setLength(logo_branch_length);
		
		// update branch so it grows
		logo_branch.update(100000);
	}
	
	@Override
	public void update(int delta) {}

	@Override
	public void render() {
		GL.glDrawRectangle(rect, rect_depth, rect_color);
		
		// render branch icon
		GL11.glPushMatrix();
		GL.glTranslatef(logo_position);
		GL11.glRotatef(90f, 0f, 0f, 1f);
		logo_branch.render();
		GL11.glPopMatrix();
		
		// if its deactivated show it
		if(!canElongate()) {
			GL.glDrawRectangle(rect, rect_depth + 1f, deactivated_color);
		}
	}
	
	@Override
	public void renderCollisionRect() {
		GL.glDrawRectangle(rect, rect_depth, getCollisionColor());
	}

	@Override
	public void click() {
		if(canElongate()) {
			Game.decreaseSalt(Game.salt_to_elongate);
			Game.decreaseWater(Game.water_to_elongate);
			branch.elongate();
		}
	}
	
	/**
	 * Checks if the branch can elongate.
	 * @return Whether the branch can elongate or not.
	 */
	private boolean canElongate() {
		return (branch != null && branch.canElongate() && Game.canElongate());
	}
	
	/**
	 * Sets branch to be elongated.
	 * @param branch
	 */
	public void setBranch(TreeBranch branch) {
		this.branch = branch;
	}

	@Override
	public void unclick() {}
	
	@Override
	public void onMouseOver() {}

	@Override
	public void onMouseOff() {}

	@Override
	public boolean isClickable() {
		return true;
	}
	
	@Override
	public boolean isDisposable() {
		return false;
	}

	@Override
	public boolean isPositionAbsolute() {
		return false;
	}

}

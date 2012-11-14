package net.sismicos.verdejo.game.tree;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class TreeBranch extends UIComponent {
	
	// branch geometry
	private Rectanglef rect = new Rectanglef(-7f, 0f, 14f, -150f);
	private final float depth = 4f;
	private Vector4f color = new Vector4f(152f/255f, 81f/255f, 
			12f/255f, 1f);
	private static final Vector4f original_color = new Vector4f(152f/255f, 
			81f/255f, 12f/255f, 1f);
	private static final Vector4f selected_color = new Vector4f(176f/255f, 
			122f/255f, 70f/255f, 1f);
	
	// sub branches
	private ArrayList<UIComponent> branches = new ArrayList<UIComponent>();
	private float[] angles = null;
	
	// initial position (only effective for the root branch)
	private final Vector2f position;
	
	// maximum number of sub branches
	private static int MAX_BRANCHES = 4;
	
	// flag to check if branch is visible to collisions
	private boolean collision_visible = true;
	
	/**
	 * Public constructor. Initializes the initial position to zero.
	 */
	public TreeBranch() {
		this.position = new Vector2f(0f, 0f);
		show();
	}
	
	/**
	 * Public constructor. Initializes the initial position to the given 
	 * position.
	 * @param position Initial position.
	 */
	public TreeBranch(Vector2f position) {
		this.position = new Vector2f(position);
		show();
	}
	
	/**
	 * Add a custom branch to the set of sub branches.
	 * @param branch Custom branch to add.
	 * @return Whether the branch addition succeeded or not.
	 */
	public boolean addBranch(TreeBranch branch) {
		if(branches.size() < MAX_BRANCHES) {
			branch.init();
			branches.add(branch);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Add a default branch to the set of sub branches.
	 * @return Whether the branch addition succeeded or not.
	 */
	public boolean addBranch() {
		TreeBranch branch  = new TreeBranch();
		return addBranch(branch);
	}
	
	@Override
	public void init() {
		// get sub branch angles
		angles = calculateSubBranchAngles(branches.size());
	}

	@Override
	public void render() {
		// save state
		GL11.glPushMatrix();

		// translate to the end of my branch
		GL11.glTranslatef(position.x, position.y + rect.getHeight(), 0f);
		
		// draw sub branches
		for(int i=0; i<branches.size(); ++i) {
			// save state
			GL11.glPushMatrix();
			
			// rotate for the branch
			GL11.glRotatef(angles[i], 0f, 0f, 1f);
			
			// draw the sub branch
			branches.get(i).render();
			
			// restore state
			GL11.glPopMatrix();
		}
		
		// recover original state
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		
		// translate
		GL11.glTranslatef(position.x, position.y, 0f);
		
		// draw my branch
		GL.glDrawRectangle(rect, depth, color);
		
		// recover original state
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderCollisionRect(Vector3f color) {
		if(collision_visible) {
			
			// save state
			GL11.glPushMatrix();
	
			// translate to the end of my branch
			GL11.glTranslatef(position.x, position.y + rect.getHeight(), 0f);
			
			// draw sub branches
			for(int i=0; i<branches.size(); ++i) {
				// save state
				GL11.glPushMatrix();
				
				// rotate for the branch
				GL11.glRotatef(angles[i], 0f, 0f, 1f);
				
				// draw the sub branch
				branches.get(i).renderCollisionRect(color);
				
				// restore state
				GL11.glPopMatrix();
			}
			
			// recover original state
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			
			// translate
			GL11.glTranslatef(position.x, position.y, 0f);
			
			// draw my branch
			GL.glDrawRectangle(rect, depth, color);
			
			// recover original state
			GL11.glPopMatrix();
			
		}
	}
	
	@Override
	public void onMouseOver(Vector2f pos) {
		Vector2f new_pos = new Vector2f(pos);
		Vector2f.sub(new_pos,
				new Vector2f(position.x, position.y + rect.getHeight()),
				new_pos);
		UIComponent comp = GL.glPickObject(new_pos, branches);
		if(comp == null) {
			color = selected_color;
		}
		else {
			comp.onMouseOver(pos);
		}
	}
	
	@Override
	public void onMouseOff() {
		color = original_color;
		
		Iterator<UIComponent> it = branches.iterator();
		while(it.hasNext()) {
			it.next().onMouseOver();
		}
	}

	@Override
	public boolean isPositionAbsolute() {
		return false;
	}

	@Override
	public boolean isClickable() {
		return true;
	}
	
	@Override
	public boolean isDisposable() {
		return false;
	}
	
	/**
	 * Calculates the vector of angles in which the new branches point to.
	 * @param size Number of sub branches from this branch. 
	 * @return Array of angles. 
	 */
	private float[] calculateSubBranchAngles(int size) {
		float[] angles = null;
		
		switch(size) {
		case 0:
		case 1:
			angles = new float[1];
			angles[0] = 0f;
			break;
		case 2:
			angles = new float[2];
			angles[0] = -10f;
			angles[1] =  10f;
			break;
		case 3:
			angles = new float[3];
			angles[0] = -20f;
			angles[1] =  0f;
			angles[2] =  20f;
			break;
		case 4:
			angles = new float[2];
			angles[0] = -30f;
			angles[1] = -10f;
			angles[0] =  10f;
			angles[1] =  30f;
			break;
		default:
			Logger.printErr("Trying to sub branch more than 4 branches.");
			break;
		}
		
		return angles;
	}
}

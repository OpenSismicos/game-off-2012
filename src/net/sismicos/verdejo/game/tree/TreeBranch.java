package net.sismicos.verdejo.game.tree;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Trapezoidf;

public class TreeBranch extends UIComponent {
	
	// branch geometry
	private Trapezoidf trap = new Trapezoidf(0f, 0f, 60f, 70f, 20f);
	private final float depth = 4f;
	private Vector4f color = new Vector4f(152f/255f, 81f/255f, 
			12f/255f, 1f);
	public static final Vector4f original_color = new Vector4f(152f/255f, 
			81f/255f, 12f/255f, 1f);
	private static final Vector4f selected_color = new Vector4f(176f/255f, 
			122f/255f, 70f/255f, 1f);
	
	// branch width limits
	private static final float base_min = 8f;
	private static final float base_max = 60f;
	private static final float head_min = 1f;
	private static final float head_max = 50f;
	
	// branch width changes
	private float current_base_width_max = 2f;
	private float current_head_width_max = 8f;
	private static final float width_inc = 2f;
	
	// branch length changes
	private float current_length_max = 20f;
	private static final float length_inc = 2f;
	private static final float length_inc_next_level = 20f;
	private static final float length_max = 160f;
	
	// branch level, i.e., number of children branches
	private int level = 1; 
	private int max_level = 64;
	
	// sub branches
	private ArrayList<UIComponent> branches = new ArrayList<UIComponent>();
	private float[] angles = null;
	
	// initial position (only effective for the root branch)
	private final Vector2f position;
	
	// maximum number of sub branches
	private static int MAX_BRANCHES = 4;
	
	/**
	 * Public default constructor.Initializes the initial position to zero.
	 */
	public TreeBranch() {
		super();
		this.position = new Vector2f(0f, 0f);
		show();
	}
	
	/**
	 * Public constructor. Initializes the initial position to the given 
	 * position.
	 * @param position Initial position.
	 */
	public TreeBranch(Vector2f position) {
		super();
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
			angles = calculateSubBranchAngles(branches.size());
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
	
	/**
	 * Checks if the branch can further elongate.
	 * @return Whether the branch can further elongate or not.
	 */
	public boolean canElongate() {
		if(current_length_max >= length_max) {
			return false;
		}
		return true;
	}
	
	/**
	 * Elongates current branch.
	 * TODO: Introduce randomness for happiness.
	 */
	public void elongate() {
		if(canElongate()) {
			current_length_max += length_inc_next_level;
		}
	}
	
	/**
	 * Checks if the branch can further fork.
	 * @return Whether the branch can further fork or not.
	 */
	public boolean canFork() {
		if(branches.size() < MAX_BRANCHES) {
			return true;
		}
		return false;
	}
	
	/**
	 * Elongates current branch.
	 * TODO: Introduce randomness for happiness.
	 */
	public void fork() {
		if(canFork()) {
			addBranch();
		}
	}
	
	/**
	 * Highlights the branch (when is selected).
	 */
	public void highlight() {
		color = selected_color;
	}
	
	@Override
	public void init() {
		// get a non-volatile collision color
		setCollisionColor(ColorDispatcher.reserveColor());
		
		// get sub branch angles
		angles = calculateSubBranchAngles(branches.size());
	}

	@Override
	public void update(int delta) {
		current_head_width_max = ((head_max - head_min) *
				(float)Math.sqrt((float)(level-1f)/(float)max_level)
				+ head_min);
		
		// update rest of branches
		Iterator<UIComponent> it = branches.iterator();
		while(it.hasNext()) {
			TreeBranch branch = (TreeBranch) it.next();
			
			// make sure parent head width is greater (or equal) to children 
			// base width
			branch.update(delta);
			current_head_width_max = Math.max(current_head_width_max, 
					branch.getBaseWidth());
		}
		
		// change trapezoid geometry according to level
		current_base_width_max = ((base_max - base_min) * 
				(float)Math.sqrt((float)(level-1f)/(float)max_level)
				+ base_min);
		
		// change width according to current maximum and time
		trap.setBaseWidth(Math.min(trap.getBaseWidth() + width_inc*delta/1000f,
				current_base_width_max));
		trap.setHeadWidth(Math.min(trap.getHeadWidth() + width_inc*delta/1000f,
				current_head_width_max));
		
		// change length according to current maximum and time
		trap.setHeight(Math.min(trap.getHeight() + length_inc*delta/1000f,
				current_length_max));
		
		// update superclass
		super.update(delta);
	}
	
	/**
	 * Updates the level of all children and itself.
	 * @return The current level.
	 */
	public int calculateBranchLevel() {
		int cur_level = 1;
		Iterator<UIComponent> it = branches.iterator();
		while(it.hasNext()) {
			cur_level += ((TreeBranch)it.next()).calculateBranchLevel();
		}
		level = cur_level;
		return level;
	}
	
	/**
	 * Gets branch base width.
	 * @return Branch base width.
	 */
	public float getBaseWidth() {
		return trap.getBaseWidth();
	}
	
	
	/**
	 * Gets/Sets branch length.
	 */
	public float getLength() {
		return current_length_max;
	}
	public void setLength(float length) {
		if(length <= length_max) {
			current_length_max = length;
		}
	}

	@Override
	public void render() {
		// save state
		GL11.glPushMatrix();

		// translate to the end of my branch
		GL11.glTranslatef(position.x, position.y - trap.getHeight() + 4f, 0f);
		
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
		GL.glDrawTrapezoid(trap, depth, color);
		
		// recover original state
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderCollisionRect() {
		// save state
		GL11.glPushMatrix();

		// translate to the end of my branch
		GL11.glTranslatef(position.x, position.y - trap.getHeight() + 4f, 0f);
		
		// draw sub branches
		for(int i=0; i<branches.size(); ++i) {
			// save state
			GL11.glPushMatrix();
			
			// rotate for the branch
			GL11.glRotatef(angles[i], 0f, 0f, 1f);
			
			// draw the sub branch
			branches.get(i).renderCollisionRect();
			
			// restore state
			GL11.glPopMatrix();
		}
		
		// recover original state
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		
		// translate
		GL11.glTranslatef(position.x, position.y, 0f);
		
		// draw the collision rectangle of my branch
		GL.glDrawTrapezoid(trap, depth, getCollisionColor());
		
		// recover original state
		GL11.glPopMatrix();
	}

	@Override
	public boolean click(Vector3f color) {
		boolean any_click = false;
		
		// click from superclass
		any_click = super.click(color);
		
		// try children just in case
		if(!any_click) {
			Iterator<UIComponent> it = branches.iterator();
			while(it.hasNext()) {
				any_click |= it.next().click(color);
			}
		}
		
		// if any click, show upgrade branch
		if(any_click) {
			Game.showUpgradeBranch();
		}
		
		// store the click state
		is_clicked = any_click;
		
		return any_click;
	}
	
	@Override
	public void click() {
		highlight();
		
		// link branch with upgrade branch
		Game.setBranchToUpgrade(this);
		
		// change position of and show branch upgrade
		Game.moveUpgradeBranch(new Vector2f(Mouse.getX(), Mouse.getY()));
	}
	
	@Override
	public void unclick() {
		color = original_color;
		
		Iterator<UIComponent> it = branches.iterator();
		while(it.hasNext()) {
			it.next().unclick();
		}
	}
	
	@Override
	public void onMouseOver() {}
	
	@Override
	public void onMouseOff() {}

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
			angles = new float[4];
			angles[0] = -30f;
			angles[1] = -10f;
			angles[2] =  10f;
			angles[3] =  30f;
			break;
		default:
			Logger.printErr("Trying to sub branch more than 4 branches.");
			break;
		}
		
		return angles;
	}
}

package net.sismicos.verdejo.game.ui.upgrade;

import java.util.ArrayList;
import java.util.Iterator;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.logger.Logger;
import net.sismicos.verdejo.util.ColorDispatcher;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class UpgradeBranch extends UIComponent {
	
	// arrow points
	private ArrayList<Vector2f> points;
	private final ArrayList<Vector2f> final_points;
	
	// rectangle
	private final Rectanglef rect = new Rectanglef(0f, 0f, 106f, 53f); 
	
	// animation velocity in pixels per second
	private static final float velocity = 60f;
	
	// controller position and depth
	private Vector2f position = new Vector2f(0f, 0f);
	private static final float depth = 12f;
	
	// draw color
	private static final Vector4f color = new Vector4f(0f, 0f, 0f, .65f); 
	
	// flag to select if facing left or right
	private boolean face_right = true;
	private static final float X_TO_FACE_LEFT = 284f;
	
	// Elongate and fork buttons
	private ElongateButton elongate = new ElongateButton();
	private ForkButton fork = new ForkButton();
	
	/**
	 * Public constructor.
	 */
	public UpgradeBranch() {
		// initialize the arrow point vectors
		points = new ArrayList<Vector2f>();
		final_points = new ArrayList<Vector2f>();
		
		// initialize current points
		points.add(new Vector2f(0f, 0f));
		points.add(new Vector2f(0f, 0f));
		points.add(new Vector2f(0f, 0f));
		points.add(new Vector2f(0f, 0f));
		
		// initialize final points
		final_points.add(new Vector2f( 15f, 20f));
		final_points.add(new Vector2f(  0f,  0f));
		final_points.add(new Vector2f( 15f, 10f));
		final_points.add(new Vector2f( 30f, 10f));
		
		// initialize rectangle initial position
		rect.setX(15f);
		rect.setY(10f);
	}

	@Override
	public void init() {
		// get a non-volatile collision color
		setCollisionColor(ColorDispatcher.reserveColor());
		
		// initialize buttons
		elongate.init();
		fork.init();
	}

	@Override
	public void update(int delta) {
		// update the arrow
		for(int i=0; i<points.size(); ++i) {
			Vector2f direction = new Vector2f();
			Vector2f initial_point = new Vector2f(points.get(i));
			Vector2f final_point = new Vector2f(initial_point);
			
			// velocity vector
			Vector2f.sub(final_points.get(i), points.get(i), direction);
			if(direction.lengthSquared() > 0) {
				direction.normalise().scale(velocity*delta/1000f);
			}
			
			// if facing left invert Y component of direction
			if(!face_right) {
				direction.y = -direction.y;
			}
			
			// final vector
			Vector2f.add(final_point, direction, final_point);
			final_point.setY(Math.min(final_point.getY(), 
					final_points.get(i).getY()));
			final_point.setX(Math.min(final_point.getX(), 
					final_points.get(i).getX()));
			points.set(i, final_point);
		}
		
		// update buttons
		elongate.update(delta);
		fork.update(delta);
		
		// update superclass
		super.update(delta);
	}

	@Override
	public void render() {
		// save modelview matrix
		GL11.glPushMatrix();
		
		// translate to the initial point
		GL.glTranslatef(new Vector3f(position.x, Game.HEIGHT - position.y,
				0f));
		
		// draw the arrow
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			// set color
			GL.glColor4f(color);
			
			// change facing
			float facing = 1f;
			Rectanglef draw_rect = new Rectanglef(rect);
			if(!face_right) {
				facing = -1f;
				draw_rect.setX(facing*draw_rect.getX());
				draw_rect.setWidth(facing*draw_rect.getWidth());
			}
			
			// draw points
			Iterator<Vector2f> it = points.iterator();
			Vector2f point = new Vector2f();
			while(it.hasNext()) {
				point = it.next();
				GL11.glVertex3f(facing*point.x, point.y, depth);
			}
		GL11.glEnd();
		
		// draw the rectangle
		GL.glDrawRectangle(draw_rect, depth, color);
		
		// draw the elongate button
		GL11.glPushMatrix();
		GL.glTranslatef(new Vector3f(22f, 17f, 0f));
		elongate.render();
		GL.glTranslatef(new Vector3f(0f, 23f, 0f));
		fork.render();
		GL11.glPopMatrix();
		
		// restore modelview matrix
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderCollisionRect() {
		// save modelview matrix
		GL11.glPushMatrix();
		
		// translate to the initial point
		GL.glTranslatef(new Vector3f(position.x, Game.HEIGHT - position.y,
				0f));
		
		// draw the arrow
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			// set color
			GL.glColor3f(getCollisionColor());
			
			// change facing
			float facing = 1f;
			Rectanglef draw_rect = new Rectanglef(rect);
			if(!face_right) {
				facing = -1f;
				draw_rect.setX(facing*draw_rect.getX());
				draw_rect.setWidth(facing*draw_rect.getWidth());
			}
			
			// draw points
			Iterator<Vector2f> it = points.iterator();
			Vector2f point = new Vector2f();
			while(it.hasNext()) {
				point = it.next();
				GL11.glVertex3f(facing*point.x, point.y, depth);
			}
		GL11.glEnd();
		
		// draw the rectangle
		GL.glDrawRectangle(draw_rect, depth, getCollisionColor());
		
		// draw the elongate button
		GL11.glPushMatrix();
		GL.glTranslatef(new Vector3f(22f, 17f, 0f));
		elongate.renderCollisionRect();
		GL.glTranslatef(new Vector3f(0f, 23f, 0f));
		fork.renderCollisionRect();
		GL11.glPopMatrix();
		
		// restore modelview matrix
		GL11.glPopMatrix();
	}
	
	/**
	 * Gets/Sets the facing of the component.
	 */
	public boolean isFacingRight() {
		return face_right;
	}
	public void faceRight(boolean yesno) {
		face_right = yesno;
	}
	
	/**
	 * Gets/Sets the controller position.
	 * @param position New position for the controller.
	 */
	public void setPosition(Vector2f position) {
		if(position.x > X_TO_FACE_LEFT) {
			faceRight(false);
		}
		else {
			faceRight(true);
		} 
		this.position = new Vector2f(position);
	}
	public Vector2f getPosition() {
		return new Vector2f(position);
	}

	@Override
	public boolean click(Vector3f color) {
		boolean any_click = super.click(color);
		
		// click buttons
		any_click |= elongate.click(color);
		any_click |= fork.click(color);
		
		// if any click, keep showing the UI
		if(any_click) {
			show();
		}
		
		// click or click any children
		is_clicked = any_click;
		
		return any_click;
	}
	
	@Override
	public void click() {
		Logger.printDebug("Click on Upgrade Branch UI.", 2);
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
		return true;
	}

}

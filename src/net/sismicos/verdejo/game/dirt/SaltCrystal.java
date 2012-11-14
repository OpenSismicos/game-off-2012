package net.sismicos.verdejo.game.dirt;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class SaltCrystal extends UIComponent {
	// geometry of the crystal valid initial positions
	private static final float STARTING_HEIGHT = 950f;
	private static final float STARTING_HEIGHT_VAR = 600f;
	private static final float APPEARANCE_WIDTH = 300f;
	
	// flag to check if the crystal is to be disposed
	private boolean to_be_disposed = false;
	
	// flag to check if the crystal  is clickable
	private boolean clickable = true;
	
	// head position
	private Vector2f position = new Vector2f(0f, 0f);
	private boolean is_position_absolute = false;
	
	// velocity in pixels per second
	private Vector2f velocity = new Vector2f(0f, 20f);
	
	// salt crystal geometry and color
	private float radius = 4f;
	private float depth = 4.85f;
	private static final int segments = 4;
	private static final Vector4f color = new Vector4f(1f, 1f, 1f, 0.9f);
	private static final float INIT_RADIUS = 5f;
	private static final float MAX_RADIUS = 10f;
	private static final float RADIUS_INC = 3f;
	
	// crystal live time
	private float live = 0f;
	private static final float MAX_LIVE = 30f;
	
	// flag to check if the salt crystal has been clicked
	private boolean clicked = false;
	
	private static final Vector2f final_position = new Vector2f(75f, 15f);

	@Override
	public void init() {
		// randomize initial position
		position.set((float) (Math.random()-0.5f)*APPEARANCE_WIDTH
				+ Game.WIDTH/2f, STARTING_HEIGHT - 
				(float)(Math.random()-.5f)*STARTING_HEIGHT_VAR);
		show();
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		// update the collision rectangle
		setCollisionRect(new Rectanglef(position.x-radius-2f, 
				position.y-radius-2f, 2f*radius+4f, 2f*radius+4f));
		
		if(!clicked) {
			live += delta/1000f;
			if(live > MAX_LIVE) {
				to_be_disposed = true;
			}
			
			if(radius < MAX_RADIUS) {
				radius = Math.min(radius + RADIUS_INC*delta/1000f, MAX_RADIUS);
			}
		}
		else {
			Vector2f distance = new Vector2f();
			Vector2f.sub(final_position, position, distance);
			if(Vector2f.dot(distance, distance) < 16f) {
				to_be_disposed = true;
				Game.increaseSalt();
			}
			else {
				Vector2f inst_velocity = new Vector2f(velocity);
				inst_velocity.scale((float) delta/1000f);
				Vector2f.add(position, inst_velocity, position);
				if(radius > INIT_RADIUS) {
					radius = Math.max(radius - RADIUS_INC*delta/1000f*10f,
							INIT_RADIUS);
				}
			}
		}
	}

	@Override
	public void render() {
		Vector3f draw_position = new Vector3f(position.x, position.y, depth);
		GL.glDrawCircle(draw_position, radius, segments, color);
	}

	@Override
	public boolean isPositionAbsolute() {
		return is_position_absolute;
	}

	@Override
	public boolean isClickable() {
		return clickable;
	}
	
	@Override
	public boolean isDisposable() {
		return to_be_disposed;
	}
	
	@Override
	public void click() {
		// TODO: Only click when viewing the roots
		
		// convert relative to absolute position
		Vector2f absolute_position = new Vector2f(position);
		absolute_position.x += Game.getCameraPos().x;
		absolute_position.y += Game.getCameraPos().y;
		position = absolute_position;
		is_position_absolute = true;
		
		// change velocity to go to the marker
		Vector2f absolute_vel = new Vector2f(final_position);
		Vector2f.sub(absolute_vel, absolute_position, velocity);
		velocity.normalise().scale(300f);
		
		// chage depth
		depth = 7f;
		
		// not clickable
		clickable = false;
		
		// has been clicked
		clicked = true;
	}
}

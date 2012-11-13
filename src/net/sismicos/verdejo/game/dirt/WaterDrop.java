package net.sismicos.verdejo.game.dirt;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.ui.UIComponent;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class WaterDrop extends UIComponent {
	// geometry of the drop valid positions
	private static final float STARTING_HEIGHT = 600f;
	private static final float STARTING_HEIGHT_VAR = 0f;
	private static final float BOTTOM_POSITION = 1250f;
	
	// flag to check if the drop is to be disposed
	private boolean to_be_disposed = false;
	
	// flag to check if the drop is clickable
	private boolean clickable = true;
	
	// head position
	private Vector2f position = new Vector2f(0f, 0f);
	private boolean is_position_absolute = false;
	
	// velocity in pixels per second
	private Vector2f velocity = new Vector2f(0f, 20f);
	
	// water drop geometry and color
	private static final float radius = 4f;
	private float depth = 4f;
	private static final int segments = 10;
	private static final Vector4f color = new Vector4f(27/255f, 132/255f,
			186/255f, 1f);
	
	// flag to check if the water has been clicked
	private boolean clicked = false;
	
	private static final Vector2f final_position = new Vector2f(15f, 15f);

	@Override
	public void init() {
		// randomize initial position
		position.set((float) Math.random()*Game.WIDTH, 
				STARTING_HEIGHT - (float)Math.random()*STARTING_HEIGHT_VAR);
		show();
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		// update the collision rectangle
		setCollisionRect(new Rectanglef(position.x-radius-2f, 
				position.y-radius-2f, 2f*radius+4f, 2f*radius+4f));
		
		if(!clicked) {
			if(position.y > BOTTOM_POSITION) {
				to_be_disposed = true;
			}
			else {
				Vector2f inst_velocity = new Vector2f(velocity);
				inst_velocity.scale((float) delta/1000f);
				Vector2f.add(position, inst_velocity, position);
			}
		}
		else {
			Vector2f distance = new Vector2f();
			Vector2f.sub(final_position, position, distance);
			if(Vector2f.dot(distance, distance) < 4f) {
				to_be_disposed = true;
				Game.increaseWater();
				//clicked = false;
				//velocity.set(0f, 0f);
			}
			else {
				Vector2f inst_velocity = new Vector2f(velocity);
				inst_velocity.scale((float) delta/1000f);
				Vector2f.add(position, inst_velocity, position);
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

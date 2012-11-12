package net.sismicos.verdejo.game.sky;

import java.util.ArrayList;
import java.util.Iterator;

import net.sismicos.verdejo.game.Component;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.util.Rectanglef;

public class Sky extends Component {
	
	// rain drop collection
	private ArrayList<RainDrop> rain_drops = null;
	
	// rate of rain drop generation in rain drops per second
	private int num_drops = 150;
		
	// color
	private final Vector4f color = new Vector4f(27/255f, 132/255f, 186/255f,
			1f);
	
	// position
	private Rectanglef rect = new Rectanglef(Game.UPPER_VIEW);
	private final float DEPTH = -1f; 
	
	// constructor
	public Sky() {
		rain_drops = new ArrayList<RainDrop>();
	}
	
	@Override
	public void init() {
		rect.setHeight(rect.getHeight() - 150);
	}
	
	@Override
	public void update(int delta) {
		// add new rain drops
		float prob_drop = num_drops*delta/1000f;
		while(Math.random() <= prob_drop) {			
			--prob_drop;
			generateRainDrop();			
		}
		
		// update rain drops
		for(int i=rain_drops.size()-1; i>=0; --i) {
			if(rain_drops.get(i).isDisposable()) {
				rain_drops.remove(i);
			}
			else {
				rain_drops.get(i).update(delta);
			}
		}
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL.glColor4f(color);
			GL11.glVertex3f(rect.getX(), rect.getY(), DEPTH);
			GL11.glVertex3f(rect.getX() + rect.getWidth(), rect.getY(), DEPTH);
			GL11.glVertex3f(rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight(), DEPTH);
			GL11.glVertex3f(rect.getX(), rect.getY() + rect.getHeight(), DEPTH);
		GL11.glEnd();
		
		Iterator<RainDrop> it = rain_drops.iterator();
		while(it.hasNext()) {
			it.next().render();
		}
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
		return true;
	}
	
	/**
	 * Generates a rain drop, initializes it, and adds it to the rain drop 
	 * collection.
	 */
	private void generateRainDrop() {
		RainDrop rain_drop = new RainDrop();
		rain_drop.init();
		rain_drops.add(rain_drop);
	}
}

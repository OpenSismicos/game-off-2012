package net.sismicos.verdejo.game.dirt;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector4f;

import net.sismicos.verdejo.game.Component;
import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.game.sky.RainDrop;
import net.sismicos.verdejo.util.GL;
import net.sismicos.verdejo.util.Rectanglef;

public class Dirt extends Component {

	// color
	private final Vector4f color = new Vector4f(64/256f, 59/256f, 59/256f, 1f);

	// position
	private Rectanglef rect = new Rectanglef(Game.LOWER_VIEW);
	private final float depth = 3f; 
	
	// sky to dirt absorption rate
	private static final float absorption_rate = .0025f;
	
	// constructor
	public Dirt() {}
	
	@Override
	public void init() {
		rect.setY(rect.getY() + 150);
		rect.setHeight(rect.getHeight() - 150);
	}
	
	@Override
	public void update(int delta) {
		// add new water drops
		float prob_drop = Game.getRainRate()*absorption_rate*delta/1000f;
		while(Math.random() <= prob_drop) {
			--prob_drop;
			generateWaterDrop();
		}
	}

	@Override
	public void render() {
		// draw dirt
		GL.glDrawRectangle(rect, depth, color);
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
	
	public void generateWaterDrop() {
		Game.addUIComponent(new WaterDrop());		
	}
}

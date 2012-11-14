package net.sismicos.verdejo.util;

import net.sismicos.verdejo.logger.Logger;

import org.lwjgl.util.vector.Vector3f;

public class ColorDispatcher {
	// color assigned to no object
	public static final Vector3f FAIL_COLOR = new Vector3f(0f, 0f, 0f);
	
	// current regular and volatile colors
	private static int current_color = 1;
	private static int current_volatile = 1;
	
	// maximum colors permitted
	private static final int MAX_COLOR = 255;
	private static final int MAX_VOLATILE = 255*256; 
	
	/**
	 * Reserve a color for collision identification.
	 * @return Reserved color.
	 */
	public static Vector3f reserveColor() {
		if(current_color == MAX_COLOR) {
			Logger.printWarn("Maximum number of non-volatile objects reached!");
		}
		return new Vector3f(current_color++/255f, 0f, 0f); 
	}
	
	/**
	 * Reserve a volatile color for collision identification. This color is 
	 * meant to be reused in the future. 
	 * @return Reserved color.
	 */
	public static Vector3f reserveVolatileColor() {
		int first = current_volatile % 256;
		int second = (int) Math.floor(current_volatile/256f);
		current_volatile = (++current_volatile) % MAX_VOLATILE; 
		return new Vector3f(0f, first/255f, second/255f);
	}
	
	/**
	 * Compare two colors.
	 * @param color1
	 * @param color2
	 * @return True if both colors are the same, false otherwise.
	 */
	public static boolean compareColors(Vector3f color1, Vector3f color2) {
		return (color1.getX() == color2.getX() &&
				color1.getY() == color2.getY() &&
				color1.getZ() == color2.getZ());
	}
}

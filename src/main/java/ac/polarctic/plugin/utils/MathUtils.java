package ac.polarctic.plugin.utils;

import org.bukkit.util.Vector;

public class MathUtils {

    public static int floor(final double var0) {
        final int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }
    
    public static double getHorizontalDistanceToHitBox(Vector from, Vector to) {
    	from.setY(0);
    	to.setY(0);
		double nearestX = clamp(from.getX(), to.getX() - 0.4, to.getX() + 0.4);
		double nearestZ = clamp(from.getZ(), to.getZ() - 0.4, to.getZ() + 0.4);

		double distX = from.getX() - nearestX;
		double distZ = from.getZ() - nearestZ;

		return Math.hypot(distX, distZ);
	}

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

}

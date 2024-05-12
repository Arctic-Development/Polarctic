package ac.polarctic.plugin.check.impl.emulation.util.impl;

import ac.polarctic.plugin.check.impl.emulation.util.ClientMath;
import ac.polarctic.plugin.utilities.mcp.MathHelper;

/**
 * @author Salers
 * made on ac.polarctic.plugin.check.impl.emulation.util.impl
 */
public class VanillaMathImpl implements ClientMath {

    @Override
    public float sin(float value) {
        return MathHelper.sin(value);
    }

    @Override
    public float cos(float value) {
        return MathHelper.cos(value);
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt(f);
    }
}

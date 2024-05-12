package ac.polarctic.plugin.check.impl.emulation;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.annotation.Experimental;
import ac.polarctic.plugin.check.api.annotation.Testing;
import ac.polarctic.plugin.check.api.types.MovementCheck;
import ac.polarctic.plugin.check.impl.emulation.util.ClientMath;
import ac.polarctic.plugin.check.impl.emulation.util.impl.FastMathImpl;
import ac.polarctic.plugin.check.impl.emulation.util.impl.VanillaMathImpl;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.AttributeTracker;
import ac.polarctic.plugin.data.tracker.PositionTracker;
import ac.polarctic.plugin.data.tracker.VelocityTracker;
import ac.polarctic.plugin.utilities.mcp.MathHelper;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Salers & Mexify
 * made on Hydro AC
 */

@Experimental
@CheckInformation(name = "Movement Validation XZ", minVL = 15)
public class EmulationHorizontal extends Check implements MovementCheck {

    private final VanillaMathImpl vanillaMath = new VanillaMathImpl();

    private final FastMathImpl fastMath = new FastMathImpl();

    private static final boolean[] BOOLEANS = {true, false};

    public EmulationHorizontal(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onMove() {
        double motionXZ = positionTracker.getLastDeltaXZ();
        double lowestMatch = Double.MAX_VALUE;
        boolean isVelocity = false;

        final double deltaXZ = positionTracker.getDeltaXZ();
        final float tickFriction = positionTracker.isLastLastClientOnGround() ?
                0.91F * positionTracker.getLastLastSlipperiness() : 0.91F;

        // Applying friction
        final double lastDeltaX = positionTracker.getLastDeltaX() * tickFriction;
        final double lastDeltaZ = positionTracker.getLastDeltaZ() * tickFriction;

        // Loop through various combinations of parameters using nested loops
        for (int forw = -1; forw < 2; forw++) {
            for (int stra = -1; stra < 2; stra++) {
                for (boolean attacking : BOOLEANS) {
                    for (boolean velocity : BOOLEANS) {
                        for (boolean using : BOOLEANS) {
                            for (boolean fastMath : BOOLEANS) {
                                for (boolean sprinting : BOOLEANS) {
                                    for (boolean sneaking : BOOLEANS) {
                                        for (boolean jumping : BOOLEANS) {
                                            for (boolean ground : BOOLEANS) {
                                                /*
                                                 * This section of code involves the calculation of movement parameters and simulation of movement scenarios.
                                                 */
                                                float forward = forw, strafe = stra; // Initialize the movement directions based on the loop iteration

                                                // Select the math utility based on the value of 'fastMath'
                                                ClientMath clientMath = fastMath ? this.fastMath : this.vanillaMath;

                                                double bruteforceX = lastDeltaX;// Initialize 'bruteforceX' with the player's last delta X
                                                double bruteforceZ = lastDeltaZ; // Initialize 'bruteforceZ' with the player's last delta Z

                                                // Update 'bruteforceX' and 'bruteforceZ' if velocity is enabled and there are possible velocities
                                                if (velocity && !velocityTracker.getVelocities().isEmpty()) {
                                                    bruteforceX = velocityTracker.peek().getX();
                                                    bruteforceZ = velocityTracker.peek().getZ();
                                                }

                                                float friction = 0.91F; // Initialize the friction factor

                                                // Apply ground-specific friction using NmsUtil.getBlockFriction() method
                                                if (ground) {
                                                    friction *= positionTracker.getLastSlipperiness();
                                                }

                                                // Make sure 'bruteforceX' and 'bruteforceZ' are not too close to zero
                                                if (Math.abs(bruteforceX) < 0.005D) {
                                                    bruteforceX = 0.0D;
                                                }
                                                if (Math.abs(bruteforceZ) < 0.005D) {
                                                    bruteforceZ = 0.0D;
                                                }

                                                // Various conditions to skip impossible cases based on movement and action states
                                                if (sprinting && sneaking) {
                                                    continue; // Skip iteration if both sprinting and sneaking
                                                }
                                                if (jumping && !ground) {
                                                    continue; // Skip iteration if jumping without being on the ground
                                                }
                                                if (sprinting && forward <= 0) {
                                                    continue; // Skip iteration if sprinting with non-positive forward value
                                                }

                                                // Apply modifiers to 'forward' and 'strafe' based on movement conditions
                                                if (sneaking) {
                                                    forward *= (float) 0.3D; // Reduce forward and strafe if sneaking
                                                    strafe *= (float) 0.3D;
                                                }
                                                if (using) {
                                                    forward *= 0.2D; // Reduce forward and strafe if using an item
                                                    strafe *= 0.2D;
                                                }
                                                if (attacking) {
                                                    bruteforceX *= 0.6D; // Reduce 'bruteforceX' and 'bruteforceZ' if attacking
                                                    bruteforceZ *= 0.6D;
                                                }

                                                // Adjust 'bruteforceX' and 'bruteforceZ' based on jumping and sprinting conditions
                                                if (jumping && sprinting) {
                                                    float f = playerData.getRotationTracker().getYaw() * 0.017453292F;

                                                    bruteforceX -= clientMath.sin(f) * 0.2F; // Adjust 'bruteforceX' based on yaw and jump
                                                    bruteforceZ += clientMath.cos(f) * 0.2F; // Adjust 'bruteforceZ' based on yaw and jump
                                                }

                                                // Apply a slight deceleration to 'forward' and 'strafe'
                                                forward *= 0.98F;
                                                strafe *= 0.98F;

                                                // Calculate moveSpeed and weird factors based on conditions
                                                float weird = 0.16277136F / (friction * friction * friction);
                                                float moveSpeed = ground ? weird * getAiMoveSpeed(sprinting) : (sprinting ? 0.026F : 0.02F);

                                                // Calculate new 'bruteforceX' and 'bruteforceZ' using MovementUtil.moveFlying() method
                                                double[] moveFlying = moveFlying(forward, strafe, moveSpeed, bruteforceX, bruteforceZ, playerData.getRotationTracker().getYaw(), clientMath);

                                                bruteforceX = moveFlying[0]; // Update 'bruteforceX'
                                                bruteforceZ = moveFlying[1]; // Update 'bruteforceZ'

                                                // Calculate offsets between calculated and actual delta X and Z
                                                double offsetX = positionTracker.getDeltaX() - bruteforceX;
                                                double offsetZ = positionTracker.getDeltaZ() - bruteforceZ;

                                                // Calculate squared offset distance
                                                double offset = (offsetX * offsetX) + (offsetZ * offsetZ);

                                                // Update 'lowestMatch' if the current offset is lower
                                                if (offset < lowestMatch) {
                                                    lowestMatch = offset;
                                                    motionXZ = Math.hypot(bruteforceX, bruteforceZ);
                                                    isVelocity = velocity;

                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }

                    }

                }
            }
        }

        isVelocity = isVelocity || velocityTracker.getVelocityTicks() <= 2;

        final double zeroThreeTicks = positionTracker.getLastTicksSincePosition() * 0.03D;
        final double threshold = 1E-5 + zeroThreeTicks;

        final NumberFormat formatter = new DecimalFormat("#.######");
        final boolean invalid = (positionTracker.getDeltaXZ() > motionXZ + threshold && !isVelocity)
                || lowestMatch > threshold * 10D;

        if (invalid && deltaXZ >= 0.1 + zeroThreeTicks && !velocityTracker.isConfirming()) {
            if(++buffer > 2) {
                buffer = Math.min(8, buffer);
                flag("delta=" + formatter.format(deltaXZ) + " motion=" + formatter.format(motionXZ) + " 00three=" + zeroThreeTicks);
            }

        } else buffer = Math.max(0, buffer - 0.05D);

    }

    public double[] moveFlying(float forward, float strafe, float friction, double motionX, double motionZ, float yaw, ClientMath math) {
        float combined = strafe * strafe + forward * forward;

        if (combined >= 1.0E-4F) {
            combined = MathHelper.sqrt_float(combined);

            if (combined < 1.0F) {
                combined = 1.0F;
            }

            combined = friction / combined;

            strafe *= combined;
            forward *= combined;

            float sin = math.sin(yaw * (float) Math.PI / 180.0F);
            float cos = math.cos(yaw * (float) Math.PI / 180.0F);

            motionX += strafe * cos - forward * sin;
            motionZ += forward * cos + strafe * sin;
        }

        return new double[]{
                motionX,
                motionZ
        };
    }

    // Calculate and return the player's AI move speed, considering sprinting and various modifiers
    private float getAiMoveSpeed(boolean sprint) {
        AttributeTracker attributeTracker = playerData.getAttributeTracker();

        // Retrieve the base walk speed value from the attribute tracker
        float baseValue = attributeTracker.getWalkSpeed();

        // Increase base walk speed if sprinting is enabled
        if (sprint) {
            baseValue *= 1.3F;
        }

        int speed = attributeTracker.getSpeedBoost(); // Get the speed boost attribute value
        int slow = attributeTracker.getSlowness();    // Get the slowness attribute value

        // Adjust the base walk speed based on speed boost and slowness attributes
        baseValue += baseValue * speed * 0.2F;
        baseValue += baseValue * slow * -0.15F;

        return baseValue; // Return the final adjusted AI move speed
    }
}

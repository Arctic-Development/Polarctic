package ac.polarctic.plugin.check.impl.move;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.annotation.Experimental;
import ac.polarctic.plugin.check.api.types.MovementCheck;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.PositionTracker;
import org.bukkit.Bukkit;

/**
 * @author Salers
 * made on ac.polarctic.plugin.check.impl.move
 */

@Experimental
@CheckInformation(name = "Movement Limitation (HORIZONTAL)", minVL = 10)
public class MoveXZ extends Check implements MovementCheck {
    /*
    TEST CHECK
     */
    private final PositionTracker positionTracker = playerData.getPositionTracker();

    public MoveXZ(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onMove() {
        final boolean clientOnGround = positionTracker.isClientOnGround();
        final boolean lastClientOnGround = positionTracker.isLastClientOnGround();

        final float friction = clientOnGround && lastClientOnGround ? 0.91F * positionTracker.getLastSlipperiness() : 0.91F;
        final boolean jump = !clientOnGround && lastClientOnGround && positionTracker.getDeltaY() != 0.0D;
        final boolean landDesync = clientOnGround && !lastClientOnGround;

        final double momentum = (lastClientOnGround ? 0.13F : 0.026F) + (landDesync ? 0.15F : 0.F) + (jump ? 0.2F : 0.F) ;
        final double threshold = momentum + positionTracker.getTicksSincePosition() * 0.03D;

        final double motion = this.positionTracker.getLastDeltaXZ() * friction ;
        final double offset = Math.abs(motion - positionTracker.getDeltaXZ()) - threshold;

        final boolean invalid = offset > 1E-4 && Math.abs(positionTracker.getDeltaXZ()) >= 0.1D;

        if (invalid) {
            if(++buffer > 3) {
                flag("motion=" + motion +  " threshold=" + threshold + " offset=" + offset);
                buffer = 0;
            }
        } else buffer = Math.max(0, buffer - 0.2D);

    }
}

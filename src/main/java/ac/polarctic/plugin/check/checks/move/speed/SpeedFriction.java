package ac.polarctic.plugin.check.checks.move.speed;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedFriction extends Check {

	private double lastDeltaXZ;
	private boolean wasOnGround;
	private int threshold;

	public SpeedFriction(DataPlayer data, String name, String type) {
		super(data, name, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMove(PlayerMoveEvent event) {
		double deltaXZ = Math.hypot((event.getTo().getX() - event.getFrom().getX()),
				(event.getTo().getZ() - event.getFrom().getZ()));
		double lastDeltaXZ = this.lastDeltaXZ;
		this.lastDeltaXZ = deltaXZ;
		double predictedDeltaXZ = lastDeltaXZ * 0.91F;
		double result = Math.abs(deltaXZ - predictedDeltaXZ) * 100;
		boolean isOnGround = event.getPlayer().isOnGround();
		boolean wasOnGround = this.wasOnGround;
		this.wasOnGround = isOnGround;
		if (!wasOnGround && !isOnGround) {
			if (result >= 2.61D) {
				if (++this.threshold > 4) {
					fail("r=" + (float) result);
				}
			}else this.threshold *= 0.95;
		}

	}

}

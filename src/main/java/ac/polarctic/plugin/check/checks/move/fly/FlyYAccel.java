package ac.polarctic.plugin.check.checks.move.fly;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import ac.polarctic.plugin.utils.LocationUtils;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyYAccel extends Check {

	private double lastDeltaY; // the previous deltaY...
	private int airTicks; // since how many ticks player is in air
	private int threshold;

	public FlyYAccel(DataPlayer data, String name, String type) {
		super(data, name, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMove(PlayerMoveEvent event) {
		double deltaY = event.getTo().getY() - event.getFrom().getY();
		double lastDeltaY = this.lastDeltaY;
		this.lastDeltaY = deltaY;
		double accelY = Math.abs(deltaY - lastDeltaY);
		if (LocationUtils.isCloseToGround(event.getTo())) {
			this.airTicks = 0;
		} else
			this.airTicks++;
		if (airTicks > 5 && !LocationUtils.isAtEdgeOfABlock(event.getPlayer())
				&& !LocationUtils.isCollidingWithClimbable(event.getPlayer())
				&& !LocationUtils.isNearBoat(event.getPlayer()) && !LocationUtils.isInLiquid(event.getPlayer())) {

			if (accelY < 0.01 || Double.toString(accelY).contains("E")) {
				if (++this.threshold > 5) {
					fail("aY=" + (float) accelY + " tH=" + this.threshold);
				}

			} else
				this.threshold *= 0.75;

		}

	}

}

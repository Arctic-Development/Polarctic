package ac.polarctic.plugin.check.checks.move.fly;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import ac.polarctic.plugin.utils.LocationUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyClimb extends Check{
	
	private double lastDeltaY; // the previous deltaY...
	private int airTicks; // since how many ticks player is in air
	private int threshold;

	public FlyClimb(DataPlayer data, String name, String type) {
		super(data, name, type);
		
	}
	
	@EventHandler 
	public void onMove(PlayerMoveEvent event) {
		double deltaY = event.getTo().getY() - event.getFrom().getY();
		double lastDeltaY = this.lastDeltaY;
		this.lastDeltaY = deltaY;
		if (LocationUtils.isCloseToGround(event.getTo())) {
			this.airTicks = 0;
		} else
			this.airTicks++;
		if (airTicks > 15 && !LocationUtils.isAtEdgeOfABlock(event.getPlayer())
				&& !LocationUtils.isCollidingWithClimbable(event.getPlayer())
				&& !LocationUtils.isNearBoat(event.getPlayer()) && !LocationUtils.isInLiquid(event.getPlayer())) {
			
			if(deltaY > lastDeltaY) {
					fail("dY=" + (float) deltaY + " lDY=" + (float) lastDeltaY);
				}
			
		}
		
	
	}
	
	
	
	

}

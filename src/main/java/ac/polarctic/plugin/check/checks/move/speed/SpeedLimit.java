package ac.polarctic.plugin.check.checks.move.speed;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedLimit extends Check {
	
	private int groundTicks,threshold;

	public SpeedLimit(DataPlayer data, String name, String type) {
		super(data, name, type);
	}
	
	@Override
	public void onMove(PlayerMoveEvent event) {
		double deltaXZ = Math.hypot((event.getTo().getX() - event.getFrom().getX()),
				(event.getTo().getZ() - event.getFrom().getZ()));
		double limit;

		if(event.getPlayer().isOnGround()) {
			this.groundTicks = 0;
		}else this.groundTicks++;
		
		if(groundTicks > 5) {
			limit = 0.443D;
		}else limit = 0.63D;
		
		if(!event.getPlayer().getLocation().add(0,0.899,0).getBlock().isEmpty()) {
                limit = 1.31;
        }

		if (event.getPlayer().getLocation().add(0, -1.25, 0).getBlock().getType()
				== Material.ICE ||event.getPlayer().getLocation().add(0,-1.25,0).
				getBlock().getType() == Material.PACKED_ICE) {
			limit += 0.6;
		}

		limit += event.getPlayer().getWalkSpeed() > 0.2 ? 0 :
				event.getPlayer().getWalkSpeed();
		
 		if(deltaXZ > limit) {
 			if(++this.threshold > 4) {
 				fail("l=" + limit + " dXZ=" + (float) deltaXZ);
 			}
 			
 		}else this.threshold *= 0.75;
		
		
	}

}

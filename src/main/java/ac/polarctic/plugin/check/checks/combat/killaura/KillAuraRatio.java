package ac.polarctic.plugin.check.checks.combat.killaura;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class KillAuraRatio extends Check {
	
	private int attacks,armAnimations,threshold;
	private List<Location> pastVictimLocations = new ArrayList<>();

	public KillAuraRatio(DataPlayer data, String name, String type) {
		super(data, name, type);
		
	}

	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if (packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if (packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) {
				this.attacks++;
				Entity victim = packetEvent.getPacket().getEntityModifier(data.getPlayer().getWorld()).read(0);
				if(this.pastVictimLocations.size()  > 100 ) this.pastVictimLocations.clear();
				this.pastVictimLocations.add(victim.getLocation());
				

			}
		}else if(packetEvent.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
		
			if(++this.armAnimations >= 100) {
				if(this.pastVictimLocations.size() < 3) return;
				
				if(this.pastVictimLocations.get(0).getPitch() != this.pastVictimLocations.get(3).getPitch()) {
					data.getPlayer().sendMessage("attacks=" + this.attacks);
					if(this.attacks > 85) {
						fail("a=" + this.attacks);
					
					
					}
					
				}
				this.armAnimations = this.attacks = 0;	
			}
		}
	}

}

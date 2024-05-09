package ac.polarctic.plugin.check.checks.combat.killaura;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;

public class KillAuraSwing extends Check {
	
	private int attacks;
	

	public KillAuraSwing(DataPlayer data, String name, String type) {
		super(data, name, type);
	}

	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if (packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if (packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) {
				if(++this.attacks > 4) {
					fail("a=" + this.attacks);
				}
				
			}
		}else if(packetEvent.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
			this.attacks = 0;
		}
	}

}

package ac.polarctic.plugin.check.checks.combat.aimassist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.event.player.PlayerMoveEvent;

public class AimAssistPitch extends Check {
	
	private double lastDeltaYaw;
	
	private int hitTicks;

	public AimAssistPitch(DataPlayer data, String name, String type) {
		super(data, name, type);
	
	}
	
	@Override
	public void onMove(PlayerMoveEvent event) {
		this.hitTicks++;
		double deltaYaw = (event.getTo().getYaw() - event.getFrom().getYaw()) % 360;
		double deltaPitch = event.getTo().getPitch() - event.getFrom().getPitch();
		double lastDeltaYaw = this.lastDeltaYaw;
		this.lastDeltaYaw = deltaYaw;
		double yawAccel = Math.abs(deltaYaw - lastDeltaYaw);
		if(hitTicks < 2) {
			if(yawAccel > 27.5 && deltaPitch == 0) {
				fail("yWA=" + (float) yawAccel + " dP=" + (float) deltaPitch);
			}
			
		}
	}
	
	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if(packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if(packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) {
				this.hitTicks = 0;
			}
		}
	}

}

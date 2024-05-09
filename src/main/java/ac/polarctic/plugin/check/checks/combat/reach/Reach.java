package ac.polarctic.plugin.check.checks.combat.reach;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import ac.polarctic.plugin.utils.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Reach extends Check {

	private int ping,threshold;
	private long lastServerKeepAlive;
	

	private List<Vector> ray(int pingInTicks) {
		List<Vector> toReturn = new ArrayList<>();
		for (int range = 0; range < 2; range++) {
			toReturn.add(data.getPastVictimVectors().get(pingInTicks + range));
		}
		return toReturn;

	}

	public Reach(DataPlayer data, String name, String type) {
		super(data, name, type);
	}

	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if (packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if (packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) {
				LivingEntity living = (LivingEntity) packetEvent.getPacket()
						.getEntityModifier(data.getPlayer().getWorld()).read(0);
				data.setVictim(living);
				List<Vector> fromRay = this.ray(ping / 50);
				Vector attacker = data.getPlayer().getLocation().toVector();
				float distance = (float) fromRay.stream().mapToDouble(vector -> MathUtils.getHorizontalDistanceToHitBox(vector, attacker)).min().orElse(1);
				if(distance > 3.3f) {
					if(++this.threshold > 10) {
						fail("d=" + distance);
					}
					
				}else this.threshold *= 0.99;
			}
		}else if(packetEvent.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
			this.ping =  (int) ((int) System.currentTimeMillis() - this.lastServerKeepAlive);
		}

	}
	
	@Override
	public void onPacketSend(PacketEvent event) {
		if(event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
			this.lastServerKeepAlive = System.currentTimeMillis();
		}else if(event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {
			if(data.getVictim() != null) {
				if(data.getPastVictimVectors().size() > 20) data.getPastVictimVectors().clear();
				data.getPastVictimVectors().add(data.getVictim().getLocation().toVector());
			}
		}
	}

}

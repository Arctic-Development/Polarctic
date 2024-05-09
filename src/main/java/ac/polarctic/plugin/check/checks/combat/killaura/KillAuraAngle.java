package ac.polarctic.plugin.check.checks.combat.killaura;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KillAuraAngle extends Check {

	public KillAuraAngle(DataPlayer data, String name, String type) {
		super(data, name, type);
	}

	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if (packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if (packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) {
				LivingEntity victim = data.getVictim();
				Player damager = data.getPlayer();
				double x1 = damager.getEyeLocation().getX();
				double z1 = damager.getEyeLocation().getZ();
				double vdX = damager.getEyeLocation().getDirection().getX();
				double vdZ = damager.getEyeLocation().getDirection().getZ();
				double x2 = victim.getLocation().getX();
				double z2 = victim.getLocation().getZ();

				double dotProduct = vdX * (x2 - x1) + vdZ * (z2 - z1);
				double avMod = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
				double vdMod = Math.sqrt(vdX * vdX + vdZ * vdZ);

				double cosAngle = dotProduct / (avMod * vdMod);
				int angle = (int) Math.toDegrees(Math.acos(cosAngle));
				
				if((victim.getLocation().toVector().distance(damager.getLocation().toVector()) - 0.58) > 1.5) {
					if(angle > 50) {
						fail("a=" + angle);
					}
				}
 
			}

		}
	}

}

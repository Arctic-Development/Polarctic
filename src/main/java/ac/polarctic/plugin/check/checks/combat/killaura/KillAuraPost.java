package ac.polarctic.plugin.check.checks.combat.killaura;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.DataPlayer;
import ac.polarctic.plugin.utils.PlayerUtils;

public class KillAuraPost extends Check {

	private long lastFlying;
	private int threshold;

	public KillAuraPost(DataPlayer data, String name, String type) {
		super(data, name, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPacketReceive(PacketEvent packetEvent) {
		if (packetEvent.getPacketType() == PacketType.Play.Client.LOOK
				|| packetEvent.getPacketType() == PacketType.Play.Client.POSITION
				|| packetEvent.getPacketType() == PacketType.Play.Client.POSITION_LOOK
				|| packetEvent.getPacketType() == PacketType.Play.Client.FLYING) { // These packet are sent every ticks.
			this.lastFlying = System.currentTimeMillis();

		} else if (packetEvent.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
			if (packetEvent.getPacket().getEntityUseActions().read(0) == EntityUseAction.ATTACK) { // Checking if player
																									// is attacking
				long delay = System.currentTimeMillis() - this.lastFlying; // should be around 40ms
				if (delay < 25L && PlayerUtils.getPingNMS(data.getPlayer()) < 175 /** lag can make falses flags **/
				) {
					if (++this.threshold > 2) {
						fail("d=" + delay + " tH=" + this.threshold);
					}

				} else
					this.threshold *= 0.75;

			}
		}

	}

}

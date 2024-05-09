package ac.polarctic.plugin.check.api.types;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

/**
 * @author Salers
 * made on ac.polarctic.plugin.check.api.types
 */
public abstract class PacketCheck extends Check {

    public PacketCheck(PlayerData playerData) {
        super(playerData);
    }

    public abstract void onPacket(final PacketReceiveEvent event);
}

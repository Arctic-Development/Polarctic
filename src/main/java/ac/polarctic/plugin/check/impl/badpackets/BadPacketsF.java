package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

/**
 * account for teleports.
 */
@CheckInformation(name = "Bad Packets F", minVL = 1)
public class BadPacketsF extends PacketCheck {
    private boolean sent = false;

    public BadPacketsF(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            if (sent)
                flag("Duplicate Entity Action");
        } else if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction entityAction = new WrapperPlayClientEntityAction(event);

            if (entityAction.getAction().name().toLowerCase().contains("sprint") || entityAction.getAction().name().toLowerCase().contains("sneak"))
                sent = true;
        } else if (PacketUtil.isFlying(event.getPacketType())) {
            sent = false;
        }
    }
}

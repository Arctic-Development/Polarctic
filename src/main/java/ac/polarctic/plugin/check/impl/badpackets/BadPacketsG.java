package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

/**
 * add actual tracking of server held item packets for the player.
 */
@CheckInformation(name = "Bad Packets G", minVL = 2)
public class BadPacketsG extends PacketCheck {
    private int lastSlot = -1;

    public BadPacketsG(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            WrapperPlayClientHeldItemChange itemChange = new WrapperPlayClientHeldItemChange(event);

            int slot = itemChange.getSlot();

            if (lastSlot == slot && ++buffer > 3)
                flag(String.format("S %s", slot));
            else
                buffer = Math.max(0, buffer - 0.15D);

            lastSlot = slot;
        }
    }
}

package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;

@CheckInformation(name = "Bad Packets E", minVL = 1)
public class BadPacketsE extends PacketCheck {
    public BadPacketsE(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
            WrapperPlayClientKeepAlive keepAlive = new WrapperPlayClientKeepAlive(event);

            long id = keepAlive.getId();

            if (id == 0)
                flag("Spoofed Keep Alive");
        }
    }
}

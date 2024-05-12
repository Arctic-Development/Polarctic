package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;

@CheckInformation(name = "Bad Packets C", minVL = 1)
public class BadPacketsC extends PacketCheck {
    public BadPacketsC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            WrapperPlayClientSteerVehicle steerVehicle = new WrapperPlayClientSteerVehicle(event);

            float forward = Math.abs(steerVehicle.getForward());
            float side = Math.abs(steerVehicle.getSideways());

            boolean invalid = forward > 0.98F || side > 0.98F;

            if (invalid)
                flag(String.format("F %.2f S %.2f", forward, side));
        }
    }
}

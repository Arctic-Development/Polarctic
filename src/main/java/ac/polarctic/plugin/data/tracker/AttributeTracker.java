package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import lombok.Getter;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */
@Getter
public class AttributeTracker extends PlayerTracker {

    private float walkSpeed = 0.1F;

    private int jumpBoost, speedBoost, slowness;

    private boolean flightAllowed, creativeMode, flying;

    public AttributeTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {

    }

    @Override
    public void onSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) {
            final WrapperPlayServerEntityEffect entityEffect = new WrapperPlayServerEntityEffect(event);

            if (entityEffect.getEntityId() != data.getPlayer().getEntityId())
                return;

            final int amplifier = entityEffect.getEffectAmplifier();

            data.getTransactionConfirmatory().confirmPre(() -> {
                switch (entityEffect.getPotionType().getId(data.getUser().getClientVersion())) {
                    case 1: {
                        speedBoost = amplifier + 1;
                        break;
                    }

                    case 2: {
                        slowness = amplifier + 1;
                        break;
                    }

                    case 8: {
                        jumpBoost = amplifier + 1;
                        break;
                    }
                }
            });
        } else if (event.getPacketType() == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            final WrapperPlayServerRemoveEntityEffect removeEntityEffect = new WrapperPlayServerRemoveEntityEffect(event);

            if (removeEntityEffect.getEntityId() != data.getPlayer().getEntityId())
                return;

            data.getTransactionConfirmatory().confirmPre(() -> {
                switch (removeEntityEffect.getPotionType().getId(data.getUser().getClientVersion())) {
                    case 1: {
                        speedBoost = 0;
                        break;
                    }

                    case 2: {
                        slowness = 0;
                        break;
                    }

                    case 8: {
                        jumpBoost = 0;
                        break;
                    }
                }
            });
        } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_ABILITIES) {
            final WrapperPlayServerPlayerAbilities wrapper = new WrapperPlayServerPlayerAbilities(event);

            data.getTransactionConfirmatory().confirmPre(() -> {
                flightAllowed = wrapper.isFlightAllowed();
                creativeMode = wrapper.isInCreativeMode();
                flying = wrapper.isFlying();
                walkSpeed = wrapper.getFOVModifier();
            });
        }
    }


    @Override
    public void onPostReceive(PacketReceiveEvent event) {

    }
}

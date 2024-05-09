package ac.polarctic.plugin.check.api.types.order;

import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

/**
 * @author Salers
 * made on ac.polarctic.plugin.check.api
 */
public abstract class PacketOrderCheck extends PacketCheck {

    private final PacketTypeCommon before, target, after;
    private boolean sentBefore, sentTarget, sentAfter;

    public PacketOrderCheck(PlayerData playerData, PacketTypeCommon before, PacketTypeCommon target, PacketTypeCommon after) {
        super(playerData);
        this.before = before;
        this.target = target;
        this.after = after;
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == this.before) {
            this.sentBefore = true;
            if(this.sentTarget || this.sentAfter) {
                this.handleInvalidOrder();

            }
        } else if(event.getPacketType() == this.target) {
            this.sentTarget = true;
            if(!this.sentBefore || this.sentAfter) {
                this.handleInvalidOrder();
            }
        } else if(event.getPacketType() == after) {
            this.sentAfter = true;
            if(!sentBefore || !sentTarget) {
                this.handleInvalidOrder();
            }
        }
    }

    private void handleInvalidOrder() {
        this.reset();
    }

    private void reset() {
        sentBefore = sentTarget = sentAfter = false;
    }
}

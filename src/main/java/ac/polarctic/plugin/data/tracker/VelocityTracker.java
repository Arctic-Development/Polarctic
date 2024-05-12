package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */

@Getter
public class VelocityTracker extends PlayerTracker {

    private boolean confirming = false;
    private final Deque<Vector3d> velocities = new ArrayDeque<>();
    private Vector3d lastVelocity = null;
    private int velocityTicks;

    public VelocityTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {
        if (!PacketUtil.isFlying(event.getPacketType())) return;
        this.velocityTicks++;
    }

    @Override
    public void onSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            final WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity(event);

            if (wrapper.getEntityId() != data.getPlayer().getEntityId()) return;

            data.getTransactionConfirmatory().confirmPre(() -> {
                this.velocityTicks = 0;
                this.confirming = true;
                this.velocities.add(wrapper.getVelocity());
            });

            data.getTransactionConfirmatory().confirmPost(() -> {
                this.confirming = false;
                if (this.velocities.size() > 1) {
                    this.lastVelocity = this.velocities.removeFirst();
                }
            });
        }

    }

    @Override
    public void onPostReceive(PacketReceiveEvent event) {
        if (!PacketUtil.isFlying(event.getPacketType())) return;

        if (confirming && lastVelocity != null) {
         //   this.velocities.add(this.lastVelocity);
        }

    }

    public Vector3d peek() {
        return this.velocities.peek();
    }
}

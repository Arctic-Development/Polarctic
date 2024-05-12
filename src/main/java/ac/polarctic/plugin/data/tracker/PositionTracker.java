package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import ac.polarctic.plugin.utilities.PacketUtil;
import ac.polarctic.plugin.utilities.mcp.MathHelper;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import lombok.Getter;
import org.bukkit.block.Block;


/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */

@Getter
public class PositionTracker extends PlayerTracker {

    private double x, y, z, lastX, lastY, lastZ;
    private double deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;
    private int ticksSincePosition, lastTicksSincePosition;
    private boolean clientOnGround, lastClientOnGround, lastLastClientOnGround;
    private float slipperiness, lastSlipperiness, lastLastSlipperiness;

    public PositionTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {
        if(!PacketUtil.isFlying(event.getPacketType())) return;

        final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

       this.lastX = this.x;
       this.lastY = this.y;
       this.lastZ = this.z;

       this.lastDeltaX = this.deltaX;
       this.lastDeltaY = this.deltaY;
       this.lastDeltaZ = this.deltaZ;

       this.lastDeltaXZ = this.deltaXZ;

       this.lastTicksSincePosition = this.ticksSincePosition;
       this.ticksSincePosition++;

       if(PacketUtil.isPosition(event.getPacketType())) {
           final Location location = wrapper.getLocation();
           this.ticksSincePosition = 0;

           this.x = location.getX();
           this.y = location.getY();
           this.z = location.getZ();

           this.deltaX = this.x - this.lastX;
           this.deltaY = this.y - this.lastY;
           this.deltaZ = this.z - this.lastZ;

           this.deltaXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
       }

       this.lastLastClientOnGround = this.lastClientOnGround;
       this.lastClientOnGround = this.clientOnGround;
       this.clientOnGround = wrapper.isOnGround();

        this.lastLastSlipperiness = this.lastSlipperiness;
        this.lastSlipperiness = this.slipperiness;
        this.slipperiness = getBlockFriction();
    }

    private float getBlockFriction() {
        Block block = data.getPlayer().getWorld().getBlockAt(
                MathHelper.floor_double(this.x),
                MathHelper.floor_double(this.y) - 1,
                MathHelper.floor_double(this.z)
        );

        switch (block.getType()) {
            case SLIME_BLOCK:
                return 0.8F;
            case ICE:
            case PACKED_ICE:
                return 0.98F;
        }

        return 0.6F;
    }

    @Override
    public void onSend(PacketSendEvent event) {

    }

    @Override
    public void onPostReceive(PacketReceiveEvent event) {

    }

}

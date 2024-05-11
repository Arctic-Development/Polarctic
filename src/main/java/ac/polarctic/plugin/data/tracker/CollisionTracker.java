package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */
public class CollisionTracker extends PlayerTracker {

    public CollisionTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {

    }

    @Override
    public void onSend(PacketSendEvent event) {

    }

    @Override
    public void onPostReceive(PacketReceiveEvent event) {

    }
}

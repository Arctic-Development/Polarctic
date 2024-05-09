package ac.polarctic.plugin.data.tracker.api;

import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */
public abstract class PlayerTracker {

    public final PlayerData data;

    public PlayerTracker(PlayerData data) {
        this.data = data;
        this.data.getPlayerTrackers().add(this);
    }

    public abstract void onReceive(final PacketReceiveEvent event);

    public abstract void onSend(final PacketSendEvent event);

    public abstract void onPostReceive(final PacketReceiveEvent event);
}

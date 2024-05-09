package ac.polarctic.plugin.data.processor;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.processor.api.IProcessor;
import ac.polarctic.plugin.data.tracker.PositionTracker;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.processor
 */
public class TrackerProcessor extends IProcessor {

    private final List<PlayerTracker> playerTrackers = playerData.getPlayerTrackers();

    public TrackerProcessor(PlayerData playerData) {
        super(playerData);

    }

    @Override
    public void onReceive(PacketReceiveEvent event) {
        playerTrackers.forEach(playerTracker -> playerTracker.onReceive(event));
    }

    @Override
    public void onSend(PacketSendEvent event) {
        playerTrackers.forEach(playerTracker -> playerTracker.onSend(event));
    }

    public void handlePostCheckReceive(PacketReceiveEvent event) {
        playerTrackers.forEach(playerTracker -> playerTracker.onPostReceive(event));
    }

}

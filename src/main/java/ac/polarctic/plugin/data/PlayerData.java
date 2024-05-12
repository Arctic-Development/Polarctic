package ac.polarctic.plugin.data;

import ac.polarctic.plugin.data.processor.CheckProcessor;
import ac.polarctic.plugin.data.processor.TrackerProcessor;
import ac.polarctic.plugin.data.tracker.ActionTracker;
import ac.polarctic.plugin.data.tracker.AttributeTracker;
import ac.polarctic.plugin.data.tracker.PositionTracker;
import ac.polarctic.plugin.data.tracker.RotationTracker;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data
 */

@Data
public class PlayerData {

    private final UUID uuid;
    private final Player player;
    private final User user;

    private final List<PlayerTracker> playerTrackers = new ArrayList<>();

    private final PositionTracker positionTracker = new PositionTracker(this);
    private final AttributeTracker attributeTracker = new AttributeTracker(this);
    private final RotationTracker rotationTracker = new RotationTracker(this);
    private final ActionTracker actionTracker = new ActionTracker(this);

    private final CheckProcessor checkProcessor = new CheckProcessor(this);
    private final TrackerProcessor trackerProcessor = new TrackerProcessor(this);

    private final TransactionConfirmatory transactionConfirmatory = new TransactionConfirmatory(this);



    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.user = PacketEvents.getAPI().getPlayerManager().getUser(player);
    }

    public void onPacket(PacketReceiveEvent event) {
        trackerProcessor.onReceive(event);
        checkProcessor.onReceive(event);
        trackerProcessor.handlePostCheckReceive(event);
    }

    public void onPacket(PacketSendEvent event) {
        trackerProcessor.onSend(event);

    }




}

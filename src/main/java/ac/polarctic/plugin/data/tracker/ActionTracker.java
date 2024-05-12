package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import lombok.Getter;

@Getter
public class ActionTracker extends PlayerTracker {
    private boolean digging;

    public ActionTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging playerDigging = new WrapperPlayClientPlayerDigging(event);

            //just an example, needs to be remade.
            switch (playerDigging.getAction()) {
                case START_DIGGING:
                    digging = true;
                    break;
                case CANCELLED_DIGGING:
                case FINISHED_DIGGING:
                    digging = false;
                    break;
            }
        }
    }

    @Override
    public void onSend(PacketSendEvent event) {}

    @Override
    public void onPostReceive(PacketReceiveEvent event) {}
}

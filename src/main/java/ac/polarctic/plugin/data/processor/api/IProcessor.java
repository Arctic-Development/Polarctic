package ac.polarctic.plugin.data.processor.api;

import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

public abstract class IProcessor {

    public final PlayerData playerData;

    protected IProcessor(PlayerData playerData) {
        this.playerData = playerData;
    }

    public abstract void onReceive(final PacketReceiveEvent event);

    public abstract void onSend(final PacketSendEvent event);
}

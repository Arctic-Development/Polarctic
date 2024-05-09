package ac.polarctic.plugin.listener;

import ac.polarctic.plugin.Global;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

/**
 * @author Salers
 * made on ac.polarctic.plugin.listener
 */
public class PacketEventsListener extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(!Global.INSTANCE.getPlayerDataManager().has(event.getUser())) return;
        final PlayerData data = Global.INSTANCE.getPlayerDataManager().get(event.getUser());

        if(data != null) {
            data.onPacket(event);
        }


    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(!Global.INSTANCE.getPlayerDataManager().has(event.getUser())) return;
        final PlayerData data = Global.INSTANCE.getPlayerDataManager().get(event.getUser());

        if(data != null) {
            data.onPacket(event);
        }
    }


}

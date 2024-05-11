package ac.polarctic.plugin.listener;

import ac.polarctic.plugin.Global;
import ac.polarctic.plugin.data.PlayerData;
import dev.thomazz.pledge.pinger.frame.FrameClientPingerListener;
import dev.thomazz.pledge.pinger.frame.data.Frame;
import org.bukkit.entity.Player;

/**
 * @author Salers
 * made on ac.polarctic.plugin.listener
 */

public class PledgeListener implements FrameClientPingerListener {

    @Override
    public void onFrameReceiveStart(Player player, Frame frame) {
        final PlayerData data = Global.INSTANCE.getPlayerDataManager().get(player);
        if (data != null)
            data.getTransactionConfirmatory().onFrameReceiveStart(frame);


    }

    @Override
    public void onFrameReceiveEnd(Player player, Frame frame) {
        final PlayerData data = Global.INSTANCE.getPlayerDataManager().get(player);
        if (data != null)
            data.getTransactionConfirmatory().onFrameReceiveEnd(frame);
    }
}


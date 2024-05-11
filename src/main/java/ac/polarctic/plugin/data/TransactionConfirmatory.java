package ac.polarctic.plugin.data;
import ac.polarctic.plugin.Global;
import ac.polarctic.plugin.data.PlayerData;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.thomazz.pledge.pinger.frame.data.Frame;
import lombok.RequiredArgsConstructor;

/**
 * @author Salers
 * made on ac.polarctic.Global.INSTANCE.data
 */

@RequiredArgsConstructor
public class TransactionConfirmatory {

    private final PlayerData playerData;
    private final Multimap<Short, Runnable> scheduledTransactions = ArrayListMultimap.create();

    public void onFrameReceiveStart(Frame frame){
        short id = (short) frame.getStartId();

        if (scheduledTransactions.containsKey(id)) {
            for (Runnable runnable : scheduledTransactions.removeAll(id)) {
                runnable.run();
            }
        }
    }

    public void onFrameReceiveEnd(Frame frame){
        short id = (short) frame.getEndId();

        if (scheduledTransactions.containsKey(id)) {
            for (Runnable runnable : scheduledTransactions.removeAll(id)) {
                runnable.run();
            }
        }
    }

    public void confirmPre(Runnable runnable) {
        Frame frame = Global.INSTANCE.getFrameClientPinger().getOrCreate(playerData.getPlayer());

        scheduledTransactions.put((short) frame.getStartId(), runnable);
    }

    public void confirmPost(Runnable runnable) {
        Frame frame = Global.INSTANCE.getFrameClientPinger().getOrCreate(playerData.getPlayer());

        scheduledTransactions.put((short) frame.getEndId(), runnable);
    }
}

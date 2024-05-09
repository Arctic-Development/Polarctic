package ac.polarctic.plugin.listener;

import ac.polarctic.plugin.Global;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Salers
 * made on ac.polarctic.plugin.listener
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        Global.INSTANCE.getPlayerDataManager().update(event.getPlayer());
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        Global.INSTANCE.getPlayerDataManager().update(event.getPlayer());
    }
}

package ac.polarctic.plugin.manager;

import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Salers
 * made on ac.polarctic.plugin.manager
 */

public class PlayerDataManager {

    private final Set<PlayerData> playerDatas = new HashSet<>();

    private void add(final Player player) {
        this.playerDatas.add(new PlayerData(player.getUniqueId()));
    }

    public PlayerData get(final User player) {
        return playerDatas.stream().filter(data -> data.getUser().equals(player)).findFirst().get();
    }

    public PlayerData get(final Player player) {
        return playerDatas.stream().filter(data -> data.getPlayer().equals(player)).findFirst().get();
    }

    private void remove(final Player player) {
        this.playerDatas.remove(get(player));
    }

    public boolean has(final Player player) {
        return playerDatas.stream().anyMatch(data -> data.getPlayer().equals(player));
    }


    public boolean has(final User player) {
        return playerDatas.stream().anyMatch(data -> data.getUser().equals(player));
    }

    public void update(final Player player) {
        if(!has(player)) {
            add(player);
            return;
        }

        remove(player);
    }
}

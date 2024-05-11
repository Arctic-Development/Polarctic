package ac.polarctic.plugin.manager;

import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Salers
 * made on ac.polarctic.plugin.manager
 */

public class PlayerDataManager {

    private final Map< UUID,PlayerData> playerDatas = new HashMap<>();

    public void add(final Player player) {
        this.playerDatas.put(player.getUniqueId(),new PlayerData(player.getUniqueId()));
    }

    public PlayerData get(final User player) {
        return playerDatas.get(player.getUUID());
    }

    public PlayerData get(final Player player) {
        return playerDatas.get(player.getUniqueId());
    }

    public void remove(final Player player) {
        this.playerDatas.remove(player.getUniqueId());
    }

    public boolean has(final Player player) {
        return playerDatas.containsKey(player.getUniqueId());
    }


    public boolean has(final User player) {
        return playerDatas.containsKey(player.getUUID());
    }


}

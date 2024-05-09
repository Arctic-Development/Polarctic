package ac.polarctic.plugin.data;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class DataManager {

	private Set<DataPlayer> dataSet = new HashSet<>();

	private final static DataManager INSTANCE = new DataManager();

	public DataPlayer getDataPlayer(Player player) {
		return dataSet.stream().filter(dataPlayer -> dataPlayer.getPlayer() == player).findFirst().orElse(null);
	}

	public void add(Player player) {
		dataSet.add(new DataPlayer(player));
	}

	public void remove(Player player) {
		dataSet.removeIf(dataPlayer -> dataPlayer.getPlayer() == player);
	}

	public static DataManager getInstance() {
		return INSTANCE;
	}

}

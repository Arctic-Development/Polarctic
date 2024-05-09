package ac.polarctic.plugin.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static int getPingNMS(Player player) {
		assert player != null;
		CraftPlayer nmsPlayer = ((CraftPlayer) player);
		if(nmsPlayer == null) return 0; 
		
		return nmsPlayer.getHandle().ping;
		
	}

}

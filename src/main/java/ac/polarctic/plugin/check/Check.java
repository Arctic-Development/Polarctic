package ac.polarctic.plugin.check;

import com.comphenix.protocol.events.PacketEvent;
import ac.polarctic.plugin.Polarctic;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;


public abstract class Check {

	protected DataPlayer data;
	protected String name,type;
	private int vl;

	public Check(DataPlayer data, String name,String type) {
		this.data = data;
		this.name = name;
		this.type = type;
	}

	public void onPacketReceive(PacketEvent packetEvent) {
		if (data.getPlayer().getGameMode() == GameMode.CREATIVE || data.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| data.getPlayer().getAllowFlight())
			return;
	}

	public void onPacketSend(PacketEvent event) {
		if (data.getPlayer().getGameMode() == GameMode.CREATIVE || data.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| data.getPlayer().getAllowFlight())
			return;
	}

	public void onMove(PlayerMoveEvent event) {
		if (data.getPlayer().getGameMode() == GameMode.CREATIVE || data.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| data.getPlayer().getAllowFlight())
			return;
	}

	protected void fail(String moreInfo) {
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("polarctic.alerts")) {
				  String toSend = ChatColor.translateAlternateColorCodes('&', Polarctic.getInstance().getConfig()
			                .getString("polarctic.alert-message").replaceAll("%player%", data.getPlayer().getName()).
			                        replaceAll("%check%", this.name).replaceAll("%vl%",
			                        String.valueOf(vl)).replaceAll("%type%", this.type).replaceAll("%info%", moreInfo));
				  vl++;
				  staff.sendMessage(toSend);
			}
		}
	}

}

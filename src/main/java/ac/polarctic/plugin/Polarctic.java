package ac.polarctic.plugin;

import ac.polarctic.plugin.check.Handler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Polarctic extends JavaPlugin {
	
	private static Polarctic instance;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		new Handler();
		Bukkit.getPluginManager().registerEvents(new Handler(), instance);
		
	}
	
	public static Polarctic getInstance() {
		return instance;
	}




}

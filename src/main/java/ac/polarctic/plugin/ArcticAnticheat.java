package ac.polarctic.plugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Salers
 * made on ac.polarctic.plugin
 */
public class ArcticAnticheat extends JavaPlugin {

    @Override
    public void onLoad() {
        Global.INSTANCE.onLoad(this);
    }

    @Override
    public void onEnable() {
        Global.INSTANCE.onEnable();
    }

    @Override
    public void onDisable() {
        Global.INSTANCE.onDisable();
    }
}

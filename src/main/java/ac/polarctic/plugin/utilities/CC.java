package ac.polarctic.plugin.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * @author Salers
 * made on gg.salers.anticheat.util
 */

@UtilityClass
public class CC {

    public String translate(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}

package ac.polarctic.plugin.check;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.annotation.Experimental;
import ac.polarctic.plugin.check.api.annotation.Testing;
import ac.polarctic.plugin.config.Config;
import ac.polarctic.plugin.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

/**
 * @author Salers
 * made on ac.polarctic.plugin.check
 */

@Getter
@Setter
public abstract class Check {

    protected final PlayerData playerData;
    protected final CheckInformation checkInformation;
    protected final boolean experimental, testing;
    protected double buffer = -1, violations;

    private final int minVL;
    private final String name;

    public Check(PlayerData playerData) {
        this.playerData = playerData;
        this.checkInformation = getClass().getAnnotation(CheckInformation.class);
        this.experimental = getClass().isAnnotationPresent(Experimental.class);
        this.testing = getClass().isAnnotationPresent(Testing.class);

        this.minVL = checkInformation.minVL();
        this.name = checkInformation.name();
    }

    public void flag(final String info) {
        this.violations++;
        if(this.violations >= minVL) {
            final String message = Config.ALERT_MESSAGE.translate()
                    .replaceAll("%player%", playerData.getPlayer().getName())
                    .replaceAll("%check%", name)
                    .replaceAll("%vl%", String.valueOf(violations - minVL))
                    .replace("%experimental%", Config.EXPERIMENTAL_NOTICE.translate())
                    .replaceAll("%info%", info);

            final TextComponent textComponent = new TextComponent(message);
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "" +
                    "/tp " + playerData.getPlayer().getName()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(info + "\n" + "\nClick to teleport to the player.").create()));
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(textComponent));

        }
    }

}

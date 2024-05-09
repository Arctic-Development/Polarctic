package ac.polarctic.plugin.config;


import ac.polarctic.plugin.Global;
import ac.polarctic.plugin.utilities.CC;
import lombok.Getter;

@Getter
public enum Config {
    ALERT_MESSAGE("alert-message", "&8[&bArctic&8] &f%player% &b- &f%check% &cx%vl% %experimental% &7(%info%)"),
    EXPERIMENTAL_NOTICE("experimental-notice", "(*E)");


    private final String path;
    private final Object value;

    Config(String path, Object value) {
        this.path = path;

        if (Global.INSTANCE.getPlugin().getConfig().contains(path)) {
            this.value = Global.INSTANCE.getPlugin().getConfig().get(path);
        } else {
            Global.INSTANCE.getPlugin().getConfig().set(path, value);
            Global.INSTANCE.getPlugin().saveConfig();

            this.value = value;
        }
    }

    public String translate() {
        return CC.translate((String) value);
    }
}

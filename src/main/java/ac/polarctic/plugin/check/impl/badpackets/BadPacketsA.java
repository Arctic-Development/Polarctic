package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.RotationCheck;
import ac.polarctic.plugin.data.PlayerData;

/**
 * account for teleports.
 */
@CheckInformation(name = "Bad Packets A", minVL = 1)
public class BadPacketsA extends Check implements RotationCheck {
    public BadPacketsA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onRotate() {
        float pitch = Math.abs(playerData.getRotationTracker().getPitch());

        if (pitch > 90F)
            flag(String.format("P %.2f", pitch));
    }
}

package ac.polarctic.plugin.check.impl.badpackets;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.EntityInteractionCheck;
import ac.polarctic.plugin.data.PlayerData;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInformation(name = "Bad Packets B", minVL = 1)
public class BadPacketsB extends Check implements EntityInteractionCheck {
    public BadPacketsB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onInteract(int targetId, WrapperPlayClientInteractEntity.InteractAction type) {
        int entityId = playerData.getPlayer().getEntityId();

        if (targetId == entityId)
            flag("Self Interact");
    }
}

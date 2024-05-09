package ac.polarctic.plugin.check.api.types;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

public interface EntityInteractionCheck {

    void onInteract(final int targetId, final WrapperPlayClientInteractEntity.InteractAction type);
}

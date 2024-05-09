package ac.polarctic.plugin.data.processor;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.types.EntityInteractionCheck;
import ac.polarctic.plugin.check.api.types.MovementCheck;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.check.api.types.RotationCheck;
import ac.polarctic.plugin.check.impl.move.MoveXZ;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.processor.api.IProcessor;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.processor
 */
public class CheckProcessor extends IProcessor {

    @Getter
    private final List<Check> checks;
    private final List<PacketCheck> packetChecks;
    private final Collection<? extends MovementCheck> movementChecks;
    private final Collection<? extends RotationCheck> rotationChecks;
    private Collection<? extends EntityInteractionCheck> entityInteractionChecks;

    public CheckProcessor(PlayerData playerData) {
        super(playerData);
        this.checks = Arrays.asList(
                new MoveXZ(playerData)
        );

        this.packetChecks = this.checks.stream().filter(check -> check instanceof PacketCheck)
                .map(check -> (PacketCheck) check).collect(Collectors.toList());
        this.movementChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(MovementCheck.class)).map(check -> (MovementCheck) check).collect(Collectors.toList());
        this.rotationChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(RotationCheck.class)).map(check -> (RotationCheck) check).collect(Collectors.toList());
        this.entityInteractionChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(EntityInteractionCheck.class)).map(check -> (EntityInteractionCheck) check).collect(Collectors.toList());
    }




    @Override
    public void onReceive(PacketReceiveEvent event) {
        if(PacketUtil.isFlying(event.getPacketType())) {
            if(PacketUtil.isPosition(event.getPacketType())) {
                this.movementChecks.forEach(MovementCheck::onMove);
            } else if(PacketUtil.isRotation(event.getPacketType())) {
                this.rotationChecks.forEach(RotationCheck::onRotate);
            }
        } else if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            final WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            this.entityInteractionChecks.forEach(entityInteractionCheck ->
                    entityInteractionCheck.onInteract(wrapper.getEntityId(), wrapper.getAction()));
        }

        packetChecks.forEach(packetCheck -> packetCheck.onPacket(event));



    }

    @Override
    public void onSend(PacketSendEvent event) {

    }
}

package ac.polarctic.plugin.data.processor;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.types.EntityInteractionCheck;
import ac.polarctic.plugin.check.api.types.MovementCheck;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.check.api.types.RotationCheck;
import ac.polarctic.plugin.check.impl.autoclicker.*;
import ac.polarctic.plugin.check.impl.badpackets.*;
import ac.polarctic.plugin.check.impl.emulation.EmulationHorizontal;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.processor.api.IProcessor;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lombok.Getter;

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
    private final Collection<? extends EntityInteractionCheck> entityInteractionChecks;

    public CheckProcessor(PlayerData playerData) {
        super(playerData);

        this.checks = Arrays.asList(
                new EmulationHorizontal(playerData),

                new AutoClickerA(playerData),
                new AutoClickerB(playerData),
                new AutoClickerC(playerData),
                new AutoClickerD(playerData),
                new AutoClickerE(playerData),
                new AutoClickerF(playerData),

                new BadPacketsA(playerData),
                new BadPacketsB(playerData),
                new BadPacketsC(playerData),
                new BadPacketsD(playerData),
                new BadPacketsE(playerData),
                new BadPacketsF(playerData),
                new BadPacketsG(playerData)
        );

        this.packetChecks = this.checks.stream().filter(check -> check instanceof PacketCheck)
                .map(check -> (PacketCheck) check).collect(Collectors.toList());
        this.movementChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(MovementCheck.class)).map(check -> (MovementCheck) check).collect(Collectors.toList());
        this.rotationChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(RotationCheck.class)).map(check -> (RotationCheck) check).collect(Collectors.toList());
        this.entityInteractionChecks = this.checks.stream().filter(check -> Arrays.asList(check.getClass().getInterfaces())
                .contains(EntityInteractionCheck.class)).map(check -> (EntityInteractionCheck) check).collect(Collectors.toList());

        this.handleTestingCheck();
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

    private void handleTestingCheck() {
        if(this.checks.stream().anyMatch(Check::isTesting))
            checks.removeIf(check -> !check.isTesting());
    }
}

package ac.polarctic.plugin.check.impl.autoclicker;

import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.sim0n.iridium.math.statistic.Stats;

import java.util.ArrayList;
import java.util.List;

@CheckInformation(name = "Auto Clicker B", minVL = 2)
public class AutoClickerB extends PacketCheck {
    private final List<Integer> samples = new ArrayList<>();

    private int movements;

    public AutoClickerB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION && !actionTracker.isDigging()) {
            if (movements < 10) {
                samples.add(movements);

                if (samples.size() == 70) {
                    long outliers = samples.stream()
                            .filter(d -> d == 0L)
                            .count();

                    double cps = 20D / Stats.mean(samples);

                    if (cps >= 16 && outliers == 0L)
                        flag(String.format("CPS %.2f", cps));

                    samples.clear();
                }
            }

            movements = 0;
        } else if (PacketUtil.isFlying(event.getPacketType())) {
            ++movements;
        }
    }
}

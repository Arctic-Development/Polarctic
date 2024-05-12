package ac.polarctic.plugin.check.impl.autoclicker;

import ac.polarctic.plugin.check.Check;
import ac.polarctic.plugin.check.api.annotation.CheckInformation;
import ac.polarctic.plugin.check.api.types.PacketCheck;
import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.sim0n.iridium.math.statistic.Stats;

import java.util.ArrayList;
import java.util.List;

@CheckInformation(name = "Auto Clicker D", minVL = 3)
public class AutoClickerD extends PacketCheck {
    private final List<Integer> samples = new ArrayList<>();

    private int movements;

    public AutoClickerD(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION && !actionTracker.isDigging()) {
            if (movements < 10) {
                samples.add(movements);

                if (samples.size() == 200) {
                    double sd = Stats.stdDev(samples);

                    double cps = 20D / Stats.mean(samples);

                    if (cps > 8 && sd < 0.45)
                        flag(String.format("CPS %.2f SD %.2f", cps, sd));

                    samples.clear();
                }
            }

            movements = 0;
        } else if (PacketUtil.isFlying(event.getPacketType())) {
            ++movements;
        }
    }
}

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

@CheckInformation(name = "Auto Clicker C", minVL = 2)
public class AutoClickerC extends PacketCheck {
    private final List<Integer> samples = new ArrayList<>();

    private int movements;

    private static final int OUTLIER = 3;

    public AutoClickerC(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION && !actionTracker.isDigging()) {
            if (movements < 10) {
                samples.add(movements);

                if (samples.size() == 1000) {
                    int outliers = (int) samples.stream()
                            .filter(d -> d > OUTLIER)
                            .count();

                    double cps = 20D / Stats.mean(samples);

                    if (cps > 8 && outliers < 7)
                        flag(String.format("CPS %.2f O %s", cps, outliers));

                    samples.clear();
                }
            }

            movements = 0;
        } else if (PacketUtil.isFlying(event.getPacketType())) {
            ++movements;
        }
    }
}

package ac.polarctic.plugin.data.tracker;

import ac.polarctic.plugin.data.PlayerData;
import ac.polarctic.plugin.data.tracker.api.PlayerTracker;
import ac.polarctic.plugin.utilities.MathUtil;
import ac.polarctic.plugin.utilities.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Salers
 * made on ac.polarctic.plugin.data.tracker
 */

@Getter
public class RotationTracker extends PlayerTracker {

    private final Set<Integer> candidates = new HashSet<>();

    private float yaw, pitch, lastYaw, lastPitch, deltaYaw,
            deltaPitch, lastDeltaYaw, lastDeltaPitch, joltYaw, joltPitch,
            lastJoltYaw, lastJoltPitch, yawAccel, pitchAccel,
            lastYawAccel, lastPitchAccel;

    private double gcdYaw, gcdPitch, absGcdPitch, absGcdYaw;

    private int sensitivity, calcSensitivity;

    public RotationTracker(PlayerData data) {
        super(data);
    }

    @Override
    public void onReceive(PacketReceiveEvent event) {
        if (PacketUtil.isFlying(event.getPacketType())) {
            WrapperPlayClientPlayerFlying playerFlying = new WrapperPlayClientPlayerFlying(event);

            lastYaw = yaw;
            lastPitch = pitch;

            lastJoltPitch = joltPitch;
            lastJoltYaw = joltYaw;

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;

            lastYawAccel = yawAccel;
            lastPitchAccel = pitchAccel;
            if (PacketUtil.isRotation(event.getPacketType())) {

                yaw = playerFlying.getLocation().getYaw();
                pitch = playerFlying.getLocation().getPitch();

                yawAccel = Math.abs(deltaYaw - lastDeltaYaw);
                pitchAccel = Math.abs(deltaPitch - lastDeltaPitch);

                deltaYaw = Math.abs(yaw - lastYaw);
                deltaPitch = Math.abs(pitch - lastPitch);

                joltYaw = Math.abs(deltaYaw - lastDeltaYaw);
                joltPitch = Math.abs(deltaPitch - lastDeltaPitch);

                gcdYaw = MathUtil.getGcd(deltaYaw, lastDeltaYaw);
                gcdPitch = MathUtil.getGcd(deltaPitch, lastDeltaPitch);

                absGcdYaw = MathUtil.getGcd(Math.abs(deltaYaw), Math.abs(lastDeltaYaw));
                absGcdPitch = MathUtil.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

                processSensitivity();

            }
        }
    }



    @Override
    public void onSend(PacketSendEvent event) {

    }

    @Override
    public void onPostReceive(PacketReceiveEvent event) {

    }

    public void processSensitivity() {
        if (Math.abs(pitch) != 90.0f) {
            float distanceY = pitch - lastPitch;

            double error = Math.max(Math.abs(pitch), Math.abs(lastPitch)) * 3.814697265625E-6;

            computeSensitivity(distanceY, error);
        }

        float distanceX = circularDistance(yaw, lastYaw);

        double error = Math.max(Math.abs(yaw), Math.abs(lastYaw)) * 3.814697265625E-6;

        computeSensitivity(distanceX, error);

        if (candidates.size() == 1) {
            calcSensitivity = candidates.iterator().next();
            sensitivity = 200 * calcSensitivity / 143;
        } else {
            sensitivity = -1;
            forEach(candidates::add);
        }
    }

    public void computeSensitivity(double delta, double error) {
        double start = delta - error;
        double end = delta + error;

        forEach(s -> {
            double f0 = ((double) s / 142.0) * 0.6 + 0.2;
            double f = (f0 * f0 * f0 * 8.0) * 0.15;

            int pStart = (int) Math.ceil(start / f);
            int pEnd = (int) Math.floor(end / f);

            if (pStart <= pEnd) {
                for (int p = pStart; p <= pEnd; p++) {
                    double d = p * f;

                    if (!(d >= start && d <= end)) {
                        candidates.remove(s);
                    }
                }
            } else {
                candidates.remove(s);
            }
        });
    }

    public float circularDistance(float a, float b) {
        float d = Math.abs(a % 360.0f - b % 360.0f);
        return d < 180.0f ? d : 360.0f - d;
    }

    public void forEach(Consumer<Integer> consumer) {
        for (int s = 0; s <= 143; s++) {
            consumer.accept(s);
        }
    }
}

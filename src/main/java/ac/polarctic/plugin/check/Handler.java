package ac.polarctic.plugin.check;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import ac.polarctic.plugin.Polarctic;
import ac.polarctic.plugin.check.checks.combat.aimassist.AimAssistPitch;
import ac.polarctic.plugin.check.checks.combat.killaura.KillAuraAngle;
import ac.polarctic.plugin.check.checks.combat.killaura.KillAuraPost;
import ac.polarctic.plugin.check.checks.combat.killaura.KillAuraRatio;
import ac.polarctic.plugin.check.checks.combat.killaura.KillAuraSwing;
import ac.polarctic.plugin.check.checks.combat.reach.Reach;
import ac.polarctic.plugin.check.checks.move.fly.FlyClimb;
import ac.polarctic.plugin.check.checks.move.fly.FlyYAccel;
import ac.polarctic.plugin.check.checks.move.speed.SpeedFriction;
import ac.polarctic.plugin.check.checks.move.speed.SpeedLimit;
import ac.polarctic.plugin.data.DataManager;
import ac.polarctic.plugin.data.DataPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Handler implements Listener {

	public void initChecks(DataPlayer data) {
		data.getChecks().add(new FlyYAccel(data, "Fly","ACCEL"));
		data.getChecks().add(new FlyClimb(data, "Fly","CLIMB"));
		data.getChecks().add(new SpeedFriction(data, "Speed", "FRICTION"));
		data.getChecks().add(new SpeedLimit(data, "Speed", "LIMIT"));
		data.getChecks().add(new KillAuraPost(data, "KillAura","POST"));
		data.getChecks().add(new KillAuraRatio(data, "KillAura","RATIO"));
		data.getChecks().add(new KillAuraSwing(data, "KillAura","SWING"));
		data.getChecks().add(new KillAuraAngle(data, "KillAura","ANGLE"));
		data.getChecks().add(new Reach(data,"Reach",""));
		data.getChecks().add(new AimAssistPitch(data, "AimAssist", "PITCH"));
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		DataManager.getInstance().add(event.getPlayer());
		this.initChecks(DataManager.getInstance().getDataPlayer(event.getPlayer()));
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		DataManager.getInstance().remove(event.getPlayer());
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		DataPlayer dataPlayer = DataManager.getInstance().getDataPlayer(event.getPlayer());
		for(Check checks : dataPlayer.getChecks()) {
			checks.onMove(event);
		}
	}
	
	public Handler() {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Polarctic.getInstance(),PacketType.values()) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				DataPlayer dataPlayer = DataManager.getInstance().getDataPlayer(event.getPlayer());
				if(dataPlayer == null) return;
				if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
					dataPlayer.setVictim( (LivingEntity) event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0));
				}
			
				for(Check checks : dataPlayer.getChecks()) {
					checks.onPacketReceive(event);
				}
			}
			
			@Override
			public void onPacketSending(PacketEvent event) {
				DataPlayer dataPlayer = DataManager.getInstance().getDataPlayer(event.getPlayer());
				if(dataPlayer == null) return;
				for(Check checks : dataPlayer.getChecks()) {
					checks.onPacketSend(event);
				}
			}
		});
	}
	
	

}

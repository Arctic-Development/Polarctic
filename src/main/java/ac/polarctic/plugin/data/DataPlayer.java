package ac.polarctic.plugin.data;

import ac.polarctic.plugin.check.Check;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DataPlayer {
	
	private Player player;
	private LivingEntity victim;
	
	private List<Check> checks = new ArrayList<>();
	private List<Vector> pastVictimVectors;

	public List<Check> getChecks() {
		return checks;
	}
	
	public DataPlayer(Player player) {
		this.player = player;
		this.pastVictimVectors = new ArrayList<>();
	
		
	}

	public Player getPlayer() {
		return player;
	}

	public LivingEntity getVictim() {
		return victim;
	}

	public void setVictim(LivingEntity victim) {
		this.victim = victim;
	}

	public List<Vector> getPastVictimVectors() {
		return pastVictimVectors;
	}

	
	
	
	
	
	
}

package tk.imdt.javadev;

import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

public class BossBarCountDown {
	Plugin plugin;
	BossBar bar;
	Plugin MVplugin;
	WorldRegen worldRegen;
	
	public BossBarCountDown(Plugin plugin, Plugin MVplugin) {
		this.plugin = plugin;
		this.MVplugin = MVplugin;
		//this.world = ((MultiverseCore) this.MVplugin).getMVWorldManager().getMVWorld(((Main) this.plugin).getResourcesWorldName());
		this.worldRegen = new WorldRegen(this.plugin, this.MVplugin);
		init();
	}
	private void init() {
		this.bar = this.plugin.getServer().createBossBar("TempName", BarColor.RED, BarStyle.SOLID);
		this.bar.setProgress(1.0);
	}
	public void updateBar() {
		updateProgress();
		updatePlayers();
	}
	private void updateProgress() {
		long nextRegenInSec = (this.plugin.getConfig().getLong("config.nextregen") - System.currentTimeMillis()) / 1000;
		double remainsTimeInBlinder = Double.parseDouble(nextRegenInSec + "") / Double.parseDouble(this.plugin.getConfig().getLong("config.regencycleinsec") + "");
		if (nextRegenInSec == 0) {
			this.bar.setTitle("Regenerating ...");
		} else {
			this.bar.setTitle("Next Regeneration in " + nextRegenInSec + " Second(s)");
		}
		try {
			this.bar.setProgress(remainsTimeInBlinder);
		} catch (IllegalArgumentException e) {}
	}
	private void updatePlayers() {
		try {
			List<Player> playersInThisWorld = this.worldRegen.getPlayerInResourceWorld();
			List<Player> playersInBarList = this.bar.getPlayers();
			for (Player currentPlayer : playersInThisWorld) {
				this.bar.addPlayer(currentPlayer);
			}
			for (Player currentPlayer : playersInBarList) {
				if (!playersInThisWorld.contains(currentPlayer)) {
					this.bar.removePlayer(currentPlayer);
				}
			}
		} catch (IllegalStateException e) {}
	}
}

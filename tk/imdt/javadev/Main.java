package tk.imdt.javadev;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static boolean isTrying = false;
	private Plugin MVplugin;
	private String resourceWorldName;
	private String spawnWorldName;
	private ConsoleCommandSender consoleSender;
	private WorldRegen regen;
	private BossBarCountDown bossBarCountDown;
	//ConsoleCommandSender
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.MVplugin = getServer().getPluginManager().getPlugin("Multiverse-Core");
		this.resourceWorldName = getConfig().getString("config.resourceworldname");
		getConfig().getLong("config.regencycleinsec");
		this.spawnWorldName = getConfig().getString("config.spawnworldname");
		this.consoleSender = getServer().getConsoleSender();
		this.regen = new WorldRegen(this, this.MVplugin);
		this.bossBarCountDown = new BossBarCountDown(this, this.MVplugin);
		if (!(getConfig().getLong("config.nextregen") > 0)) {
			getConfig().set("config.nextregen", System.currentTimeMillis() + 1000L * getConfig().getLong("config.regencycleinsec"));
			saveConfig();
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
            	updateBossBar();
            	long currentTime = System.currentTimeMillis();
            	if (currentTime >= getConfig().getLong("config.nextregen")) {
            		tryRegen();
            	}
            }
        }, 0, 20);
		
	}
	public void updateBossBar() {
		this.bossBarCountDown.updateBar();
	}
	public void tryRegen() {
		if (!isTrying) {
			isTrying = true;
			try {
				forceRegen();
			} catch (Exception e) {}
			isTrying = false;
		}
		
	}
	
	public String getResourcesWorldName() {
		return this.resourceWorldName;
	}
	public String getSpawnWorldName() {
		return this.spawnWorldName;
	}
	public ConsoleCommandSender getConsoleCommandSender() {
		return this.consoleSender;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("regensourcesworld") && (sender.isOp() || sender.hasPermission("regensourcesworld.admin"))) {
			tryRegen();
			return true;
		}
		return false;
	}
	public void forceRegen() {
		this.regen.runFunction();
		getConfig().set("config.nextregen", (System.currentTimeMillis() + 1000L * getConfig().getLong("config.regencycleinsec")));
		saveConfig();
	}
	@Override
	public void onDisable() {
		saveConfig();
	}
}

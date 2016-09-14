package tk.imdt.javadev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.commands.ConfirmCommand;
import com.onarandombox.MultiverseCore.commands.RegenCommand;
import com.onarandombox.MultiverseCore.commands.TeleportCommand;

public class WorldRegen {
	private Plugin plugin;
	private Plugin MVplugin;
	private MVWorldManager worldManager;
	private MultiverseCore mvCore;
	private Main PluginMain;
	
	public WorldRegen(Plugin plugin, Plugin MVplugin) {
		this.plugin = plugin;
		this.MVplugin = MVplugin;
		this.worldManager = ((MultiverseCore) this.MVplugin).getMVWorldManager();
		this.mvCore = (MultiverseCore) this.MVplugin;
		this.PluginMain = (Main) plugin;
	}
	public void runFunction() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Resources World Regenerating");
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Resources World Regenerating");
		sendPlayersBack(getPlayerInResourceWorld());
		regenMap();
		confirmCommand();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Resources World Regeneration Complete");
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Resources World Regeneration Complete");
	}
	private void confirmCommand() {
		ConfirmCommand confirm = new ConfirmCommand(this.mvCore);
		confirm.runCommand(this.PluginMain.getConsoleCommandSender(), null);
	}
	private void regenMap() {
		Random rnd = new Random();
		RegenCommand regenCmd = new RegenCommand(this.mvCore);
		long rndSeed = rnd.nextLong();
		List<String> regenCmdString = new ArrayList<>();
		regenCmdString.add(this.PluginMain.getResourcesWorldName());
		regenCmdString.add("-s");
		regenCmdString.add(rndSeed + "");
		regenCmd.runCommand(this.PluginMain.getConsoleCommandSender(), regenCmdString);
	}
	public List<Player> getPlayerInResourceWorld() {
        	MultiverseWorld world;
		world = this.worldManager.getMVWorld(this.PluginMain.getResourcesWorldName());
        	List<Player> players = world.getCBWorld().getPlayers();
        	return players;
	}
	private void sendPlayersBack(List<Player> players) {
		TeleportCommand tpcmd = new TeleportCommand(this.mvCore);
		List<String> cmd = new ArrayList<>();
		cmd.add("playerName");
		cmd.add(this.PluginMain.getSpawnWorldName());
		if (players.size() > 0) {
			for (Player player : players) {
				cmd.set(0, player.getDisplayName());
				tpcmd.runCommand(this.PluginMain.getConsoleCommandSender(), cmd);
			}
		}
	}
}

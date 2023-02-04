package xyz.konstl01.bedrockremover;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.konstl01.bedrockremover.commands.BedrockRemoved;

public class BedrockRemover extends JavaPlugin {
    public static World world;
    public static int count;
    public String stringWorld;
    private BukkitTask task;

    public static String getPrefix() {
        return "[BedrockRemover] ";
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        BedrockRemover.count = getConfig().getInt("amount");
        stringWorld = getConfig().getString("world");
        getCommand("bedrock-removed").setExecutor(new BedrockRemoved());
        getCommand("brreload").setExecutor(new ReloadCMD());
        getWorld();
        if (world != null) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Remover(), 20L, 20L);
            LagChecker();
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "Plugin started");
        }
        return;
    }

    public void getWorld() {
        Bukkit.getConsoleSender().sendMessage("World executed");
        if (stringWorld == null) {
            Bukkit.getConsoleSender().sendMessage(getPrefix()+"World in config cannot be NULL!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            world = getServer().getWorld(stringWorld);
        }
    }

    public void LagChecker() {
        if (getServer().getTPS()[0] < 17) {
            task.cancel();
            Bukkit.getConsoleSender().sendMessage(getPrefix()+"TPS are too low, pausing task");
        } else {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Remover(), 20L, 20L);
            Bukkit.getConsoleSender().sendMessage(getPrefix()+"TPS recovered, continuing task.");
        }
        return;
    }

    private class ReloadCMD implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            onDisable();
            onEnable();
            sender.sendMessage(BedrockRemover.getPrefix() + "Reloaded");
            sender.sendMessage(stringWorld);
            return true;
        }
    }

    public void onDisable() {
        task.cancel();
        Bukkit.getConsoleSender().sendMessage("stopping");
        getConfig().set("amount", BedrockRemover.count);
        getConfig().set("world", world);
        saveConfig();
    }
}
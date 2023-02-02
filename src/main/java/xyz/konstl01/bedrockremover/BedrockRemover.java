package xyz.konstl01.bedrockremover;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BedrockRemover extends JavaPlugin {
    private final World world;
    private BukkitRunnable task;
    private boolean running;
    public static int count;


    public static String getPrefix() {
        return "[BedrockRemover] ";
    }

    public BedrockRemover() {
        world = getServer().getWorld("ClymCityWorldNewNewNew");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        BedrockRemover.count = getConfig().getInt("amount");
        getCommand("bedrock-removed").setExecutor(new BedrockRemoved());
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (world.getLoadedChunks() == null) {
                    running = false;
                }
                if (getServer().getTPS()[0] < 17 && running) {
                    Bukkit.getLogger().fine(getPrefix()+"Server TPS is low, pausing BedrockRemover task");
                    running = false;
                    cancel();
                } else if (getServer().getTPS()[0] >= 17 && !running) {
                    Bukkit.getLogger().fine(getPrefix()+"Server TPS has recovered, resuming BedrockRemover task");
                    running = true;
                    runTaskTimer(BedrockRemover.this, 0, 20);
                }

                if (!running) {
                    return;
                }

                assert world != null;
                for (Chunk chunk : world.getLoadedChunks()) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 10; y > -2; y--) {
                                Block block = chunk.getBlock(x, y, z);

                                if (block.getType() == Material.BEDROCK) {
                                    block.setType(Material.DEEPSLATE);
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        };
        task.runTaskTimer(this, 0, 20);
    }
    public void onDisable() {
        getConfig().set("amount", BedrockRemover.count);
        saveConfig();
    }
}